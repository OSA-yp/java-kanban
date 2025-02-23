package managers;

import exceptions.NotFoundException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final TreeSet<Task> sortedTasks = new TreeSet<>();

    private final HistoryManager historyManager;
    private Integer nextTaskId = 1; // id задач всех типов, он всегда увеличивается, кроме удаления всех задач

    public InMemoryTaskManager() {
        this.historyManager = Managers.historyManager;
    }


    //    a. Получение списка всех задач.
    @Override
    public ArrayList<Task> getTasks() {

        return new ArrayList<>(tasks.values());
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    void sortTasks() { // метод должен использоваться единожды, при загрузке из внешнего источника
        List<Task> tasksToAdd = tasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .toList();

        List<SubTask> subTasksToAdd = subTasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .toList();

        sortedTasks.clear();

        sortedTasks.addAll(tasksToAdd);
        sortedTasks.addAll(subTasksToAdd);
    }

    @Override
    public ArrayList<Epic> getEpics() {

        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {

        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Task> getHistory() {
        return Managers.getDefaultHistory().getHistory();
    }

    //    b. Удаление всех задач.
    @Override
    public void removeAllTypesOfTasks() {
        removeAllTasks();
        removeAllEpics();
        removeAllSubTasks();
        nextTaskId = 1;
        sortedTasks.clear();
    }

    @Override
    public void removeAllTasks() {

        // предварительно удаляем все из истории
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
            sortedTasks.remove(tasks.get(taskId));
        }
        tasks.clear();

    }

    // удаление всех эпиков с удалением всех их подзадач
    @Override
    public void removeAllEpics() {

        // предварительно удаляем все из истории
        for (Integer taskId : epics.keySet()) {
            historyManager.remove(taskId);
        }
        epics.clear();
        removeAllSubTasks();
    }

    // Удаление всех подзадач у всех эпиков с обновлением статусов эпиков
    @Override
    public void removeAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.removeAllSubTasks();
            refreshEpicStatus(epic);
            refreshEpicTimeParams(epic);
        }

        // предварительно удаляем все из истории
        for (Integer taskId : subTasks.keySet()) {
            historyManager.remove(taskId);
            sortedTasks.remove(subTasks.get(taskId));
        }
        subTasks.clear();
    }

    //    c. Получение по идентификатору.
    @Override
    public Optional<Task> getTaskById(Integer id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        if (task == null) {
            throw new NotFoundException("Task not found");
        }
        return Optional.of(task);
    }

    @Override
    public Optional<SubTask> getSubTaskById(Integer id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);

        if (subTask == null) {
            throw new NotFoundException("Subtask not found");
        }

        return Optional.of(subTask);
    }

    @Override
    public Optional<Epic> getEpicById(Integer id) {

        Epic epic = epics.get(id);
        historyManager.add(epic);

        if (epic == null) {
            throw new NotFoundException("Epic not found");
        }

        return Optional.of(epic);
    }

    //    d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public int addTask(Task newTask) {

        if (newTask == null) {
            return -1;
        }

        if (hasIntersection(newTask)) {
            return -2;
        }

        newTask.setId(nextTaskId);
        tasks.put(newTask.getId(), newTask);
        nextTaskId++;
        sortedTasks.add(newTask);

        return newTask.getId();
    }

    private boolean hasIntersection(Task newTask) {
        List<Task> intersections = getPrioritizedTasks().stream()
                .filter(task ->
                        // ПФильтр для случаев апдейта, чтобы задачи не сравнивались сами с собой
                        !task.getId().equals(newTask.getId()))
                .filter(task ->
                        // Проверка на пересечение интервалов
                        (task.getStartTime().isBefore(newTask.getEndTime()) && task.getEndTime().isAfter(newTask.getStartTime())))

                .toList();

        return !intersections.isEmpty(); // Если есть пересечения, вернем true
    }


    @Override
    public int addEpic(Epic newEpic) {
        if (newEpic == null) {
            return -1;
        }

        // если добавить эпик с несуществующими подзадачами
        for (int subTaskId : newEpic.getSubTasksIds()) {
            if (!subTasks.containsKey(subTaskId)) {
                return -2;
            }
        }


        newEpic.setId(nextTaskId);
        epics.put(newEpic.getId(), newEpic);
        nextTaskId++;
        return newEpic.getId();
    }


    @Override
    public int addSubTask(SubTask newSubTask) {
        if (newSubTask == null) {
            return -1;
        }
        if (epics.get(newSubTask.getParentEpicId()) == null) {
            return -3;
        }

        if (hasIntersection(newSubTask)) {
            return -2;
        }

        newSubTask.setId(nextTaskId);
        subTasks.put(newSubTask.getId(), newSubTask);
        sortedTasks.add(newSubTask);
        nextTaskId++;

        // связываем эпик с подзадачей и подзадачу с эпиком
        Epic parentEpic = epics.get(newSubTask.getParentEpicId());
        parentEpic.addSubTask(newSubTask.getId());

        // обновляем статус родительского эпика
        refreshEpicStatus(parentEpic);

        // обновляем временные параметры родительского эпика
        refreshEpicTimeParams(parentEpic);


        return newSubTask.getId();
    }

    protected void refreshEpicTimeParams(Epic epic) {

        // Если у эпика нет подзадач, то временных параметров тоже нет
        if (epic.getSubTasksIds().isEmpty()) {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(0);
            return;
        }

        long durationSum = 0;
        LocalDateTime minStartTime = epic.getStartTime();
        LocalDateTime maxEndTime = epic.getEndTime();


        for (Task subTask : subTasks.values()) {

            durationSum += subTask.getDuration();
            if (minStartTime == null) {
                minStartTime = subTask.getStartTime();
            } else if (minStartTime.isAfter(subTask.getStartTime())) {
                minStartTime = subTask.getStartTime();
            }
            if (maxEndTime == null) {
                maxEndTime = subTask.getEndTime();
            } else if (maxEndTime.isBefore(subTask.getEndTime())) {
                maxEndTime = subTask.getEndTime();
            }
        }

        epic.setStartTime(minStartTime);
        epic.setDuration(durationSum);
        epic.setEndTime(maxEndTime);

    }


    //    e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public int updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return -1;
        }

        if (hasIntersection(task)) {
            return -2;
        }

        sortedTasks.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        sortedTasks.add(task);

        return 1;
    }

    @Override
    public int updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return -1;
        }

        // если у эпика несуществующие подзадачами
        for (int subTaskId : epic.getSubTasksIds()) {
            if (!subTasks.containsKey(subTaskId)) {
                return -2;
            }
        }


        Epic epicToUpdate = epics.get(epic.getId());
        epicToUpdate.setTitle(epic.getTitle());
        epicToUpdate.setDescription(epic.getDescription());

        return 1;
    }

    @Override
    public int updateSubTask(SubTask subTask) {
        if (!subTasks.containsKey(subTask.getId())) {
            return -1;
        }
        if (!Objects.equals(subTasks.get(subTask.getId()).getParentEpicId(), subTask.getParentEpicId())) {
            return -3;
        }

        if (hasIntersection(subTask)) {
            return -2;
        }
        sortedTasks.remove(subTasks.get(subTask.getId()));
        subTasks.put(subTask.getId(), subTask);
        sortedTasks.add(subTask);

        // обновляем статус родительского эпика
        refreshEpicStatus(epics.get(subTask.getParentEpicId()));
        refreshEpicTimeParams(epics.get(subTask.getParentEpicId()));


        return 1;
    }


    //    f. Удаление по идентификатору.
    @Override
    public int removeTaskById(Integer id) {
        if (!tasks.containsKey(id)) {
            return -1;
        }
        sortedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);

        return 1;
    }

    @Override
    public int removeEpicById(Integer id) {
        if (!epics.containsKey(id)) {
            return -1;
        }

        Epic epic = epics.get(id);

        // если удаляется эпик, удаляются и все его задачи
        if (!epic.getSubTasksIds().isEmpty()) {
            epic.getSubTasksIds().forEach(subTaskId -> {

                sortedTasks.remove(subTasks.get(subTaskId));

                subTasks.remove(subTaskId);

                historyManager.remove(subTaskId);

            });
        }
        epics.remove(id);
        historyManager.remove(id);
        return 1;
    }

    @Override
    public int removeSubTaskById(Integer id) {
        if (!subTasks.containsKey(id)) {
            return -1;
        }

        SubTask subTask = subTasks.get(id);
        sortedTasks.remove(subTask);
        subTasks.remove(id);
        historyManager.remove(id);
        // также удаляем подзадачу у эпика и обновляем его статус
        Epic subTaskEpic = epics.get(subTask.getParentEpicId());
        subTaskEpic.removeSubTask(id);
        refreshEpicStatus(subTaskEpic);
        refreshEpicTimeParams(subTaskEpic);
        return 1;
    }


    // a. Получение списка всех подзадач определённого эпика.
    @Override
    public ArrayList<SubTask> getSubtaskByEpicId(int epicId) {

        Epic epic = epics.get(epicId);
        ArrayList<SubTask> epicSubTasks = new ArrayList<>();
        if (epic != null) {
            epic.getSubTasksIds().stream()
                    .map(this::getSubTaskById) // Возвращает Optional<SubTask>
                    .forEach(optionalSubTask -> optionalSubTask.ifPresent(epicSubTasks::add));

        }

        return epicSubTasks;
    }


    private void refreshEpicStatus(Epic epic) {

        ArrayList<Integer> epicSubTasks = epic.getSubTasksIds();
        boolean allNew = true;
        boolean allDone = true;

        allNew = epicSubTasks.stream().allMatch(subTaskId -> subTasks.get(subTaskId).getStatus() == TaskStatus.NEW);
        allDone = epicSubTasks.stream().allMatch(subTaskId -> subTasks.get(subTaskId).getStatus() == TaskStatus.DONE);

        // если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
        if (subTasks.isEmpty() || allNew) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        // если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
        if (allDone) {
            epic.setStatus(TaskStatus.DONE);
            return;
        }

        // во всех остальных случаях статус должен быть IN_PROGRESS.
        epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    protected void loadTask(Task task) {
        tasks.put(task.getId(), task);

    }

    protected void loadSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
    }

    protected void loadEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        refreshEpicTimeParams(epic);

    }

    protected void setLastId(Integer lastId) {
        this.nextTaskId = lastId + 1;
    }

    protected Integer getLastId() {
        return this.nextTaskId - 1;
    }

}

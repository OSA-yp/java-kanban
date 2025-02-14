package managers;

import java.time.LocalDateTime;
import java.util.*;

import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private TreeSet<Task> sortedTasks = new TreeSet<>();

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

    private void sortTasks() {
        List<Task> tasksToAdd = tasks.values().stream()
                .filter(task -> task.getStartTime() != null)  // если у задачи нет времени (по ТЗ это так, по коду такого не бывает)
                .toList();

        List<SubTask> subTasksToAdd = subTasks.values().stream()
                .filter(task -> task.getStartTime() != null)  // если у задачи нет времени (по ТЗ это так, по коду такого не бывает)
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
        nextTaskId = 0;
        sortTasks();
    }

    @Override
    public void removeAllTasks() {

        // предварительно удаляем все из истории
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }

        tasks.clear();
        sortTasks();

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
        sortTasks();
    }

    // Удаление всех подзадач у всех эпиков с обновлением статусов эпиков
    @Override
    public void removeAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.removeAllSubTasks();
            refreshEpicStatus(epic);
        }

        // предварительно удаляем все из истории
        for (Integer taskId : subTasks.keySet()) {
            historyManager.remove(taskId);
        }
        subTasks.clear();
        sortTasks();
    }

    //    c. Получение по идентификатору.
    @Override
    public Optional<Task> getTaskById(Integer id) {
        Task task = tasks.get(id);
        historyManager.add(task);

        return Optional.ofNullable(task);
    }

    @Override
    public Optional<SubTask> getSubTaskById(Integer id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);
        return Optional.ofNullable(subTask);
    }

    @Override
    public Optional<Epic> getEpicById(Integer id) {

        Epic epic = epics.get(id);
        historyManager.add(epic);
        return Optional.ofNullable(epic);
    }

    //    d. Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public int addTask(Task newTask) {

        if (newTask == null) {
            return -1;
        }

        if (hasIntersection(newTask)) {
            return -1;
        }

        newTask.setId(nextTaskId);
        tasks.put(newTask.getId(), newTask);
        nextTaskId++;
        sortTasks();

        return newTask.getId();
    }

    private boolean hasIntersection(Task newTask) {
        List<Task> intersections = getPrioritizedTasks().stream()
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
        newEpic.setId(nextTaskId);
        epics.put(newEpic.getId(), newEpic);
        nextTaskId++;
        sortTasks();
        return newEpic.getId();
    }


    @Override
    public int addSubTask(SubTask newSubTask) {
        if (newSubTask == null) {
            return -1;
        }
        if (epics.get(newSubTask.getParentEpicId()) == null) {
            return -1;
        }

        if (hasIntersection(newSubTask)) {
            return -1;
        }

        newSubTask.setId(nextTaskId);
        subTasks.put(newSubTask.getId(), newSubTask);
        nextTaskId++;

        // связываем эпик с подзадачей и подзадачу с эпиком
        Epic parentEpic = epics.get(newSubTask.getParentEpicId());
        parentEpic.addSubTask(newSubTask.getId());

        // обновляем статус родительского эпика
        refreshEpicStatus(parentEpic);

        // обновляем временные параметры родительского эпика
        refreshEpicTimeParams(parentEpic);

        sortTasks();
        return newSubTask.getId();
    }

    private void refreshEpicTimeParams(Epic parentEpic) {
        long durationSum = 0;
        LocalDateTime minStartTime = parentEpic.getStartTime();
        LocalDateTime maxEndTime = parentEpic.getEndTime();

        for (Task subTask : subTasks.values()) {
            durationSum += subTask.getDuration();
            if (minStartTime.isAfter(subTask.getStartTime())) {
                minStartTime = subTask.getStartTime();
            }
            if (maxEndTime.isBefore(subTask.getEndTime())) {
                maxEndTime = subTask.getEndTime();
            }
        }

        parentEpic.setStartTime(minStartTime);
        parentEpic.setDuration(durationSum);
        parentEpic.setEndTime(maxEndTime);

    }


    //    e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    @Override
    public boolean updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return false;
        }
        tasks.put(task.getId(), task);
        sortTasks();
        return true;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return false;
        }
        Epic epicToUpdate = epics.get(epic.getId());
        epicToUpdate.setTitle(epic.getTitle());
        epicToUpdate.setDescription(epic.getDescription());
        sortTasks();

        return true;
    }

    @Override
    public boolean updateSubTask(SubTask subTask) {
        if (!subTasks.containsKey(subTask.getId())) {
            return false;
        }
        if (!Objects.equals(subTasks.get(subTask.getId()).getParentEpicId(), subTask.getParentEpicId())) {
            return false;
        }
        subTasks.put(subTask.getId(), subTask);

        // обновляем статус родительского эпика
        refreshEpicStatus(epics.get(subTask.getParentEpicId()));

        sortTasks();

        return true;
    }


    //    f. Удаление по идентификатору.
    @Override
    public boolean removeTaskById(Integer id) {
        if (!tasks.containsKey(id)) {
            return false;
        }
        tasks.remove(id);
        historyManager.remove(id);
        sortTasks();
        return true;
    }

    @Override
    public boolean removeEpicById(Integer id) {
        if (!epics.containsKey(id)) {
            return false;
        }

        Epic epic = epics.get(id);

        // если удаляется эпик, удаляются и все его задачи
        if (!epic.getSubTasksIds().isEmpty()) {
            epic.getSubTasksIds().forEach(subTaskId -> {
                subTasks.remove(subTaskId);
                historyManager.remove(subTaskId);
            });
        }
        epics.remove(id);
        historyManager.remove(id);
        sortTasks();
        return true;
    }

    @Override
    public boolean removeSubTaskById(Integer id) {
        if (!subTasks.containsKey(id)) {
            return false;
        }

        SubTask subTask = subTasks.get(id);

        subTasks.remove(id);
        historyManager.remove(id);
        // также удаляем подзадачу у эпика и обновляем его статус
        Epic subTaskEpic = epics.get(subTask.getParentEpicId());
        subTaskEpic.removeSubTask(id);
        refreshEpicStatus(subTaskEpic);
        sortTasks();
        return true;
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
        sortTasks();
    }

    protected void loadSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        sortTasks();
    }

    protected void loadEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        sortTasks();
    }

    protected void setLastId(Integer lastId) {
        this.nextTaskId = lastId + 1;
    }

    protected Integer getLastId() {
        return this.nextTaskId - 1;
    }

}

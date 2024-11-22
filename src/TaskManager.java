import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private Integer nextTaskId = 1; // id задач всех типов, он всегда увеличивается, кроме удаления всех задач


    //    a. Получение списка всех задач.
    public ArrayList<Task> getTasks() {

        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {

        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getSubTasks() {

        return new ArrayList<>(subTasks.values());
    }

    //    b. Удаление всех задач.
    public void removeAllTypesOfTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        nextTaskId = 0;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    // удаление всех эпиков с удалением всех их подзадач
    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    // Удаление всех подзадач у всех эпиков с обновлением статусов эпиков
    public void removeAllSubTasks() {
        for (Epic epic : epics.values()
        ) {
            epic.removeAllSubTasks();
            refreshEpicStatus(epic);
        }
        subTasks.clear();
    }

    //    c. Получение по идентификатору.
    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public SubTask getSubTaskById(Integer id) {
        return subTasks.get(id);
    }

    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    //    d. Создание. Сам объект должен передаваться в качестве параметра.
    public void addTask(Task newTask) {
        if (newTask == null) {
            return;
        }
        newTask.setId(nextTaskId);
        tasks.put(newTask.getId(), newTask);
        nextTaskId++;
    }

    public void addEpic(Epic newEpic) {
        if (newEpic == null) {
            return;
        }
        newEpic.setId(nextTaskId);
        epics.put(newEpic.getId(), newEpic);
        nextTaskId++;
    }


    public void addSubTask(SubTask newSubTask) {
        if (newSubTask == null) {
            return;
        }
        if (epics.get(newSubTask.getParentEpicId()) == null) {
            return;
        }
        newSubTask.setId(nextTaskId);
        subTasks.put(newSubTask.getId(), newSubTask);
        nextTaskId++;

        // связываем эпик с подзадачей и подзадачу с эпиком
        Epic parentEpic = epics.get(newSubTask.getParentEpicId());
        parentEpic.addTask(newSubTask.getId());

        // обновляем статус родительского эпика
        refreshEpicStatus(parentEpic);
    }


    //    e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public boolean updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return false;
        }
        tasks.put(task.getId(), task);
        return true;
    }

    public boolean updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return false;
        }
        Epic epicToUpdate = epics.get(epic.getId());
        epicToUpdate.setTitle(epic.getTitle());
        epicToUpdate.setDescription(epic.getDescription());

        return true;
    }

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

        return true;
    }


    //    f. Удаление по идентификатору.
    public boolean removeTaskById(Integer id) {
        if (!tasks.containsKey(id)) {
            return false;
        }
        tasks.remove(id);
        return true;
    }

    public boolean removeEpicById(Integer id) {
        if (!epics.containsKey(id)) {
            return false;
        }

        Epic epic = epics.get(id);

        // если удаляется эпик, удаляются и все его задачи
        if (epic.getSubTasksIds().size() != 0) {
            for (Integer subTaskId : epic.getSubTasksIds()
            ) {
                subTasks.remove(subTaskId);
            }
        }
        epics.remove(id);
        return true;
    }

    public boolean removeSubTaskById(Integer id) {
        if (!subTasks.containsKey(id)) {
            return false;
        }

        SubTask subTask = subTasks.get(id);

        subTasks.remove(id);
        // также удаляем подзадачу у эпика и обновляем его статус
        Epic subTaskEpic = getEpicById(subTask.getParentEpicId());
        subTaskEpic.removeSubTask(id);
        refreshEpicStatus(subTaskEpic);
        return true;
    }


    // a. Получение списка всех подзадач определённого эпика.
    public ArrayList<SubTask> getSubtaskByEpic(int epicId) {

        Epic epic = epics.get(epicId);
        ArrayList<SubTask> epicSubTasks = new ArrayList<>();
        if (epic != null) {
            for (
                    Integer id : epic.getSubTasksIds()
            ) {
                epicSubTasks.add(getSubTaskById(id));
            }
        }

        return epicSubTasks;
    }

    private void refreshEpicStatus(Epic epic) {

        ArrayList<Integer> epicSubTasks = epic.getSubTasksIds();
        boolean allNew = true;
        boolean allDone = true;

        for (Integer subTaskId : epicSubTasks
        ) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask.getStatus() != TaskStatus.NEW) {
                allNew = false;
            }
            if (subTask.getStatus() != TaskStatus.DONE) {
                allDone = false;
            }

        }
        // если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
        if (subTasks.size() == 0 || allNew) {
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


}

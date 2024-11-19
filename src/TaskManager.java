import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private Integer nextTaskId = 0; // id задач всех типов, он всегда увеличивается, кроме удаления всех задач


    //    a. Получение списка всех задач.
    public ArrayList<Task> getTasks() {

        ArrayList<Task> allTasks = new ArrayList<>(tasks.values());

        return allTasks;
    }

    //    b. Удаление всех задач.
    public void removeAllTasks() {
        tasks.clear();
        nextTaskId = 0;
    }

    //    c. Получение по идентификатору.
    public Task getTaskById(Integer id) {
        return tasks.get(id);
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
    public void addTask(SubTask newSubTask, Epic parentEpic) {
        if (newSubTask == null) {
            return;
        }
        newSubTask.setId(nextTaskId);
        newSubTask.setParentEpic(parentEpic);
        tasks.put(newSubTask.getId(), newSubTask);
        nextTaskId++;
        parentEpic.addTask(newSubTask);
        parentEpic.refreshStatus();
    }




    //    e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }



    //    f. Удаление по идентификатору.
    public boolean removeTaskById(Integer id) {
        Task task =  getTaskById(id);

        // если удаляется эпик, но в нём есть задачи, то удалять его нельзя
        if (task.getClass() == Epic.class && ((Epic) task).getSubTasks().size() != 0) {
            return false;
        }
        tasks.remove(id);
         // если это подзадача, то требуется удалить подзадачу её в эпике и обновить её эпик
        if (task.getClass() == SubTask.class) {
            Epic subTaskEpic = ((SubTask) task).getParentEpic();
            subTaskEpic.removeSubTask(id);
            subTaskEpic.refreshStatus();
        }
        return true;
    }

    // a. Получение списка всех подзадач определённого эпика.
    public ArrayList<SubTask> getTasks(Epic epic) {
        return epic.getSubTasks();
    }


}

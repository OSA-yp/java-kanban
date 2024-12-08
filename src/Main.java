import Managers.*;
import Tasks.*;

public class Main {
    public static void main(String[] args) {


        // тестирование Managers.TaskManager

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();


        // Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        Task task1 = new Task("title", "description", TaskStatus.NEW);
        Task task2 = new Task("Задача номер два", "Это вторая  тестовая задача для теста", TaskStatus.NEW);
        Task task2copy = new Task("Задача номер два", "Это вторая  тестовая задача для теста", TaskStatus.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task2copy);
        Integer task1Id = task1.getId();
        Integer task2Id = task2.getId();


        Epic epic1 = new Epic("Мой первый эпик", "Этот эпик будет содержать задачи для тестирования");
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Подзадача номер 1", "Это первая тестовая подзадача она входит в эпик 1", TaskStatus.NEW, epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача номер два", "Это 2я  тестовая подзадача она входит в эпик 1", TaskStatus.NEW, epic1.getId());
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        Integer subTask1Id = subTask1.getId();
        Integer epic1Id = epic1.getId();

        Epic epic2 = new Epic("Мой 2й эпик", "Этот эпик будет содержать 1 задачу для тестирования");
        taskManager.addEpic(epic2);
        SubTask subTask3 = new SubTask("Подзадача номер 3", "Это 3я тестовая подзадача она входит в эпик 2", TaskStatus.NEW, epic2.getId());
        taskManager.addSubTask(subTask3);
        Integer subTask3Id = subTask3.getId();
        Integer epic2Id = epic2.getId();

        // обновление эпика с предварительным добавлением ему задач
        Epic epicToUpdate1 = new Epic("Мой обновленный 2й эпик", epic2.getDescription());
        epicToUpdate1.setId(epic2.getId());
        for (Integer subId : epic2.getSubTasksIds()
        ) {
            epicToUpdate1.addSubTask(taskManager.getSubTaskById(subId).getId());
        }
        System.out.println("Обновление эпика с id=" + epicToUpdate1.getId() + " Успешно:" + taskManager.updateEpic(epicToUpdate1));

        // Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        printTasks(taskManager);

        // Измените статусы созданных объектов, распечатайте их. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        Task oldTask = taskManager.getTaskById(task1Id);
        Task taskToUpdate1 = new Task(oldTask.getTitle(), oldTask.getDescription(), TaskStatus.IN_PROGRESS);
        taskToUpdate1.setId(taskManager.getTaskById(task1Id).getId());
        System.out.println("Обновление задачи с id=" + taskToUpdate1.getId() + " Успешно:" + taskManager.updateTask(taskToUpdate1));


        SubTask oldSubTask1 = taskManager.getSubTaskById(subTask1Id);
        SubTask subTaskToUpdate2 = new SubTask(oldSubTask1.getTitle(), oldSubTask1.getDescription(), TaskStatus.DONE, oldSubTask1.getParentEpicId());
        subTaskToUpdate2.setId(oldSubTask1.getId());
        subTaskToUpdate2.setStatus(TaskStatus.DONE);
        System.out.println("Обновление подзадачи с id=" + subTaskToUpdate2.getId() + " Успешно:" + taskManager.updateSubTask(subTaskToUpdate2));
        taskManager.updateSubTask(subTaskToUpdate2);

        SubTask oldSubTask2 = taskManager.getSubTaskById(subTask3Id);
        SubTask subTaskToUpdate3 = new SubTask(oldSubTask2.getTitle(), oldSubTask2.getDescription(), TaskStatus.DONE, oldSubTask2.getParentEpicId());
        subTaskToUpdate3.setId(oldSubTask2.getId());
        System.out.println("Обновление подзадачи с id=" + subTaskToUpdate3.getId() + " Успешно:" + taskManager.updateSubTask(subTaskToUpdate3));

        printTasks(taskManager);

        System.out.println("История обращения к задачам:");
        System.out.println(historyManager.getHistory());
        System.out.println("_".repeat(60));



        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        System.out.println("Удаление подзадачи с id=" + subTask3Id + " Успешно:" + taskManager.removeSubTaskById(subTask3Id));
        System.out.println("Удаление задачи с id=" + task2Id + " Успешно:" + taskManager.removeTaskById(task2Id));
        System.out.println("Удаление эпика с id=" + epic1Id + " Успешно:" + taskManager.removeEpicById(epic1Id));

        printTasks(taskManager);

        System.out.println("Удаление эпика с id=" + epic2Id + " Успешно:" + taskManager.removeTaskById(epic2Id));



        // Удаление всех эпиков
        taskManager.removeAllEpics();
        System.out.println("Удаление всех эпиков");
        printTasks(taskManager);


        // удалить все задачи
        System.out.println("Удаление всех типов задач");
        taskManager.removeAllTypesOfTasks();

        printTasks(taskManager);
    }

    private static void printTasks(TaskManager tm) {

        // отдельные задачи
        for (Task task : tm.getTasks()
        ) {
            System.out.println(task);
        }

        // эпики с их подзадачами
        for (Epic epic : tm.getEpics()
        ) {
            System.out.println(epic);
            for (SubTask subTask : tm.getSubtaskByEpicId(epic.getId())
            ) {
                System.out.println(subTask);
            }
        }

        System.out.println("_".repeat(60));
    }
}
import managers.*;
import tasks.*;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {


        // Дополнительное задание. Реализуем пользовательский сценарий


        // тестирование Managers.TaskManager

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();


        // 1. Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
        Task task1 = new Task("Задача номер 1", "Это 1я  тестовая задача для теста", TaskStatus.NEW, LocalDateTime.now(), 5);
        Task task2 = new Task("Задача номер два", "Это вторая  тестовая задача для теста", TaskStatus.NEW, LocalDateTime.now().minusHours(1), 15);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        Integer task1Id = task1.getId();
        Integer task2Id = task2.getId();


        Epic epic1 = new Epic("Мой первый эпик", "Этот эпик будет содержать задачи для тестирования");
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Подзадача номер 1", "Это первая тестовая подзадача она входит в эпик 1", TaskStatus.NEW, LocalDateTime.now(), 60, epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача  номер два", "Это 2я  тестовая подзадача она входит в эпик 1", TaskStatus.NEW, LocalDateTime.now().minusMinutes(100), 15, epic1.getId());
        SubTask subTask3 = new SubTask("Подзадача  номер 3", "Это 3я  тестовая подзадача она входит в эпик 1", TaskStatus.NEW,LocalDateTime.now().minusMinutes(15), 5, epic1.getId());
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        Integer subTask1Id = subTask1.getId();
        Integer subTask2Id = subTask2.getId();
        Integer subTask3Id = subTask3.getId();
        Integer epic1Id = epic1.getId();

        Epic epic2 = new Epic("Мой 2й эпик", "Этот эпик не будет содержать задач");
        taskManager.addEpic(epic2);

        Integer epic2Id = epic2.getId();
        printHistory(historyManager);



        System.out.println("Запросите созданные задачи несколько раз в разном порядке:");
        taskManager.getEpicById(epic1Id);
        taskManager.getSubTaskById(subTask1Id);
        taskManager.getSubTaskById(subTask2Id);
        taskManager.getSubTaskById(subTask3Id);

        taskManager.getEpicById(epic1Id);  // повторный запрос

        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task2Id); // двойной запрос
        taskManager.getTaskById(task2Id);


        // После каждого запроса выведите историю и убедитесь, что в ней нет повторов
        printHistory(historyManager);

        System.out.println("Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться (task1Id = " + task1Id + "):");
        taskManager.removeTaskById(task1Id);
        printHistory(historyManager);


        System.out.println("Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи");
        taskManager.removeEpicById(epic1Id);
        printHistory(historyManager);
    }

    private static void printTasks(TaskManager tm) {
        System.out.println("Текущие задачи:");
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

    private static void printHistory(HistoryManager historyManager) {
        System.out.println("Текущая история:");
        for (Task task : historyManager.getHistory()
        ) {
            System.out.println(task);
        }
        System.out.println("_".repeat(60));
    }
}
import managers.*;
import tasks.*;

public class Main {
    public static void main(String[] args) {


        // �������������� �������. ��������� ���������������� ��������


        // ������������ Managers.TaskManager

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();


        // 1. �������� ��� ������, ���� � ����� ����������� � ���� ��� ��������.
        Task task1 = new Task("������ ����� 1", "��� 1�  �������� ������ ��� �����", TaskStatus.NEW);
        Task task2 = new Task("������ ����� ���", "��� ������  �������� ������ ��� �����", TaskStatus.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        Integer task1Id = task1.getId();
        Integer task2Id = task2.getId();


        Epic epic1 = new Epic("��� ������ ����", "���� ���� ����� ��������� ������ ��� ������������");
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("��������� ����� 1", "��� ������ �������� ��������� ��� ������ � ���� 1", TaskStatus.NEW, epic1.getId());
        SubTask subTask2 = new SubTask("���������  ����� ���", "��� 2�  �������� ��������� ��� ������ � ���� 1", TaskStatus.NEW, epic1.getId());
        SubTask subTask3 = new SubTask("���������  ����� 3", "��� 3�  �������� ��������� ��� ������ � ���� 1", TaskStatus.NEW, epic1.getId());
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        Integer subTask1Id = subTask1.getId();
        Integer subTask2Id = subTask2.getId();
        Integer subTask3Id = subTask3.getId();
        Integer epic1Id = epic1.getId();

        Epic epic2 = new Epic("��� 2� ����", "���� ���� �� ����� ��������� �����");
        taskManager.addEpic(epic2);

        Integer epic2Id = epic2.getId();
        printHistory(historyManager);



        System.out.println("��������� ��������� ������ ��������� ��� � ������ �������:");
        taskManager.getEpicById(epic1Id);
        taskManager.getSubTaskById(subTask1Id);
        taskManager.getSubTaskById(subTask2Id);
        taskManager.getSubTaskById(subTask3Id);

        taskManager.getEpicById(epic1Id);  // ��������� ������

        taskManager.getTaskById(task1Id);
        taskManager.getTaskById(task2Id); // ������� ������
        taskManager.getTaskById(task2Id);


        // ����� ������� ������� �������� ������� � ���������, ��� � ��� ��� ��������
        printHistory(historyManager);

        System.out.println("������� ������, ������� ���� � �������, � ���������, ��� ��� ������ ��� �� ����� ���������� (task1Id = " + task1Id + "):");
        taskManager.removeTaskById(task1Id);
        printHistory(historyManager);


        System.out.println("������� ���� � ����� ����������� � ���������, ��� �� ������� �������� ��� ��� ����, ��� � ��� ��� ���������");
        taskManager.removeEpicById(epic1Id);
        printHistory(historyManager);
    }

    private static void printTasks(TaskManager tm) {
        System.out.println("������� ������:");
        // ��������� ������
        for (Task task : tm.getTasks()
        ) {
            System.out.println(task);
        }

        // ����� � �� �����������
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
        System.out.println("������� �������:");
        for (Task task : historyManager.getHistory()
        ) {
            System.out.println(task);
        }
        System.out.println("_".repeat(60));
    }
}
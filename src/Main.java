import Managers.*;
import Tasks.*;

public class Main {
    public static void main(String[] args) {


        // ������������ Managers.TaskManager

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();


        // �������� ��� ������, � ����� ���� � ����� ����������� � ���� � ����� ����������.
        Task task1 = new Task("title", "description", TaskStatus.NEW);
        Task task2 = new Task("������ ����� ���", "��� ������  �������� ������ ��� �����", TaskStatus.NEW);
        Task task2copy = new Task("������ ����� ���", "��� ������  �������� ������ ��� �����", TaskStatus.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task2copy);
        Integer task1Id = task1.getId();
        Integer task2Id = task2.getId();


        Epic epic1 = new Epic("��� ������ ����", "���� ���� ����� ��������� ������ ��� ������������");
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("��������� ����� 1", "��� ������ �������� ��������� ��� ������ � ���� 1", TaskStatus.NEW, epic1.getId());
        SubTask subTask2 = new SubTask("��������� ����� ���", "��� 2�  �������� ��������� ��� ������ � ���� 1", TaskStatus.NEW, epic1.getId());
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        Integer subTask1Id = subTask1.getId();
        Integer epic1Id = epic1.getId();

        Epic epic2 = new Epic("��� 2� ����", "���� ���� ����� ��������� 1 ������ ��� ������������");
        taskManager.addEpic(epic2);
        SubTask subTask3 = new SubTask("��������� ����� 3", "��� 3� �������� ��������� ��� ������ � ���� 2", TaskStatus.NEW, epic2.getId());
        taskManager.addSubTask(subTask3);
        Integer subTask3Id = subTask3.getId();
        Integer epic2Id = epic2.getId();

        // ���������� ����� � ��������������� ����������� ��� �����
        Epic epicToUpdate1 = new Epic("��� ����������� 2� ����", epic2.getDescription());
        epicToUpdate1.setId(epic2.getId());
        for (Integer subId : epic2.getSubTasksIds()
        ) {
            epicToUpdate1.addSubTask(taskManager.getSubTaskById(subId).getId());
        }
        System.out.println("���������� ����� � id=" + epicToUpdate1.getId() + " �������:" + taskManager.updateEpic(epicToUpdate1));

        // ������������ ������ ������, ����� � �������� ����� System.out.println(..).
        printTasks(taskManager);

        // �������� ������� ��������� ��������, ������������ ��. ���������, ��� ������ ������ � ��������� ����������, � ������ ����� ����������� �� �������� ��������.
        Task oldTask = taskManager.getTaskById(task1Id);
        Task taskToUpdate1 = new Task(oldTask.getTitle(), oldTask.getDescription(), TaskStatus.IN_PROGRESS);
        taskToUpdate1.setId(taskManager.getTaskById(task1Id).getId());
        System.out.println("���������� ������ � id=" + taskToUpdate1.getId() + " �������:" + taskManager.updateTask(taskToUpdate1));


        SubTask oldSubTask1 = taskManager.getSubTaskById(subTask1Id);
        SubTask subTaskToUpdate2 = new SubTask(oldSubTask1.getTitle(), oldSubTask1.getDescription(), TaskStatus.DONE, oldSubTask1.getParentEpicId());
        subTaskToUpdate2.setId(oldSubTask1.getId());
        subTaskToUpdate2.setStatus(TaskStatus.DONE);
        System.out.println("���������� ��������� � id=" + subTaskToUpdate2.getId() + " �������:" + taskManager.updateSubTask(subTaskToUpdate2));
        taskManager.updateSubTask(subTaskToUpdate2);

        SubTask oldSubTask2 = taskManager.getSubTaskById(subTask3Id);
        SubTask subTaskToUpdate3 = new SubTask(oldSubTask2.getTitle(), oldSubTask2.getDescription(), TaskStatus.DONE, oldSubTask2.getParentEpicId());
        subTaskToUpdate3.setId(oldSubTask2.getId());
        System.out.println("���������� ��������� � id=" + subTaskToUpdate3.getId() + " �������:" + taskManager.updateSubTask(subTaskToUpdate3));

        printTasks(taskManager);

        System.out.println("������� ��������� � �������:");
        System.out.println(historyManager.getHistory());
        System.out.println("_".repeat(60));



        // �, �������, ���������� ������� ���� �� ����� � ���� �� ������.
        System.out.println("�������� ��������� � id=" + subTask3Id + " �������:" + taskManager.removeSubTaskById(subTask3Id));
        System.out.println("�������� ������ � id=" + task2Id + " �������:" + taskManager.removeTaskById(task2Id));
        System.out.println("�������� ����� � id=" + epic1Id + " �������:" + taskManager.removeEpicById(epic1Id));

        printTasks(taskManager);

        System.out.println("�������� ����� � id=" + epic2Id + " �������:" + taskManager.removeTaskById(epic2Id));



        // �������� ���� ������
        taskManager.removeAllEpics();
        System.out.println("�������� ���� ������");
        printTasks(taskManager);


        // ������� ��� ������
        System.out.println("�������� ���� ����� �����");
        taskManager.removeAllTypesOfTasks();

        printTasks(taskManager);
    }

    private static void printTasks(TaskManager tm) {

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
}
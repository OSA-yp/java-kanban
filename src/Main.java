public class Main {
    public static void main(String[] args) {


        // тестирование TaskManager

        TaskManager tm = new TaskManager();

        // Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        Task task1 = new Task("Задача номер 1", "Это первая тестовая задача", TaskStatus.NEW);
        Task task2 = new Task("Задача номер два", "Это вторая  тестовая задача для теста", TaskStatus.NEW);
        Task task2copy = new Task("Задача номер два", "Это вторая  тестовая задача для теста", TaskStatus.NEW);

        tm.addTask(task1);
        tm.addTask(task2);
        tm.addTask(task2copy);
        Integer task1Id = task1.getId();
        Integer task2Id = task2.getId();


        Epic epic1 = new Epic("Мой первый эпик", "Этот эпик будет содержать задачи для тестирования", TaskStatus.NEW);
        tm.addEpic(epic1);
        SubTask subTask1 = new SubTask("Подзадача номер 1", "Это первая тестовая подзадача она входит в эпик 1", TaskStatus.NEW, epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача номер два", "Это 2я  тестовая подзадача она входит в эпик 1", TaskStatus.NEW, epic1.getId());
        tm.addSubTask(subTask1, epic1);
        tm.addSubTask(subTask2, epic1);
        Integer subTask1Id = subTask1.getId();
        Integer epic1Id = epic1.getId();

        Epic epic2 = new Epic("Мой 2й эпик", "Этот эпик будет содержать 1 задачу для тестирования", TaskStatus.NEW);
        tm.addEpic(epic2);
        SubTask subTask3 = new SubTask("Подзадача номер 3", "Это 3я тестовая подзадача она входит в эпик 2", TaskStatus.NEW, epic2.getId());
        tm.addSubTask(subTask3, epic2);
        Integer subTask3Id = subTask3.getId();
        Integer epic2Id = epic2.getId();

        // обновление эпика с предварительным добавлением ему задач
        Epic epicToUpdate1 = new Epic("Мой обновленный 2й эпик", epic2.getDescription(), epic2.getStatus());
        epicToUpdate1.setId(epic2.getId());
        for (Integer subId: epic2.getSubTasksIds()
             ) {
            epicToUpdate1.addTask(tm.getSubTaskById(subId).getId());
        }
        tm.updateEpic(epicToUpdate1);


        // Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        printTasks(tm);

        // Измените статусы созданных объектов, распечатайте их. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        Task oldTask = tm.getTaskById(task1Id);
        Task taskToUpdate1 = new Task(oldTask.getTitle(), oldTask.getDescription(), TaskStatus.IN_PROGRESS);
        taskToUpdate1.setId(tm.getTaskById(task1Id).getId());
        tm.updateTask(taskToUpdate1);

        SubTask oldSubTask1 = tm.getSubTaskById(subTask1Id);
        SubTask subTaskToUpdate2 = new SubTask(oldSubTask1.getTitle(), oldSubTask1.getDescription(), TaskStatus.DONE, oldSubTask1.getParentEpicId());
        subTaskToUpdate2.setId(oldSubTask1.getId());
        subTaskToUpdate2.setStatus(TaskStatus.DONE);
        tm.updateSubTask(subTaskToUpdate2);

        SubTask oldSubTask2 = tm.getSubTaskById(subTask3Id);
        SubTask subTaskToUpdate3 = new SubTask(oldSubTask2.getTitle(), oldSubTask2.getDescription(), TaskStatus.DONE, oldSubTask2.getParentEpicId());
        subTaskToUpdate3.setId(oldSubTask2.getId());
        tm.updateSubTask(subTaskToUpdate3);

        printTasks(tm);


        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        System.out.println("Удаление подзадачи с id=" + subTask3Id + " Успешно:" + tm.removeSubTaskById(subTask3Id));
        System.out.println("Удаление задачи с id=" + task2Id + " Успешно:" + tm.removeTaskById(task2Id));
        System.out.println("Удаление эпик с id=" + epic1Id + " Успешно:" + tm.removeEpicById(epic1Id));

        printTasks(tm);

        System.out.println("Удаление эпик с id=" + epic2Id + " Успешно:" + tm.removeTaskById(epic2Id));

        // удалить все задачи
        System.out.println("Удаление всех задач");
        tm.removeAllTasks();

        printTasks(tm);
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
            for (Integer subId: epic.getSubTasksIds()
                 ) {
                System.out.println(tm.getSubTaskById(subId));
            }
        }

        System.out.println("_".repeat(60));
    }
}
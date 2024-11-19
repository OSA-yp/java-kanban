public class Main {
    public static void main(String[] args) {


        // тестирование TaskManager

        TaskManager tm = new TaskManager();

        // Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        Task task1 = new Task("Задача номер 1", "Это первая тестовая задача");
        Task task2 = new Task("Задача номер два", "Это вторая  тестовая задача для теста");
        Task task2copy = new Task("Задача номер два", "Это вторая  тестовая задача для теста");

        tm.addTask(task1);
        tm.addTask(task2);
        tm.addTask(task2copy);
        Integer task1Id = task1.getId();
        Integer task2Id = task2.getId();


        Epic epic1 = new Epic("Мой первый эпик", "Этот эпик будет содержать задачи для тестирования");
        SubTask subTask1 = new SubTask("Подзадача номер 1", "Это первая тестовая подзадача она входит в эпик 1" );
        SubTask subTask2 = new SubTask("Подзадача номер два", "Это 2я  тестовая подзадача она входит в эпик 1 ");

        tm.addTask(epic1);
        tm.addTask(subTask1, epic1);
        tm.addTask(subTask2, epic1);
        Integer subTask1Id = subTask1.getId();
        Integer epic1Id = epic1.getId();

        Epic epic2 = new Epic("Мой 2й эпик", "Этот эпик будет содержать 1 задачу для тестирования");
        SubTask subTask3 = new SubTask("Подзадача номер 3", "Это 3я тестовая подзадача она входит в эпик 2");

        tm.addTask(epic2);
        tm.addTask(subTask3, epic2);
        Integer subTask3Id = subTask3.getId();
        Integer epic2Id = epic2.getId();

        // Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        printTasks(tm);

        // Измените статусы созданных объектов, распечатайте их. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        Task oldTask = tm.getTaskById(task1Id);
        Task taskToUpdate1 = new Task(oldTask.getTitle(), oldTask.getDescription());
        taskToUpdate1.setId(tm.getTaskById(task1Id).getId());
        taskToUpdate1.setStatus(TaskStatus.IN_PROGRESS);
        tm.updateTask(taskToUpdate1);

        SubTask oldSubTask1 = (SubTask) tm.getTaskById(subTask1Id);
        SubTask subTaskToUpdate2 = new SubTask(oldSubTask1.getTitle(), oldSubTask1.getDescription());
        subTaskToUpdate2.setId(oldSubTask1.getId());
        subTaskToUpdate2.setParentEpic(oldSubTask1.getParentEpic());
        subTaskToUpdate2.setStatus(TaskStatus.DONE);
        tm.updateTask(subTaskToUpdate2);

        SubTask oldSubTask2 = (SubTask) tm.getTaskById(subTask3Id);
        SubTask subTaskToUpdate3 = new SubTask(oldSubTask2.getTitle(), oldSubTask2.getDescription());
        subTaskToUpdate3.setId(oldSubTask2.getId());
        subTaskToUpdate3.setParentEpic(oldSubTask2.getParentEpic());
        subTaskToUpdate3.setStatus(TaskStatus.DONE);
        tm.updateTask(subTaskToUpdate3);

        printTasks(tm);


        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        System.out.println("Удаление подзадачи с id="+ subTask3Id + " Успешно:" + tm.removeTaskById(subTask3Id));
        System.out.println("Удаление задачи с id="+ task2Id + " Успешно:" + tm.removeTaskById(task2Id));
        System.out.println("Удаление эпик с id="+ epic1Id + " Успешно:" + tm.removeTaskById(epic1Id));


        printTasks(tm);


        // Получить подзадачи эпика
        System.out.println("Подзадачи эпика " + epic1.getTitle() + " с id=" + epic1.getId() +  ":");
        for (SubTask subTask : tm.getTasks(epic1)
        ) {
            System.out.println(subTask);
        }

        System.out.println("Удаление эпик с id="+ epic2Id + " Успешно:" + tm.removeTaskById(epic2Id));

        // удалить все задачи
        System.out.println("Удаление всех задач");
        tm.removeAllTasks();

        printTasks(tm);

    }

    private static void printTasks(TaskManager tm) {
        for (Task task : tm.getTasks()
        ) {
            System.out.println(task);
        }
        System.out.println("_".repeat(60));
    }
}
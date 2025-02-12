package managers;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private File file;

    private ArrayList<SubTask> loadedSubTasks = new ArrayList<>();

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
        loadFromFile(this.file);
    }


    private void saveToFile() {

        ArrayList<String> stringsToWrite = new ArrayList<>();

        // для упрощенной модели файл перезаписывается полностью при каждом изменении задачи
        if (!this.file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        stringsToWrite.add("id,type,name,status,description,epic\n");

// TODO новые поля про время
        for (Task task : super.getTasks()
        ) {
            stringsToWrite.add(taskToString(task, TasksType.TASK) + "\n");
        }

        for (Epic epic : super.getEpics()
        ) {
            stringsToWrite.add(taskToString(epic, TasksType.EPIC) + "\n");
        }


        for (SubTask subTask : super.getSubTasks()
        ) {
            stringsToWrite.add(subTaskToString(subTask) + "\n");
        }

        // для упрощенной модели файл перезаписывается полностью при каждом изменении задачи (append = false)
        try (Writer fileWriter = new FileWriter(this.file, StandardCharsets.UTF_8, false)) {
            for (String stringToWrite : stringsToWrite) {
                fileWriter.write(stringToWrite);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Запись в файл " + file.getName() + " не удалась");
        }
    }

    private String taskToString(Task task, TasksType type) {
        StringBuilder result = new StringBuilder();

        result.append(task.getId());                // 0
        result.append(",");
        result.append(type.toString());             // 1
        result.append(",");
        result.append(task.getTitle());             // 2
        result.append(",");
        result.append(task.getStatus().toString()); // 3
        result.append(",");
        result.append(task.getDescription());       // 4
        result.append(",");
        result.append(task.getStartTime().toString()); // 5
        result.append(",");
        result.append(task.getDuration());          // 6
        result.append(",");

        return result.toString();
    }

    private String subTaskToString(SubTask subTask) {
        StringBuilder result = new StringBuilder(taskToString(subTask, TasksType.SUBTASK));

        result.append(subTask.getParentEpicId());

        return result.toString();
    }


    private void loadFromFile(File file) {


        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.startsWith("id")) {
                    continue; // для первой строки заголовков
                }
                if (!parseObjectFromString(line)) {
                    throw new ManagerLoadException("Загрузка из файла " + file.getName() + " не удалась");
                }
            }
        } catch (IOException | ManagerLoadException e) {
            System.out.println("Во время загрузки файла произошла ошибка");
        }

        // линкуем подзадачи и эпики после всей загрузки, чтобы не зависеть от порядка subTask/epic
        for (SubTask subtask : loadedSubTasks
        ) {
            Epic epic = super.getEpicById(subtask.getParentEpicId());
            epic.addSubTask(subtask.getId());
        }

    }

    private Boolean parseObjectFromString(String sting) {

        String[] items = sting.split(",");

        // id,type,name,status,description,epic
        Integer id = Integer.valueOf(items[0]);
        TasksType type = TasksType.valueOf(items[1]);
        String title = items[2];
        TaskStatus status = TaskStatus.valueOf(items[3]);
        String description = items[4];
        LocalDateTime startTime = LocalDateTime.parse(items[5]);
        long duration = Long.parseLong(items[6]);
        Integer subTaskParenEpic = null;
        if (items.length > 7) {
            subTaskParenEpic = Integer.valueOf(items[7]);

        }
        // update следующего id в taskManager
        if (id > super.getLastId()) {
            super.setLastId(id);
        }

        switch (type) {
            case TASK: {
                Task task = new Task(title, description, status, startTime, duration);
                task.setId(id);
                super.loadTask(task);
                return true;
            }
            case SUBTASK: {
                SubTask subTask = new SubTask(title, description, status, startTime, duration,  subTaskParenEpic);
                subTask.setId(id);
                super.loadSubTask(subTask);
                loadedSubTasks.add(subTask);
                return true;
            }
            case EPIC: {
                Epic epic = new Epic(title, description);
                epic.setId(id);
                epic.setStatus(status);
                super.loadEpic(epic);
                return true;
            }
        }
        return false;
    }


    @Override
    public List<Task> getPrioritizedTasks() {
        // TODO getPrioritizedTasks Можно хранить все задачи заранее отсортированными с помощью класса TreeSet.
        // подумать нужно ли хранить их в файле, вряд ли.
        return super.getPrioritizedTasks();
    }

    @Override
    public void removeAllTypesOfTasks() {
        super.removeAllTypesOfTasks();
        saveToFile();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        saveToFile();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        saveToFile();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        saveToFile();
    }

    @Override
    public void addTask(Task newTask) {
        super.addTask(newTask);
        saveToFile();


    }

    @Override
    public void addEpic(Epic newEpic) {
        super.addEpic(newEpic);
        saveToFile();
    }

    @Override
    public void addSubTask(SubTask newSubTask) {
        super.addSubTask(newSubTask);
        saveToFile();
    }

    @Override
    public boolean updateTask(Task task) {
        Boolean res = super.updateTask(task);
        saveToFile();
        return res;

    }

    @Override
    public boolean updateEpic(Epic epic) {
        Boolean res = super.updateEpic(epic);
        saveToFile();
        return res;
    }

    @Override
    public boolean updateSubTask(SubTask subTask) {
        Boolean res = super.updateSubTask(subTask);
        saveToFile();
        return res;
    }

    @Override
    public boolean removeTaskById(Integer id) {
        Boolean res = super.removeTaskById(id);
        saveToFile();
        return res;
    }

    @Override
    public boolean removeEpicById(Integer id) {
        Boolean res = super.removeEpicById(id);
        saveToFile();
        return res;
    }

    @Override
    public boolean removeSubTaskById(Integer id) {
        Boolean res = super.removeSubTaskById(id);
        saveToFile();
        return res;
    }


    public static void main(String[] args) {

        // TODO Решить что с этим делать, стоит ли оставлять, либо поменять на новый тест

        File file = new File(Managers.TASKS_FILE_NAME); // для целей тестирования удаляется предыдущий файл, если есть
        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        TaskManager taskManager = Managers.getDefault();

        //Заведите несколько разных задач, эпиков и подзадач.
        Task task1 = new Task("Задача номер 1", "Это 1я  тестовая задача для теста", TaskStatus.NEW, LocalDateTime.now().minusMinutes(60), 60);
        Task task2 = new Task("Задача номер два", "Это вторая  тестовая задача для теста", TaskStatus.NEW, LocalDateTime.now(), 15);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Мой первый эпик", "Этот эпик будет содержать задачи для тестирования");
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Подзадача номер 1", "Это первая тестовая подзадача она входит в эпик 1", TaskStatus.NEW,LocalDateTime.now(), 15, epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача  номер два", "Это 2я  тестовая подзадача она входит в эпик 1", TaskStatus.DONE, LocalDateTime.now(), 10, epic1.getId());
        SubTask subTask3 = new SubTask("Подзадача  номер 3", "Это 3я  тестовая подзадача она входит в эпик 1", TaskStatus.IN_PROGRESS, LocalDateTime.now().minusMinutes(45), 15, epic1.getId());
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);

        System.out.println("======================= первый менеджер =======================");
        System.out.println("Эпиков:" + taskManager.getEpics().size() + taskManager.getEpics());
        System.out.println("Подзадач:" + taskManager.getSubTasks().size() + taskManager.getSubTasks());
        System.out.println("Задач:" + taskManager.getTasks().size() + taskManager.getTasks());


        //Создайте новый FileBackedTaskManager-менеджер из этого же файла.
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File("tasksdb.csv"));


        //Проверьте, что все задачи, эпики, подзадачи, которые были в старом менеджере, есть в новом.
        System.out.println("======================= второй менеджер =======================");
        System.out.println("Эпиков:" + fileBackedTaskManager.getEpics().size() + fileBackedTaskManager.getEpics());
        System.out.println("Подзадач:" + fileBackedTaskManager.getSubTasks().size() + fileBackedTaskManager.getSubTasks());
        System.out.println("Задач:" + fileBackedTaskManager.getTasks().size() + fileBackedTaskManager.getTasks());
    }
}
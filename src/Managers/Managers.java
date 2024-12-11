package Managers;

public class Managers {

    static InMemoryHistoryManager historyManager= new InMemoryHistoryManager(10);
    static InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public static TaskManager getDefault(){
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }



}

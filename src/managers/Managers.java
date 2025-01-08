package managers;

public class Managers {

    static InMemoryHistoryManager historyManager= new InMemoryHistoryManager();
    static InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public static TaskManager getDefault(){
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }



}

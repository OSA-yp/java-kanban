package Managers;import Tasks.*;import java.util.ArrayList;import java.util.List;public class InMemoryHistoryManager implements HistoryManager {    ArrayList<Task> history = new ArrayList<>();    final int HISTORY_DEEP;    public InMemoryHistoryManager(int historyDeep) {        this.HISTORY_DEEP = historyDeep;    }    @Override    public void add(Task task) {        if (task == null) {            return;        }        history.add(task);        if (history.size() > HISTORY_DEEP) {            history.removeFirst();        }    }    @Override    public List<Task> getHistory() {        return new ArrayList<>(history);    }}
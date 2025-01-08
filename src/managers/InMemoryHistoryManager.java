package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {


    private HashMap<Integer, Node> history = new HashMap<>();
    private Integer firstId = null; // null для контроля первой записи в истории
    private Integer lastId = null; // null для первой записи в истории


    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        Integer newTaskId = task.getId();

        // если задача уже есть в истории её нужно удалить
        if (history.containsKey(newTaskId)) {
            remove(newTaskId);
        }

        // создаем новый узел для истории
        Node node = new Node(task, history.get(lastId));

        // добавляем задачу в конец истории
        linkLast(node);
    }

    @Override
    public void remove(int taskIdToRemove) {

        // получаем удаляемый узел
        Node nodeToRemove = history.get(taskIdToRemove);

        if (nodeToRemove == null) {
            return;
        }

        // линкуем соседние узлы, узел с удаляемой задачей удаляется из связанного списка
        removeNode(nodeToRemove);

        // удаляем задачу из истории
        history.remove(taskIdToRemove);

        // проверяем, а не последний ли удаляется, если да удаляем указатели
        if (history.isEmpty()) {
            firstId = null;
            lastId = null;
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void clearHistory() {
        history.clear();
        firstId = null;
        lastId = null;
    }

    private void removeNode(Node nodeToRemove) {

        // получаем его предыдущий и следующий узлы
        Node nextNode = nodeToRemove.getNext();
        Node prevNode = nodeToRemove.getPrev();

        // связываем узлы до и после удаляемого
        if (prevNode != null) {
            prevNode.setNext(nextNode);

            // проверяем не нужно изменить указатель на конец
            if (nodeToRemove.getData().getId() == lastId) {
                lastId = prevNode.getData().getId();
            }

        }
        if (nextNode != null) {
            nextNode.setPrev(prevNode);
            // проверяем не нужно изменить указатель на начало
            if (nodeToRemove.getData().getId() == firstId) {
                firstId = nextNode.getData().getId();
            }
        }

    }

    private ArrayList<Task> getTasks() {

        ArrayList<Task> result = new ArrayList<>();

        // получаем первый в истории узел
        Node current = history.get(firstId);

        // пока есть next обходим всю историю
        while (current != null) {

            // добавляем задачу текущего узла в результат
            result.add(current.getData());

            // теперь следующий становится текущим
            current = current.getNext();
        }

        return result;
    }

    void linkLast(Node newNode) {

        // цепляем новый узел к предыдущему, если он уже есть
        if (lastId != null) {
            Node prev = history.get(lastId);

            prev.setNext(newNode);
        }

        // добавляем новый узел
        Integer newId = newNode.getData().getId();
        history.put(newId, newNode);

        // если это первый узел, то сохраняем его
        if (firstId == null) {
            firstId = newId;
        }

        lastId = newId;
    }
}

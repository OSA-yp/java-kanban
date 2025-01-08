package managers;

import tasks.Task;

public class Node {
    // TODO Класс для связанного списка,
    private Task data;
    private Node next;
    private Node prev;

    public Node(Task data, Node prev) {
        this.data = data;
        this.prev = prev;
    }

    public Task getData() {
        return data;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}

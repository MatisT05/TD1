package collisions.llist;

public class NELList<T> extends LList<T> {
    private final T head;
    private final LList<T> tail;
    private final int size;

    public NELList(T head, LList<T> tail) {
        this.head = head;
        this.tail = tail;
        this.size = 1 + tail.size();
    }

    public boolean isEmpty() {
        return false;
    }

    public T head() {
        return head;
    }

    public LList<T> tail() {
        return tail;
    }

    public int size() {
        return size;
    }
}

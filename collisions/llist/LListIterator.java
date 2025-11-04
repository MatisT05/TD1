package collisions.llist;

import java.util.Iterator;

public class LListIterator<T> implements Iterator<T> {
    private LList<T> current;

    public LListIterator(LList<T> list) {
        this.current = list;
    }

    @Override
    public boolean hasNext() {
        return current.nonEmpty();
    }

    @Override
    public T next() {
        T object = current.head();
        current = current.tail();
        return object;
    }
}

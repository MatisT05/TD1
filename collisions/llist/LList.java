package collisions.llist;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class LList<T> implements Iterable<T> {


    public static <T> LList<T> empty() {
        //noinspection unchecked
        return (LList<T>) EmptyLList.singleton;
    }

    public abstract T head();

    public abstract LList<T> tail();

    public abstract int size();

    public abstract boolean isEmpty();

    public boolean nonEmpty() {
        return !isEmpty();
    }

    public LList<T> prepend(T elt) {
        return new NELList<T>(elt, this);
    }

    public Stream<T> stream() {
        return StreamSupport.stream(Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED | Spliterator.IMMUTABLE), false);
    }

    public String toString() {
        return stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public Iterator<T> iterator() {
        return new LListIterator<>(this);
    }
}

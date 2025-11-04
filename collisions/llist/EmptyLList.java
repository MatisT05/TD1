package collisions.llist;

import javax.lang.model.type.NullType;
import java.util.EmptyStackException;

public class EmptyLList extends LList<NullType> {

    protected static final LList<?> singleton = new EmptyLList();

    @Override
    public LList<NullType> tail() {
        throw new EmptyStackException();
    }

    @Override
    public NullType head() {
        throw new EmptyStackException();
    }

    public boolean isEmpty() {
        return true;
    }

    public int size() {
        return 0;
    }
}

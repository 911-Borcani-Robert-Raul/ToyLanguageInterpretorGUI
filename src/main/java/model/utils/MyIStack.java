package model.utils;

import java.util.List;

public interface MyIStack<T> {
    public void push(T second);
    public T top() throws ADTException;

    boolean isEmpty();

    T pop() throws ADTException;

    public String getReversedString();

    public List<String> getReversedElements();
}

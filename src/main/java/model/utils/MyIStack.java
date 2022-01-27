package model.utils;

import java.util.List;

public interface MyIStack<T> {
    void push(T second);
    T top() throws ADTException;

    boolean isEmpty();

    T pop() throws ADTException;

    String getReversedString();

    List<String> getReversedElements();
}

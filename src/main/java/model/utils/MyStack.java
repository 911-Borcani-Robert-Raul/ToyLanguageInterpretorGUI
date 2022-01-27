package model.utils;

import model.utils.MyIStack;

import java.util.*;

public class MyStack<T> implements MyIStack<T> {
    Stack<T> stack; //a field whose type CollectionType is an appropriate // generic java library collection

    public MyStack() {
        stack = new Stack<>();
    }

    @Override
    public void push(T element) {
        stack.push(element);
    }

    @Override
    public T top() throws ADTException {
        if (stack.isEmpty())
            throw new ADTException("The stack is empty!");

        return stack.peek();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public T pop() throws ADTException {
        if (stack.isEmpty())
            throw new ADTException("The stack is empty, you can't pop elements!");

        return stack.pop();
    }

    @Override
    public String toString() {
        return stack.toString();
    }

    @Override
    public List<String> getReversedElements() {
        List<T> l= Arrays.asList((T[]) stack.toArray());
        Collections.reverse(l);

        ArrayList<String> result = new ArrayList<>();
        for (T currentElement : l) {
            result.add(currentElement.toString());
        }

        return result;
    }

    @Override
    public String getReversedString() {
        List<T> l= Arrays.asList((T[]) stack.toArray());
        Collections.reverse(l);

        String result = "";
        int idx = 0;
        for (T currentElement : l) {
            result += currentElement.toString();
            idx += 1;
            if (idx < l.size())
                result += '\n';
        }

        return result;
    }
}

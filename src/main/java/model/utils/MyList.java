package model.utils;


import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements MyIList<T> {
    List<T> list;

    public MyList() {
        list = new ArrayList<>();
    }

    @Override
    public void add(T eval) {
        list.add(eval);
    }

    @Override
    public List<T> getItems() {
        return list;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}

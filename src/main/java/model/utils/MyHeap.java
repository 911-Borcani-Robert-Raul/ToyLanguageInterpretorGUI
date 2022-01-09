package model.utils;

import model.values.Value;

public class MyHeap extends MyDictionary<Integer, Value> {
    public int getNewFreeAddress() {
        int address = 1;
        while (dictionary.containsKey(address))
            address += 1;

        return address;
    }

    public Value get(Integer address) {
        return dictionary.get(address);
    }
}

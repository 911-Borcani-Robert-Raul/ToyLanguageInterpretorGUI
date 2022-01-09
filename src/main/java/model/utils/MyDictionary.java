package model.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyDictionary<K, V> implements MyIDictionary<K, V> {
    HashMap<K, V> dictionary;

    public MyDictionary() {
        dictionary = new HashMap<>();
    }

    @Override
    public boolean isDefined(K id) {
        return dictionary.get(id) != null;
    }

    @Override
    public V lookup(K id) throws ADTException {
        if (!isDefined(id))
            throw new ADTException("key not defined!");

        return dictionary.get(id);
    }

    @Override
    public void update(K id, V val) {
        dictionary.put(id, val);
    }

    @Override
    public void delete(K id) {
        dictionary.remove(id);
    }

    @Override
    public Arrays entrySet() {
        return entrySet();
    }

    @Override
    public void setContent(HashMap<K, V> content) {
        dictionary = content;
    }

    @Override
    public Map<K, V> getContent() {
        return dictionary;
    }

    @Override
    public String toString() {
        return dictionary.toString();
    }

    @Override
    public Set<K> keys() {
        return dictionary.keySet();
    }

    @Override
    public MyIDictionary<K, V> deepCopy() {
        MyDictionary<K, V> result = new MyDictionary<K, V>();
        for (K key : dictionary.keySet()) {
            result.update(key, dictionary.get(key));
        }

        return result;
    }

}

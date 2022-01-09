package model.utils;

import java.lang.reflect.AnnotatedType;
import java.util.*;

public interface MyIDictionary<K, V> {
    boolean isDefined(K id);

    V lookup(K id) throws ADTException;

    void update(K id, V val);

    void delete(K id);

    Arrays entrySet();

    void setContent(HashMap<K, V> content);

    Map<K, V> getContent();

    Set<K> keys();

    MyIDictionary<K, V> deepCopy();
}

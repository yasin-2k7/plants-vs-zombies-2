package com.pvz2.models.pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class GenericObjectPool<T extends Resettable> {
    private final List<T> freeObjects = new ArrayList<>();
    private final Set<T> inPoolSet = new HashSet<>();
    private final Supplier<T> factory;


    public GenericObjectPool(Supplier<T> factory) {
        this.factory = factory;
    }

    public T acquire() {
        if (freeObjects.isEmpty()) {
            return factory.get();
        } else {
            T obj = freeObjects.removeLast();
            inPoolSet.remove(obj);
            return obj;
        }
    }

    public void release(T obj) {
        if (obj == null) {
            return;
        }

        if (inPoolSet.contains(obj)) {
            return;
        }

        freeObjects.add(obj);
        inPoolSet.add(obj);
    }

    public void clear() {
        freeObjects.clear();
        inPoolSet.clear();
    }
}

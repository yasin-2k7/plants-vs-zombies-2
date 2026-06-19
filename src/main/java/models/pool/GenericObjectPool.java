package models.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GenericObjectPool < T extends Resettable> {
    private final List<T> freeObjects = new ArrayList<>();
    private final Supplier<T> factory;


    public GenericObjectPool(Supplier<T> factory) {
        this.factory = factory;
    }

    public T acquire(){
        if (freeObjects.isEmpty()){
            return factory.get();
        }
        else{
            return freeObjects.removeLast();
        }
    }

    public void release(T obj){
        freeObjects.add(obj);
    }
}

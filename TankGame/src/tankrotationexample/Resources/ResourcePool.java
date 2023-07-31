package tankrotationexample.Resources;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourcePool <T> {
    private final List<T> pool;
    private final String resourceType;

    public ResourcePool (String resourceType, int size){
        this.pool = Collections.synchronizedList(new ArrayList<>(size));
        this.resourceType = resourceType;
    }

    public boolean fillPool (Class<T> type, int size){
        try{
            for (int i = 0; i < size; i++){
                this.pool.add(
                    type.getDeclaredConstructor(Float.TYPE, Float.TYPE, BufferedImage.class)
                            .newInstance(0,0,ResourceManager.getSprite(this.resourceType))

                );
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public T getResource(){
        return this.pool.remove(this.pool.size()-1);
    }

    public boolean returnResource(T obj){
        return this.pool.add(obj);
    }

}

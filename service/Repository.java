package service;
import model.annotation.Id;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
public class Repository<T> {
    private final HashMap<String, T> data = new HashMap<>();
    public T save(T entity) {
        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    data.put(String.valueOf(field.get(entity)), entity);
                    return entity;
                }
            }
            Class<?> parent = entity.getClass().getSuperclass();
            while (parent != null) {
                for (Field field : parent.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        field.setAccessible(true);
                        data.put(String.valueOf(field.get(entity)), entity);
                        return entity;
                    }
                }
                parent = parent.getSuperclass();
            }
            throw new IllegalArgumentException("No @Id field found");
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Unable to read @Id field");
        }
    }
    @SafeVarargs
    public final void saveAll(T... items) { for (T item : items) save(item); }
    public T findById(String id) { return data.get(id); }
    public ArrayList<T> findAll() { return new ArrayList<>(data.values()); }
    public boolean delete(String id) { return data.remove(id) != null; }
    public int size() { return data.size(); }
}

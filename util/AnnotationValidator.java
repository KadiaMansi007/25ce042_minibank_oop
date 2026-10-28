package util;
import model.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
public class AnnotationValidator {
    public static String[] validate(Object obj) {
        ArrayList<String> errors = new ArrayList<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (field.isAnnotationPresent(Positive.class) && value instanceof Number n && n.longValue() <= 0) {
                    errors.add(field.getName() + ": " + field.getAnnotation(Positive.class).message());
                }
                if (field.isAnnotationPresent(MaxLength.class) && value instanceof String s &&
                    s.length() > field.getAnnotation(MaxLength.class).value()) {
                    errors.add(field.getName() + ": maximum length is " + field.getAnnotation(MaxLength.class).value());
                }
            } catch (IllegalAccessException e) {
                errors.add("Could not validate " + field.getName());
            }
        }
        return errors.toArray(new String[0]);
    }
}

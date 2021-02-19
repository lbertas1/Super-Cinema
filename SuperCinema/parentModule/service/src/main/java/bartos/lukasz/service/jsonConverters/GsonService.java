package bartos.lukasz.service.jsonConverters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Service;

@Service
public class GsonService <T> {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public T getObjectFromJson(String item, Class<T> typeClass) {
        return gson.fromJson(item, typeClass);
    }

    public String toJson(T item) {
        return gson.toJson(item);
    }
}

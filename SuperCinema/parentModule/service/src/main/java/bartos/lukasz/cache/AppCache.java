package bartos.lukasz.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppCache {
    private Map<String, Long> cache = new HashMap<>();

    public boolean hasProperty(String property) {
        return cache.get(property) != null;
    }

    public Long getProperty(String property) {
        return cache.get(property);
    }

    public String addOrUpdate(String property, Long value) {
        cache.put(property, value);
        return property;
    }

    public void removeProperty(String property) {
        cache.remove(property);
    }

    public void clearCache() {
        this.cache.clear();
    }
}

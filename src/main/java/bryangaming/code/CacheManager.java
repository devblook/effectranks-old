package bryangaming.code;


import bryangaming.code.utils.Configuration;

import java.util.HashMap;
import java.util.Map;

public class CacheManager
{
    private final Map<String, Configuration> config;
    private Manager manager;

    public CacheManager(final Manager manager) {
        this.config = new HashMap<>();
        this.manager = manager;
    }

    public Map<String, Configuration> getConfigFiles() {
        return this.config;
    }
}

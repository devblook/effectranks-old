package me.bryang.effectranks;


import me.bryang.effectranks.utils.FileManager;

import java.util.HashMap;
import java.util.Map;

public class CacheManager
{
    private final Map<String, FileManager> configFile;
    private PluginService pluginService;

    public CacheManager(PluginService pluginService) {
        this.configFile = new HashMap<>();
        this.pluginService = pluginService;
    }

    public Map<String, FileManager> getConfigFiles() {
        return this.configFile;
    }
}

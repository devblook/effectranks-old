package me.bryang.effectranks.loader;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.debug.DebugLogger;
import me.bryang.effectranks.utils.FileManager;

import java.util.Map;

public class FileLoader {
    private final PluginService pluginService;

    private FileManager configFile;
    private FileManager messagesFile;
    private FileManager players;

    public FileLoader(PluginService pluginService) {
        this.pluginService = pluginService;
        setup();
    }

    private void setup() {

        configFile = this.setConfiguration("config.yml");
        messagesFile = this.setConfiguration("messages.yml");
        players = this.setConfiguration("players.yml");

        pluginService.getPlugin().getLogger().info("config loaded!");
    }

    public FileManager setConfiguration(String string) {

        Map<String, FileManager> configFiles = pluginService.getCache().getConfigFiles();
        DebugLogger log = pluginService.getLogs();

        FileManager configFile = new FileManager(pluginService.getPlugin(), string);

        log.log(string + " loaded!");
        configFiles.put(string, configFile);

        return configFile;
    }

    public FileManager getPlayers(){
        return players;
    }

    public FileManager getConfig() {
        return configFile;
    }

    public FileManager getMessages() {
        return messagesFile;
    }

}

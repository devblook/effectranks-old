package me.bryang.effectranks.loader;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.api.Service;
import me.bryang.effectranks.debug.DebugLogger;
import me.bryang.effectranks.utils.FileManager;

import java.util.Map;

public class FileService implements Service {
    private final PluginService pluginService;

    private FileManager configFile;
    private FileManager messagesFile;
    private FileManager players;

    public FileService(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void start() {
        configFile = this.setConfiguration("config.yml");
        messagesFile = this.setConfiguration("messages.yml");
        players = this.setConfiguration("players.yml");

        pluginService.getPlugin().getLogger().info("config loaded!");
    }

    // TODO: 19/10/2022 Recreate this methods to use the new FileManager class
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

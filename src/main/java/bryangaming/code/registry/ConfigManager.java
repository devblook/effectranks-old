package bryangaming.code.registry;

import bryangaming.code.Manager;
import bryangaming.code.debug.DebugLogger;
import bryangaming.code.utils.Configuration;

import java.util.Map;

public class ConfigManager {
    private final Manager manager;

    private Configuration config;
    private Configuration messages;
    private Configuration commands;
    private Configuration players;

    public ConfigManager(Manager manager) {
        this.manager = manager;
        setup();
    }

    private void setup() {

        config = this.setConfiguration("config.yml");
        messages = this.setConfiguration("messages.yml");
        commands = this.setConfiguration("commands.yml");
        players = this.setConfiguration("players.yml");

        manager.getPlugin().getLogger().info("Config loaded!");
    }

    public Configuration setConfiguration(String string) {

        Map<String, Configuration> configFiles = manager.getCache().getConfigFiles();
        DebugLogger log = manager.getLogs();

        Configuration config = new Configuration(manager.getPlugin(), string);

        log.log(string + " loaded!");
        configFiles.put(string, config);

        return config;
    }

    public Configuration getPlayers(){
        return players;
    }
    public Configuration getConfig() {
        return config;
    }

    public Configuration getMessages() {
        return messages;
    }

    public Configuration getCommands() {
        return commands;
    }
}

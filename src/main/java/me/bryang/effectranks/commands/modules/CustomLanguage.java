package me.bryang.effectranks.commands.modules;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.utils.FileManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.bukkit.BukkitDefaultTranslationProvider;

import java.util.HashMap;
import java.util.Map;

public class CustomLanguage extends BukkitDefaultTranslationProvider {


    private final PluginService pluginService;

    protected Map<String, String> translations;

    private final FileManager messagesFile;

    public CustomLanguage(PluginService pluginService) {
        this.pluginService = pluginService;
        this.messagesFile = pluginService.getFiles().getMessages();
        translations = new HashMap<>();
        setup();
    }

    public void setup(){
        translations.put("command.subcommand.invalid", "1. The subcommand %s doesn't exists!");
        translations.put("command.no-permission", "2. No permission.");
        translations.put("argument.no-more","3. No more arguments were found, size: %s position: %s");
        translations.put("player.offline", "4. The player %s is offline!");
        translations.put("sender.unknown", "5. The sender for the command is unknown!");
        translations.put("sender.only-player", messagesFile.getString("error.console"));
        pluginService.getLogs().log("Translator created!");
    }

    public String getTranslation(String key) {
        return translations.get(key);
    }

    @Override
    public String getTranslation(Namespace namespace, String key){
        return getTranslation(key);
    }
}

package me.bryang.effectranks.commands;

import me.bryang.effectranks.EffectRanks;
import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.modules.SenderManager;
import me.bryang.effectranks.utils.FileManager;
import me.bryang.effectranks.utils.TextUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

@Command(names = "effectranks")
public class EffectRanksCommand implements CommandClass{

    private final EffectRanks plugin;

    private final PluginService pluginService;
    private final SenderManager senderManager;

    private final FileManager configFile;
    private final FileManager messagesFile;

    public EffectRanksCommand(PluginService pluginService) {
        this.plugin = pluginService.getPlugin();

        this.pluginService = pluginService;
        this.senderManager = pluginService.getPlayerMethods().getSender();

        this.configFile = pluginService.getFiles().getConfig();
        this.messagesFile = pluginService.getFiles().getMessages();

    }

    @Command(names = "")
    public boolean onCommand(@Sender Player player) {
        senderManager.sendMessage(player, messagesFile.getString("error.unknown-args")
                .replace("%usage%", TextUtils.getUsage("effectranks", "help, reload")));
        return true;
    }

    @Command(names = "help")
    public boolean helpSubCommand(@Sender Player player) {

        messagesFile.getStringList("commands.effectranks.help")
                .forEach(message -> senderManager.sendMessage(player, message));
        return true;
    }

    @Command(names = "reload")
    public boolean reloadSubCommand(@Sender Player player, @OptArg("") String args) {
        if (!player.hasPermission(configFile.getString("config.perms.reload"))) {
            senderManager.sendMessage(player, messagesFile.getString("error.no-perms"));
            return true;
        }

        if (args.isEmpty()) {
            senderManager.sendMessage(player, messagesFile.getString("error.unknown-args")
                    .replace("%usage%", TextUtils.getUsage("effectranks", "help, reload", "all, <file>")));
            return true;
        }

        if (args.equalsIgnoreCase("all")) {
            senderManager.sendMessage(player, messagesFile.getString("commands.effectranks.load"));
            reloadFiles(player, "all");
            return true;
        }

        senderManager.sendMessage(player, messagesFile.getString("commands.effectranks.load-file"));
        reloadFiles(player, args);
        return true;
    }

    public void reloadFiles(Player player, String string) {
        Map<String, FileManager> fileMap = pluginService.getCache().getConfigFiles();
        if (string.equalsIgnoreCase("all")) {
            for (final FileManager configFile : fileMap.values()) {
                configFile.reload();
            }
            senderManager.sendMessage(player, messagesFile.getString("commands.effectranks.reload"));
            return;
        }

        if (fileMap.get(string) == null) {
            senderManager.sendMessage(player, messagesFile.getString("error.unknown-args"));
            senderManager.sendMessage(player, "Files: &8[" + String.join(",", fileMap.keySet()) + "&8]");
            return;
        }

        fileMap.get(string).reload();
        senderManager.sendMessage(player, messagesFile.getString("commands.effectranks.reload-file")
                .replace("%file%", StringUtils.capitalize(string)));
    }
}


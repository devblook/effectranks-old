package bryangaming.code.commands;

import bryangaming.code.Manager;
import bryangaming.code.EffectRanks;
import bryangaming.code.modules.player.PlayerMessage;
import bryangaming.code.utils.Configuration;
import bryangaming.code.utils.StringFormat;
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

    private final Manager manager;
    private final StringFormat stringFormat;
    private final PlayerMessage playersender;

    private final Configuration config;
    private final Configuration commands;
    private final Configuration messages;

    public EffectRanksCommand(Manager manager) {
        this.plugin = manager.getPlugin();

        this.manager = manager;
        this.stringFormat = manager.getVariables();
        this.playersender = manager.getPlayerMethods().getSender();

        this.config = manager.getFiles().getConfig();
        this.commands = manager.getFiles().getCommands();
        this.messages = manager.getFiles().getMessages();

    }

    @Command(names = "")
    public boolean onCommand(@Sender Player player) {
        playersender.sendMessage(player, messages.getString("error.unknown-args")
                .replace("%usage%", stringFormat.getUsage("effectranks", "help, reload")));
        return true;
    }

    @Command(names = "help")
    public boolean helpSubCommand(@Sender Player player) {

        StringFormat variable = this.manager.getVariables();
        variable.loopString(player, commands, "commands.effectranks.help");
        return true;
    }

    @Command(names = "reload")
    public boolean reloadSubCommand(@Sender Player player, @OptArg("") String args) {

        if (!player.hasPermission(config.getString("config.perms.reload"))) {
            playersender.sendMessage(player, messages.getString("error.no-perms"));
            return true;
        }

        if (args.isEmpty()) {
            playersender.sendMessage(player, messages.getString("error.unknown-args")
                    .replace("%usage%", stringFormat.getUsage("effectranks", "help, reload", "all, <file>")));
            return true;
        }

        if (args.equalsIgnoreCase("all")) {
            playersender.sendMessage(player, commands.getString("commands.effectranks.load"));
            getReloadEvent(player, "all");
            return true;
        }

        playersender.sendMessage(player, commands.getString("commands.effectranks.load-file"));
        getReloadEvent(player, args);
        return true;
    }

    public void getReloadEvent(Player player, String string) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {

            Map<String, Configuration> fileMap = manager.getCache().getConfigFiles();

            if (string.equalsIgnoreCase("all")) {
                for (final Configuration config : fileMap.values()) {
                    config.reload();
                }

                playersender.sendMessage(player, commands.getString("commands.effectranks.reload"));
                return;
            }

            if (fileMap.get(string) == null) {
                playersender.sendMessage(player, messages.getString("error.unknown-args"));
                playersender.sendMessage(player, "Files: &8[" + String.join(",", fileMap.keySet()) + "&8]");
                return;
            }

            fileMap.get(string).reload();
            playersender.sendMessage(player, commands.getString("commands.effectranks.reload-file")
                        .replace("%file%", StringUtils.capitalize(string)));

        }, 60L);
    }
}


package bryangaming.code.utils;

import bryangaming.code.Manager;
import bryangaming.code.modules.player.PlayerMessage;
import org.bukkit.command.*;


public class VariableManager
{
    private final Manager manager;

    public VariableManager(Manager manager) {
        this.manager = manager;
    }

    public void loopString(CommandSender sender, Configuration config, String string) {
        PlayerMessage player = manager.getPlayerMethods().getSender();

        for (String msg : config.getStringList(string)) {
            player.sendMessage(sender, msg);
        }
    }

    public String replaceString(String string) {
        Configuration config = manager.getFiles().getConfig();
        return string
                .replace(config.getString("config.p-variable"), config.getString("config.prefix"))
                .replace(config.getString("config.e-variable"), config.getString("config.error"));
    }
}

package bryangaming.code.utils;

import bryangaming.code.Manager;
import bryangaming.code.modules.player.PlayerMessage;
import org.bukkit.command.*;
import org.bukkit.entity.Player;


public class StringFormat
{
    private final Manager manager;

    public StringFormat(Manager manager) {
        this.manager = manager;
    }

    public void loopString(Player player, Configuration config, String string) {
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        for (String msg : config.getStringList(string)) {
            playersender.sendMessage(player, msg);
        }
    }

    public String getUsage(String command, String... args){
        StringBuilder stringBuilder = new StringBuilder();

        for (String arg : args) {
            if (arg.contains(",")) {
                stringBuilder.append("[").append(arg).append("] ");
            }
        }


        return "/" + command + " " + stringBuilder.toString();
    }

    public String replaceString(String string) {
        Configuration config = manager.getFiles().getConfig();
        return string
                .replace("%newline%", "\n")
                .replace(config.getString("config.p-variable"), config.getString("config.prefix"))
                .replace(config.getString("config.e-variable"), config.getString("config.error"));
    }
}

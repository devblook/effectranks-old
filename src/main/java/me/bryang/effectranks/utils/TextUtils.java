package me.bryang.effectranks.utils;

import me.bryang.effectranks.PluginService;
import org.bukkit.ChatColor;

public class TextUtils {

    private static PluginService pluginService;

    public TextUtils(PluginService pluginService) {
        TextUtils.pluginService = pluginService;
    }

    public static String setColor(final String path) {
        return ChatColor.translateAlternateColorCodes('&', path);
    }

    public static String setColor(final String path, final String except) {
        return ChatColor.translateAlternateColorCodes('&', path)
                .replace("%message%", except);
    }


    public static String getUsage(String command, String... args){
        StringBuilder stringBuilder = new StringBuilder();

        for (String arg : args) {
            if (arg.contains(",")) {
                stringBuilder.append("[").append(arg).append("] ");
            }
        }


        return "/" + command + " " + stringBuilder.toString();
    }

    public static String replaceString(String string) {
        FileManager configFile = pluginService.getFiles().getConfig();
        return string
                .replace("%newline%", "\n")
                .replace(configFile.getString("config.p-variable"), configFile.getString("config.prefix"))
                .replace(configFile.getString("config.e-variable"), configFile.getString("config.error"));
    }

}

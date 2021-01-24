package bryangaming.code.modules.player;


import org.bukkit.ChatColor;

public class PlayerStatic {

    public static String setColor(final String path) {
        return ChatColor.translateAlternateColorCodes('&', path);
    }

    public static String setColor(final String path, final String except) {
        return ChatColor.translateAlternateColorCodes('&', path)
                .replace("%message%", except);
    }
}

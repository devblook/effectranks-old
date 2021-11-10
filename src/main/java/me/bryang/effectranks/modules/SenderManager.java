package me.bryang.effectranks.modules;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.utils.TextUtils;
import org.bukkit.entity.Player;
import org.w3c.dom.Text;

public class SenderManager
{
    private final PluginService pluginService;

    public SenderManager(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    public void sendMessage(Player player, String path) {
        String newMessage = path;

        newMessage = TextUtils.replaceString(newMessage);
        newMessage = TextUtils.setColor(newMessage);

        player.sendMessage(newMessage);

    }

}

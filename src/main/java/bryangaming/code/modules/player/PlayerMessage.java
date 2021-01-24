package bryangaming.code.modules.player;

import bryangaming.code.Manager;
import bryangaming.code.modules.player.PlayerStatic;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class PlayerMessage
{
    private final Manager manager;

    public PlayerMessage(Manager manager) {
        this.manager = manager;
    }

    public void sendMessage(Player player, final String path) {
        player.sendMessage(getMessage(path));
    }

    public String getMessage(String message) {
        message = this.manager.getVariables().replaceString(message);
        return PlayerStatic.setColor(message);
    }
}

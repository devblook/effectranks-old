package bryangaming.code.commands;

import bryangaming.code.Manager;
import bryangaming.code.EffectRanks;

import bryangaming.code.modules.PowerMethod;
import bryangaming.code.modules.RankMethod;
import bryangaming.code.modules.player.PlayerMessage;
import bryangaming.code.utils.Configuration;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;

import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Command(names = "power")
public class PowerCommand implements CommandClass {

    private final EffectRanks plugin;
    private final Manager manager;

    private final PlayerMessage playersender;
    private final PowerMethod powerMethod;

    private final Configuration commands;
    private final Configuration messages;

    public PowerCommand(Manager manager) {
        this.plugin = manager.getPlugin();
        this.manager = manager;

        this.playersender = manager.getPlayerMethods().getSender();
        this.powerMethod = manager.getPlayerMethods().getPowerMethod();

        this.commands = manager.getFiles().getCommands();
        this.messages = manager.getFiles().getMessages();
    }

    @Command(names = "")
    public boolean onCommand(@Sender Player player) {
        playersender.sendMessage(player, messages.getString("error.no-args"));
        playersender.sendMessage(player, "&8- &fUsage: &a/power [on/off/list]");
        return true;
    }

    @Command(names = "on")
    public boolean onOnSubCommand(@Sender Player player) {

        RankMethod rankMethod = manager.getPlayerMethods().getLoopMethod();
        UUID playeruuid = player.getUniqueId();

        if (rankMethod.playerIsInCooldown(player)){
            playersender.sendMessage(player, messages.getString("error.cooldown.wait-time")
                        .replace("%time%", rankMethod.getTextRankCooldown(player)));
            return true;
        }

        powerMethod.setPower(playeruuid);
        rankMethod.putCooldown(player, (System.currentTimeMillis() / 1000) + rankMethod.getRankCooldown(player));
        playersender.sendMessage(player, commands.getString("commands.power.status-on"));
        return true;
    }

    @Command(names = "off")
    public boolean onOffSubCommand(@Sender Player player) {

        UUID playeruuid = player.getUniqueId();

        powerMethod.unsetPower(playeruuid);
        playersender.sendMessage(player, commands.getString("commands.power.status-off"));
        return true;
    }

    @Command(names = "list")
    public boolean onListSubCommand(@Sender Player player) {

        playersender.sendMessage(player, commands.getString("commands.power.space"));
        playersender.sendMessage(player, commands.getString("commands.power.potion-list"));

        Set<String> string = new HashSet<>();

        for (PotionEffectType potioneffects : PotionEffectType.values()) {
            if (potioneffects == null) {
                continue;
            }

            string.add(potioneffects.getName());
        }

        String effectList = String.join(", ", string);

        playersender.sendMessage(player, "&8- &f" + effectList.toLowerCase() + "&8.");
        playersender.sendMessage(player, commands.getString("commands.power.space"));
        return true;
    }
}

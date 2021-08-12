package bryangaming.code.commands;

import bryangaming.code.Manager;
import bryangaming.code.EffectRanks;

import bryangaming.code.api.EffectsModify;
import bryangaming.code.api.events.EnableEffectsEvent;
import bryangaming.code.modules.CooldownMethod;
import bryangaming.code.modules.PowerMethod;
import bryangaming.code.modules.RankMethod;
import bryangaming.code.modules.convert.ConvertPotion;
import bryangaming.code.modules.player.PlayerMessage;
import bryangaming.code.utils.Configuration;
import bryangaming.code.utils.StringFormat;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;

import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Command(names = "effects")
public class PowerCommand implements CommandClass {

    private final EffectRanks plugin;
    private final Manager manager;

    private final PlayerMessage playersender;
    private final PowerMethod powerMethod;

    private final Configuration config;
    private final Configuration commands;
    private final Configuration messages;

    public PowerCommand(Manager manager) {
        this.plugin = manager.getPlugin();
        this.manager = manager;

        this.playersender = manager.getPlayerMethods().getSender();
        this.powerMethod = manager.getPlayerMethods().getPowerMethod();

        this.config = manager.getFiles().getConfig();
        this.commands = manager.getFiles().getCommands();
        this.messages = manager.getFiles().getMessages();
    }

    @Command(names = "")
    public boolean onCommand(@Sender Player player) {
        StringFormat stringFormat = manager.getVariables();

        playersender.sendMessage(player, messages.getString("error.unknown-args")
            .replace("%usage%", stringFormat.getUsage("effects", "on, off, convert, list")));
        return true;
    }

    @Command(names = "on")
    public boolean onOnSubCommand(@Sender Player player) {

        EnableEffectsEvent enableEffectsEvent = new EnableEffectsEvent(player);
        Bukkit.getPluginManager().callEvent(enableEffectsEvent);

        if (enableEffectsEvent.isCancelled()){
            return false;
        }

        RankMethod rankMethod = manager.getPlayerMethods().getLoopMethod();
        CooldownMethod cooldownMethod = manager.getPlayerMethods().getCooldownMethod();

        if (cooldownMethod.playerIsInCooldown(player)) {
            playersender.sendMessage(player, messages.getString("error.cooldown.wait-time")
                    .replace("%time%", cooldownMethod.getTextRankCooldown(player)));
            return true;
        }

        if (rankMethod.getPlayerRank(player).equalsIgnoreCase("default") && config.getConfigurationSection("default") == null) {
            playersender.sendMessage(player, messages.getString("error.effects.empty-effects"));
            return true;
        }

        powerMethod.setPower(player.getUniqueId());
        playersender.sendMessage(player, commands.getString("commands.effects.status-on"));
        cooldownMethod.putCooldown(player, (System.currentTimeMillis() / 1000) + cooldownMethod.getRankCooldown(player));
        return true;
    }

    @Command(names = "convert")
    public boolean onConvertSubCommand(@Sender Player player) {

        EnableEffectsEvent enableEffectsEvent = new EnableEffectsEvent(player);
        Bukkit.getPluginManager().callEvent(enableEffectsEvent);

        if (enableEffectsEvent.isCancelled()){
            return false;
        }

        RankMethod rankMethod = manager.getPlayerMethods().getLoopMethod();
        CooldownMethod cooldownMethod = manager.getPlayerMethods().getCooldownMethod();
        ConvertPotion convertPotion = manager.getPlayerMethods().getConvertPotion();

        if (cooldownMethod.playerIsInCooldown(player)){
            playersender.sendMessage(player, messages.getString("error.cooldown.wait-time")
                        .replace("%time%", cooldownMethod.getTextRankCooldown(player)));
            return true;
        }

        if (rankMethod.getPlayerRank(player).equalsIgnoreCase("default") && config.getConfigurationSection("default") == null){
            playersender.sendMessage(player, messages.getString("error.effects.empty-effects"));
            return true;
        }

        player.getInventory().addItem(convertPotion.convertPotion(rankMethod.getPlayerRank(player)));
        cooldownMethod.putCooldown(player, (System.currentTimeMillis() / 1000) + cooldownMethod.getRankCooldown(player));
        playersender.sendMessage(player, commands.getString("commands.effects.converted"));
        return true;
    }

    @Command(names = "off")
    public boolean onOffSubCommand(@Sender Player player) {

        UUID playeruuid = player.getUniqueId();

        powerMethod.unsetPower(playeruuid);
        playersender.sendMessage(player, commands.getString("commands.effects.status-off"));
        return true;
    }

    @Command(names = "list")
    public boolean onListSubCommand(@Sender Player player) {

        Set<String> string = new HashSet<>();

        for (PotionEffectType potioneffects : PotionEffectType.values()) {
            if (potioneffects == null) {
                continue;
            }

            string.add(potioneffects.getName());
        }

        String effectList = String.join(", ", string);

        playersender.sendMessage(player, commands.getString("commands.effects.potion-message")
                .replace("%value%", effectList.toLowerCase()));
        return true;
    }
}

package me.bryang.effectranks.commands;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.api.events.EnableEffectsEvent;
import me.bryang.effectranks.modules.CooldownManager;
import me.bryang.effectranks.modules.PowerManager;
import me.bryang.effectranks.modules.RankManager;
import me.bryang.effectranks.modules.SenderManager;
import me.bryang.effectranks.modules.convert.ConvertPotionManager;
import me.bryang.effectranks.utils.FileManager;
import me.bryang.effectranks.utils.TextUtils;
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
    
    private final PluginService pluginService;

    private final SenderManager senderManager;
    private final PowerManager powerManager;

    private final FileManager configFile;
    private final FileManager messagesFile;

    public PowerCommand(PluginService pluginService) {
        this.pluginService = pluginService;

        this.senderManager = pluginService.getPlayerMethods().getSender();
        this.powerManager = pluginService.getPlayerMethods().getPowerMethod();

        this.configFile = pluginService.getFiles().getConfig();
        this.messagesFile = pluginService.getFiles().getMessages();
    }

    @Command(names = "")
    public boolean onCommand(@Sender Player player) {

        senderManager.sendMessage(player, messagesFile.getString("error.unknown-args")
            .replace("%usage%", TextUtils.getUsage("effects", "on, off, convert, list")));
        return true;
    }

    @Command(names = "on")
    public boolean onOnSubCommand(@Sender Player player) {

        EnableEffectsEvent enableEffectsEvent = new EnableEffectsEvent(player);
        Bukkit.getPluginManager().callEvent(enableEffectsEvent);

        if (enableEffectsEvent.isCancelled()){
            return false;
        }

        RankManager rankManager = pluginService.getPlayerMethods().getLoopMethod();
        CooldownManager cooldownManager = pluginService.getPlayerMethods().getCooldownMethod();

        if (cooldownManager.playerIsInCooldown(player)) {
            senderManager.sendMessage(player, messagesFile.getString("error.cooldown.wait-time")
                    .replace("%time%", cooldownManager.getTextRankCooldown(player)));
            return true;
        }

        if (rankManager.getPlayerRank(player).equalsIgnoreCase("default") && configFile.getConfigurationSection("default") == null) {
            senderManager.sendMessage(player, messagesFile.getString("error.effects.empty-effects"));
            return true;
        }

        powerManager.setPower(player.getUniqueId());
        senderManager.sendMessage(player, messagesFile.getString("commands.effects.status-on"));
        cooldownManager.putCooldown(player, (System.currentTimeMillis() / 1000) + cooldownManager.getRankCooldown(player));
        return true;
    }

    @Command(names = "convert")
    public boolean onConvertSubCommand(@Sender Player player) {

        EnableEffectsEvent enableEffectsEvent = new EnableEffectsEvent(player);
        Bukkit.getPluginManager().callEvent(enableEffectsEvent);

        if (enableEffectsEvent.isCancelled()){
            return false;
        }

        RankManager rankManager = pluginService.getPlayerMethods().getLoopMethod();
        CooldownManager cooldownManager = pluginService.getPlayerMethods().getCooldownMethod();
        ConvertPotionManager convertPotionManager = pluginService.getPlayerMethods().getConvertPotion();

        if (cooldownManager.playerIsInCooldown(player)){
            senderManager.sendMessage(player, messagesFile.getString("error.cooldown.wait-time")
                        .replace("%time%", cooldownManager.getTextRankCooldown(player)));
            return true;
        }

        if (rankManager.getPlayerRank(player).equalsIgnoreCase("default") && configFile.getConfigurationSection("default") == null){
            senderManager.sendMessage(player, messagesFile.getString("error.effects.empty-effects"));
            return true;
        }

        player.getInventory().addItem(convertPotionManager.convertPotion(rankManager.getPlayerRank(player)));
        cooldownManager.putCooldown(player, (System.currentTimeMillis() / 1000) + cooldownManager.getRankCooldown(player));
        senderManager.sendMessage(player, messagesFile.getString("commands.effects.converted"));
        return true;
    }

    @Command(names = "off")
    public boolean onOffSubCommand(@Sender Player player) {

        UUID playeruuid = player.getUniqueId();

        powerManager.unsetPower(playeruuid);
        senderManager.sendMessage(player, messagesFile.getString("commands.effects.status-off"));
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

        senderManager.sendMessage(player, messagesFile.getString("commands.effects.potion-message")
                .replace("%value%", effectList.toLowerCase()));
        return true;
    }
}

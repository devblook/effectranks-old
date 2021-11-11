package me.bryang.effectranks.api;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.loader.FileLoader;
import me.bryang.effectranks.loader.ManagerLoader;
import me.bryang.effectranks.modules.CooldownManager;
import me.bryang.effectranks.modules.PowerManager;
import me.bryang.effectranks.modules.RankManager;
import me.bryang.effectranks.modules.SenderManager;
import me.bryang.effectranks.utils.FileManager;
import org.bukkit.entity.Player;

public class EffectsModifyImpl implements EffectsModify {

    private PluginService pluginService;

    private final ManagerLoader managerLoader;
    private FileLoader fileManager;

    public EffectsModifyImpl(PluginService pluginService){
        this.pluginService = pluginService;
        this.managerLoader = pluginService.getPlayerMethods();
        this.fileManager = pluginService.getFiles();
    }


    public void giveEffectRank(Player player){

        RankManager rankManager = managerLoader.getLoopMethod();
        CooldownManager cooldownManager = managerLoader.getCooldownMethod();
        PowerManager powerManager = managerLoader.getPowerMethod();

        SenderManager senderManager = managerLoader.getSender();

        FileManager configFile = fileManager.getConfig();
        FileManager messagesFile = fileManager.getMessages();

        if (cooldownManager.playerIsInCooldown(player)) {
            senderManager.sendMessage(player, messagesFile.getString("error.cooldown.wait-time")
                    .replace("%time%", cooldownManager.getTextRankCooldown(player)));
            return;
        }

        if (rankManager.getPlayerRank(player).equalsIgnoreCase("default") && configFile.getConfigurationSection("default") == null) {
            senderManager.sendMessage(player, messagesFile.getString("error.effects.empty-effects"));
            return;
        }

        powerManager.setPower(player.getUniqueId());
        senderManager.sendMessage(player, messagesFile.getString("messages.effects.status-on"));
        cooldownManager.putCooldown(player, (System.currentTimeMillis() / 1000) + cooldownManager.getRankCooldown(player));

    }
}
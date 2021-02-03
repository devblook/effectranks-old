package bryangaming.code.api;

import bryangaming.code.Manager;
import bryangaming.code.modules.CooldownMethod;
import bryangaming.code.modules.MethodManager;
import bryangaming.code.modules.PowerMethod;
import bryangaming.code.modules.RankMethod;
import bryangaming.code.modules.player.PlayerMessage;
import bryangaming.code.registry.ConfigManager;
import bryangaming.code.utils.Configuration;
import org.bukkit.entity.Player;

public class EffectsModifyImpl implements EffectsModify {

    private Manager manager;

    private static MethodManager methodManager;
    private static ConfigManager files;

    public EffectsModifyImpl(Manager manager){
        this.manager = manager;
        methodManager = manager.getPlayerMethods();
        files = manager.getFiles();
    }


    public static void giveEffectRank(Player player){

        RankMethod rankMethod = methodManager.getLoopMethod();
        CooldownMethod cooldownMethod = methodManager.getCooldownMethod();
        PowerMethod powerMethod = methodManager.getPowerMethod();

        PlayerMessage playersender = methodManager.getSender();

        Configuration config = files.getConfig();
        Configuration messages = files.getMessages();
        Configuration commands = files.getCommands();

        if (cooldownMethod.playerIsInCooldown(player)) {
            playersender.sendMessage(player, messages.getString("error.cooldown.wait-time")
                    .replace("%time%", cooldownMethod.getTextRankCooldown(player)));
            return;
        }

        if (rankMethod.getPlayerRank(player).equalsIgnoreCase("default") && config.getConfigurationSection("default") == null) {
            playersender.sendMessage(player, messages.getString("error.effects.empty-effects"));
            return;
        }

        powerMethod.setPower(player.getUniqueId());
        playersender.sendMessage(player, commands.getString("commands.effects.status-on"));
        cooldownMethod.putCooldown(player, (System.currentTimeMillis() / 1000) + cooldownMethod.getRankCooldown(player));

    }
}
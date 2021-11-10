package me.bryang.effectranks.listener;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.api.events.EnableEffectsEvent;
import me.bryang.effectranks.modules.CooldownManager;
import me.bryang.effectranks.modules.PowerManager;
import me.bryang.effectranks.modules.RankManager;
import me.bryang.effectranks.modules.SenderManager;
import me.bryang.effectranks.utils.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class ClickEvent implements Listener{

    private final PluginService pluginService;

    public ClickEvent(PluginService pluginService){
        this.pluginService = pluginService;
    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent event){

        FileManager configFile = pluginService.getFiles().getConfig();
        String key = configFile.getString("config.key");

        if (key == null){
            return;
        }

        if (key.equalsIgnoreCase("NONE")){
            return;
        }

        if (!key.equalsIgnoreCase("SHIFT")){
            return;
        }

        setPower(event.getPlayer());

    }

    @EventHandler
    public void onClick(PlayerInteractEvent event){

        if (event.getClickedBlock() != null){
            return;
        }

        FileManager configFile = pluginService.getFiles().getConfig();
        String key = configFile.getString("config.key");

        if (key == null){
            return;
        }

        if (key.equalsIgnoreCase("NONE")){
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR){
            if (!key.equalsIgnoreCase("RIGHT_CLICK")){
                return;
            }

            setPower(event.getPlayer());
            return;
        }
        if (event.getAction() == Action.LEFT_CLICK_AIR){
            if (!key.equalsIgnoreCase("LEFT_CLICK")){
                return;

            }
            setPower(event.getPlayer());

        }
    }

    public void setPower(Player player){

        EnableEffectsEvent enableEffectsEvent = new EnableEffectsEvent(player);
        Bukkit.getPluginManager().callEvent(enableEffectsEvent);

        if (enableEffectsEvent.isCancelled()){
            return;
        }

        CooldownManager cooldownManager = pluginService.getPlayerMethods().getCooldownMethod();

        if (cooldownManager.playerIsInCooldown(player)){
            return;
        }

        SenderManager senderManager = pluginService.getPlayerMethods().getSender();
        RankManager rankManager = pluginService.getPlayerMethods().getLoopMethod();

        FileManager configFile = pluginService.getFiles().getConfig();
        FileManager messagesFile = pluginService.getFiles().getMessages();

        if (rankManager.getPlayerRank(player).equalsIgnoreCase("default") && configFile.getConfigurationSection("default") == null){
            senderManager.sendMessage(player, messagesFile.getString("error.power.empty-effects"));
            return;
        }

        PowerManager powerManager = pluginService.getPlayerMethods().getPowerMethod();

        powerManager.setPower(player.getUniqueId());
        cooldownManager.putCooldown(player, (System.currentTimeMillis() / 1000) + cooldownManager.getRankCooldown(player));
        senderManager.sendMessage(player, messagesFile.getString("messages.effects.status-on"));

    }
}

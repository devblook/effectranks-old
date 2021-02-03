package bryangaming.code.listener;

import bryangaming.code.Manager;
import bryangaming.code.api.events.EnableEffectsEvent;
import bryangaming.code.modules.CooldownMethod;
import bryangaming.code.modules.PowerMethod;
import bryangaming.code.modules.RankMethod;
import bryangaming.code.modules.player.PlayerMessage;
import bryangaming.code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import sun.security.krb5.Config;

public class ClickEvent implements Listener{

    private Manager manager;

    public ClickEvent(Manager manager){
        this.manager = manager;
    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent event){

        Configuration config = manager.getFiles().getConfig();
        String key = config.getString("config.key");

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

        Configuration config = manager.getFiles().getConfig();
        String key = config.getString("config.key");

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

        CooldownMethod cooldownMethod = manager.getPlayerMethods().getCooldownMethod();

        if (cooldownMethod.playerIsInCooldown(player)){
            return;
        }

        PlayerMessage playersender = manager.getPlayerMethods().getSender();
        RankMethod rankMethod = manager.getPlayerMethods().getLoopMethod();

        Configuration config = manager.getFiles().getConfig();
        Configuration messages = manager.getFiles().getMessages();
        Configuration commands = manager.getFiles().getCommands();

        if (rankMethod.getPlayerRank(player).equalsIgnoreCase("default") && config.getConfigurationSection("default") == null){
            playersender.sendMessage(player, messages.getString("error.power.empty-effects"));
            return;
        }

        PowerMethod powerMethod = manager.getPlayerMethods().getPowerMethod();

        powerMethod.setPower(player.getUniqueId());
        cooldownMethod.putCooldown(player, (System.currentTimeMillis() / 1000) + cooldownMethod.getRankCooldown(player));
        playersender.sendMessage(player, commands.getString("commands.power.status-on"));

    }
}

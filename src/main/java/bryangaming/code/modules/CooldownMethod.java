package bryangaming.code.modules;

import bryangaming.code.Manager;
import bryangaming.code.modules.player.PlayerMessage;
import bryangaming.code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownMethod {

    private final Manager manager;

    private final Map<String, Long> playerDoubleMap = new ConcurrentHashMap<>();

    public CooldownMethod(Manager manager){
       this.manager = manager;
        setup();
    }

    public void setup(){

        Configuration playersFile = manager.getFiles().getPlayers();
        ConfigurationSection playersCooldown = playersFile.getConfigurationSection("players");

        if (playersCooldown == null){
            return;
        }

        if (playersCooldown.getKeys(false).isEmpty()) {
            return;
        }

        for (String players : playersCooldown.getKeys(false)){
            playerDoubleMap.put(players, playersFile.getLong("players." + players));
        }

        createCooldown();
    }

    public Map<String, Long> getPlayerCooldown(){
        return playerDoubleMap;
    }

    public void createCooldown() {
        Configuration playersFile = manager.getFiles().getPlayers();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (playerDoubleMap.isEmpty()){
                    cancel();
                }

                for (String players : playerDoubleMap.keySet()){
                    long time = System.currentTimeMillis() / 1000;

                    if (time >= playersFile.getLong("players." + players)) {
                        playerDoubleMap.remove(players);
                    }

                }
            }
        }.runTaskTimer(manager.getPlugin(), 20, 20);
    }
}

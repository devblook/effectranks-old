package me.bryang.effectranks.modules;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.EffectRanks;
import me.bryang.effectranks.utils.FileManager;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class RankManager {

    private PluginService pluginService;

    private final EffectRanks plugin;

    private final FileManager configFile;
    private final Permission permission;

    public RankManager(PluginService pluginService) {
        this.pluginService = pluginService;
        this.plugin = pluginService.getPlugin();

        this.configFile = pluginService.getFiles().getConfig();
        this.permission = pluginService.getPermission();
    }

    public Set<String> getRanks() {
        ConfigurationSection rankconfig = configFile.getConfigurationSection("groups");
        return rankconfig.getKeys(false);
    }

    public String getPlayerRank(Player player) {

        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")){
            return "default";
        }
        if (pluginService.getPermission() == null){
            return "default";
        }

        if (!pluginService.getPermission().hasGroupSupport()){
            return "default";
        }

        if (configFile.getString("config.status").equalsIgnoreCase("permission")) {
            for (String getrank : getRanks()) {
                if (player.hasPermission(getrank)){
                    return getrank;
                }
            }
            return "default";
        }

        for (String getrank : getRanks()) {
            if (permission.playerInGroup(player, getrank)) {
                return getrank;
            }
        }
        return "default";
    }


    public List<String> getRankConditions(Player player) {
        if (getPlayerRank(player).equalsIgnoreCase("default")){
            return configFile.getStringList("default.effects");
        }

        return configFile.getStringList("groups." + getPlayerRank(player) + ".effects");
    }


}

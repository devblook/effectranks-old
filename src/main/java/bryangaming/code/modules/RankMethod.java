package bryangaming.code.modules;

import bryangaming.code.Manager;
import bryangaming.code.EffectRanks;
import bryangaming.code.utils.Configuration;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class RankMethod {

    private Manager manager;

    private final EffectRanks plugin;

    private final Configuration config;
    private final Permission permission;

    public RankMethod(Manager manager) {
        this.manager = manager;
        this.plugin = manager.getPlugin();

        this.config = manager.getFiles().getConfig();
        this.permission = manager.getPermission();
    }

    public Set<String> getRanks() {
        ConfigurationSection rankconfig = config.getConfigurationSection("groups");
        return rankconfig.getKeys(false);
    }

    public String getPlayerRank(Player player) {

        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")){
            return "default";
        }
        if (manager.getPermission() == null){
            return "default";
        }

        if (!manager.getPermission().hasGroupSupport()){
            return "default";
        }

        if (config.getString("config.status").equalsIgnoreCase("permission")) {
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
            return config.getStringList("default.effects");
        }

        return config.getStringList("groups." + getPlayerRank(player) + ".effects");
    }


}

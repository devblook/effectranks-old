package me.bryang.effectranks.modules.convert;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.modules.PowerManager;
import me.bryang.effectranks.utils.FileManager;
import me.bryang.effectranks.utils.TextUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.List;

public class ConvertPotionManager {

    private final PluginService pluginService;

    public ConvertPotionManager(PluginService pluginService){
        this.pluginService = pluginService;

    }

    public ItemStack convertPotion(String rank){

        FileManager configFile = pluginService.getFiles().getConfig();

        List<String> effects;
        String rankDisplayName;

        if (rank.equalsIgnoreCase("default")){
            effects = configFile.getStringList("default.effects");
            rankDisplayName = configFile.getString("default.displayname");
        }else{
            effects = configFile.getStringList("groups." + rank + ".effects");
            rankDisplayName = configFile.getString("groups." + rank + ".displayname");
        }

        ItemStack potionItem = new ItemStack(Material.POTION, 1);
        PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();

        String displayName;
            if (rankDisplayName != null){
                displayName = configFile.getString("potion.name")
                        .replace("%rank%", rank)
                        .replace("%rank_name%", rankDisplayName);
            }else{
                displayName = configFile.getString("potion.name")
                        .replace("%rank%", rank)
                        .replace("%rank_name%", rank);
            }


        potionMeta.setDisplayName(TextUtils.setColor("&r" + displayName));

        PowerManager powerManager = pluginService.getPlayerMethods().getPowerMethod();

        List<String> lore = new ArrayList<>();

        for (String path : configFile.getStringList("potion.format")){

            if (rankDisplayName != null){
                path = path
                        .replace("%rank_name%", rankDisplayName);
            }else{
                path = path
                        .replace("%rank_name%", rank);
            }

            if (path.contains("%effect-value%")){
                for (String effect : effects) {

                    String patheffect = StringUtils.capitalize(effect.split(",")[0].trim());
                    String pathduration = effect.split(",")[1].trim();
                    String pathamplifier = effect.split(",")[2].trim();


                    path = TextUtils.setColor(path.replace("%effect-value%", "&r" + configFile.getString("potion.effect-value")
                            .replace("%name%", patheffect)
                            .replace("%duration%", pathduration)
                            .replace("%level%", pathamplifier)));
                    lore.add(path);

                    potionMeta.addCustomEffect(powerManager.getEffect(patheffect.toUpperCase(), pathduration, pathamplifier), true);
                }
                continue;
            }

            path = ChatColor.translateAlternateColorCodes('&', path
                    .replace("%rank%", rank));
            lore.add(path);

        }
        potionMeta.setLore(lore);
        potionItem.setItemMeta(potionMeta);
        return potionItem;
    }
}

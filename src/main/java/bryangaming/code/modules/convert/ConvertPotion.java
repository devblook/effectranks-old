package bryangaming.code.modules.convert;

import bryangaming.code.Manager;
import bryangaming.code.modules.PowerMethod;
import bryangaming.code.modules.player.PlayerStatic;
import bryangaming.code.utils.Configuration;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConvertPotion {

    private Manager manager;

    public ConvertPotion(Manager manager){
        this.manager = manager;

    }

    public ItemStack convertPotion(String rank){


        Configuration config = manager.getFiles().getConfig();

        List<String> effects;
        String rankDisplayName;

        if (rank.equalsIgnoreCase("default")){
            effects = config.getStringList("default.effects");
            rankDisplayName = config.getString("default.displayname");
        }else{
            effects = config.getStringList("groups." + rank + ".effects");
            rankDisplayName = config.getString("groups." + rank + ".displayname");
        }

        ItemStack potionItem = new ItemStack(Material.POTION, 1);
        PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();

        String displayName;
            if (rankDisplayName != null){
                displayName = config.getString("potion.name")
                        .replace("%rank%", rank)
                        .replace("%rank_name%", rankDisplayName);
            }else{
                displayName = config.getString("potion.name")
                        .replace("%rank%", rank)
                        .replace("%rank_name%", rank);
            }


        potionMeta.setDisplayName(PlayerStatic.setColor("&r" + displayName));

        PowerMethod powerMethod = manager.getPlayerMethods().getPowerMethod();

        List<String> lore = new ArrayList<>();

        for (String path : config.getStringList("potion.format")){

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


                    path = PlayerStatic.setColor(path.replace("%effect-value%", "&r" + config.getString("potion.effect-value")
                            .replace("%name%", patheffect)
                            .replace("%duration%", pathduration)
                            .replace("%level%", pathamplifier)));
                    lore.add(path);

                    potionMeta.addCustomEffect(powerMethod.getEffect(patheffect.toUpperCase(), pathduration, pathamplifier), true);
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

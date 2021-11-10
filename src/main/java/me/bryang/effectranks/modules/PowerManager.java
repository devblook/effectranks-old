package me.bryang.effectranks.modules;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.EffectRanks;
import me.bryang.effectranks.utils.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class PowerManager {

    private final PluginService pluginService;
    private final EffectRanks plugin;

    public PowerManager(PluginService pluginService) {
        this.pluginService = pluginService;
        this.plugin = pluginService.getPlugin();
    }

    public void setPower(UUID uuid) {

        RankManager rankManager = pluginService.getPlayerMethods().getLoopMethod();
        SenderManager senderManager = pluginService.getPlayerMethods().getSender();

        FileManager messagesFile = pluginService.getFiles().getMessages();

        Player player = Bukkit.getPlayer(uuid);

        for (String conditions : rankManager.getRankConditions(player)) {

            String patheffect = conditions.split(",")[0].trim().toUpperCase();
            String pathduration = conditions.split(",")[1].trim();
            String pathamplifier = conditions.split(",")[2].trim();

            PotionEffect effect = getEffect(patheffect, pathduration, pathamplifier);

            if (effect == null) {
                senderManager.sendMessage(player, messagesFile.getString("error.power.effect-unknown")
                        .replace("%effect", patheffect));
                return;
            }

            player.addPotionEffect(effect);
        }
    }

    public void unsetPower(final UUID uuid) {

        Player player = Bukkit.getPlayer(uuid);
        RankManager loopmethod = pluginService.getPlayerMethods().getLoopMethod();

        for (String conditions : loopmethod.getRankConditions(player)) {

            String patheffect = conditions.split(",")[0];
            PotionEffectType effecttype = PotionEffectType.getByName(patheffect);
            player.removePotionEffect(effecttype);
        }
    }

    public PotionEffect getEffect(String patheffect, String pathduration, String pathamplifier) {

        PotionEffectType effecttype = PotionEffectType.getByName(patheffect);

        if (effecttype == null) {
            return null;
        }

        return new PotionEffect(effecttype, Integer.parseInt(pathduration) * 20 + 20, Integer.parseInt(pathamplifier));
    }
}

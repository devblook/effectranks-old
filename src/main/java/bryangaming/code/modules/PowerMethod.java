package bryangaming.code.modules;

import bryangaming.code.Manager;
import bryangaming.code.EffectRanks;
import bryangaming.code.modules.player.PlayerMessage;
import bryangaming.code.utils.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class PowerMethod{

    private final Manager manager;
    private final EffectRanks plugin;

    public PowerMethod(Manager manager) {
        this.manager = manager;
        this.plugin = manager.getPlugin();
    }

    public void setPower(UUID uuid) {

        RankMethod rankMethod = manager.getPlayerMethods().getLoopMethod();
        PlayerMessage playersender = manager.getPlayerMethods().getSender();

        Configuration messages = manager.getFiles().getMessages();

        Player player = Bukkit.getPlayer(uuid);

        for (String conditions : rankMethod.getRankConditions(player)) {

            String patheffect = conditions.split(",")[0].trim().toUpperCase();
            String pathduration = conditions.split(",")[1].trim();
            String pathamplifier = conditions.split(",")[2].trim();

            PotionEffect effect = getEffect(patheffect, pathduration, pathamplifier);

            if (effect == null) {

                playersender.sendMessage(player, messages.getString("error.power.effect-unknown")
                        .replace("%effect", patheffect));
                return;
            }

            player.addPotionEffect(effect);
        }
    }

    public void unsetPower(final UUID uuid) {

        Player player = Bukkit.getPlayer(uuid);
        RankMethod loopmethod = manager.getPlayerMethods().getLoopMethod();

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

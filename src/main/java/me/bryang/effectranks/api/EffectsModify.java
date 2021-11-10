package me.bryang.effectranks.api;

import org.bukkit.entity.Player;

public interface EffectsModify {


    /**
     * Activate the effects according the rank that have the player.
     *
     * @param player The player that will activate his effects.
     *
     */

    default void giveEffectRank(Player player){

    }
}

package me.bryang.effectranks.loader;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.modules.CooldownManager;
import me.bryang.effectranks.modules.PowerManager;
import me.bryang.effectranks.modules.RankManager;
import me.bryang.effectranks.modules.convert.ConvertPotionManager;
import me.bryang.effectranks.modules.SenderManager;

public class ManagerLoader {

    private final PluginService pluginService;

    private SenderManager senderManager;
    private PowerManager powerManager;
    private RankManager loopMethod;
    private CooldownManager cooldownManager;
    private ConvertPotionManager convertPotionManager;


    public ManagerLoader(final PluginService pluginService) {
        this.pluginService = pluginService;
        setup();
    }

    public void setup(){
        senderManager = new SenderManager(pluginService);
        loopMethod = new RankManager(pluginService);
        powerManager = new PowerManager(pluginService);
        cooldownManager = new CooldownManager(pluginService);
        convertPotionManager = new ConvertPotionManager(pluginService);
    }

    public RankManager getLoopMethod() {
        return loopMethod;
    }

    public SenderManager getSender() {
        return senderManager;
    }

    public PowerManager getPowerMethod() {
        return powerManager;
    }

    public CooldownManager getCooldownMethod(){
        return cooldownManager;
    }

    public ConvertPotionManager getConvertPotion() {
        return convertPotionManager;
    }
}

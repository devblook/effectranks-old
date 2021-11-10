package me.bryang.effectranks.loader;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.listener.ClickEvent;
import org.bukkit.plugin.PluginManager;

public class ListenerLoader {

    private final PluginService pluginService;

    public ListenerLoader(PluginService pluginService){
        this.pluginService = pluginService;

        setup();
        pluginService.getPlugin().getLogger().info("Events loaded!");
    }

    public void setup(){
        PluginManager pluginManager = pluginService.getPlugin().getServer().getPluginManager();
        pluginManager.registerEvents(new ClickEvent(pluginService), pluginService.getPlugin());
    }

}

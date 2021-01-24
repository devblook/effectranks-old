package bryangaming.code.registry;

import bryangaming.code.EffectRanks;
import bryangaming.code.Manager;
import bryangaming.code.listener.ClickEvent;
import org.bukkit.plugin.PluginManager;

public class ListenersRegistry {

    private final Manager manager;

    public ListenersRegistry(Manager manager){
        this.manager = manager;

        setup();
        manager.getPlugin().getLogger().info("Events loaded!");
    }

    public void setup(){
        PluginManager pluginManager = manager.getPlugin().getServer().getPluginManager();

        pluginManager.registerEvents(new ClickEvent(manager), manager.getPlugin());
    }

}

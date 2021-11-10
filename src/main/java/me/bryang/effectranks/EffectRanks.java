package me.bryang.effectranks;

import org.bukkit.plugin.java.*;

public class EffectRanks extends JavaPlugin {


    private PluginService pluginService;

    public void onEnable() {
        registerManager();
        getLogger().info("Plugin created by " + getDescription().getAuthors() + "");
        getLogger().info("If you want support, you can join in: https://discord.gg/wpSh4Bf4Es");
        pluginService.getLogs().log("- Plugin successfull loaded.", 2);
    }

    public void registerManager() {
        pluginService = new PluginService(this);
    }

    public void onDisable() {
        getLogger().info("Thx for using this plugin <3");
    }
}

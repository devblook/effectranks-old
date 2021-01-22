package bryangaming.code;

import org.bukkit.plugin.java.*;

public class EffectRanks extends JavaPlugin {


    private Manager manager;

    public void onEnable() {
        registerManager();
        getLogger().info("Plugin created by " + getDescription().getAuthors() + "");
        getLogger().info("If you want support, you can join in: https://discord.gg/wpSh4Bf4Es");
        manager.getLogs().log("- Plugin successfull loaded.", 2);
    }

    public void registerManager() {
        manager = new Manager(this);
    }

    public void onDisable() {
        getLogger().info("Thx for using this plugin <3");
        getDisableMessage();
    }

    public void getDisableMessage() {
        int number =  (int)(Math.random() * 2.0 + 1.0);

        if (number == 1) {
            this.getLogger().info("Goodbye!");
        }
        else if (number == 2) {
            this.getLogger().info("See you later!");
        }
        else {
            this.getLogger().info("You shouldn't watch this..");
        }
    }
}

package me.bryang.effectranks;

import me.bryang.effectranks.api.EffectsModifyImpl;
import me.bryang.effectranks.debug.DebugLogger;
import me.bryang.effectranks.loader.CommandLoader;
import me.bryang.effectranks.loader.FileLoader;
import me.bryang.effectranks.loader.ListenerLoader;
import me.bryang.effectranks.loader.ManagerLoader;
import me.bryang.effectranks.utils.TextUtils;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PluginService {
    private final EffectRanks plugin;

    private Permission econ;

    private DebugLogger debug;

    private CacheManager cacheManager;
    private ManagerLoader managerLoader;

    private CommandLoader commandLoader;
    private ListenerLoader listenerLoader;

    private EffectsModifyImpl effectsModifyImpl;

    private FileLoader fileLoader;

    public PluginService(final EffectRanks plugin) {
        this.plugin = plugin;
        this.setup();
    }

    public void setup() {

        TextUtils textUtils = new TextUtils(this);
        debug = new DebugLogger(this);
        setupPermisions();

        cacheManager = new CacheManager(this);
        fileLoader = new FileLoader(this);

        managerLoader = new ManagerLoader(this);

        commandLoader = new CommandLoader(this);
        listenerLoader = new ListenerLoader(this);

        effectsModifyImpl = new EffectsModifyImpl(this);
    }

    public void setupPermisions() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            plugin.getLogger().info("Warning! If you didn't installed Vault, you can only use the default group.");
            return;
        }


        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);

        if (rsp == null) {
            return;
        }

        if (!rsp.getProvider().hasGroupSupport()) {
            plugin.getLogger().info("Warning! If you don't have a Vault implemention [LuckPerms/PermissionsEx], you can only use the default group.");
            return;
        }

        econ = rsp.getProvider();
        plugin.getLogger().info("Permissions loaded!");
    }

    public EffectRanks getPlugin() {
        return plugin;
    }

    public Permission getPermission() {
        return econ;
    }

    public DebugLogger getLogs() {
        return debug;
    }

    public FileLoader getFiles() {
        return fileLoader;
    }

    public CacheManager getCache() {
        return cacheManager;
    }


    public ManagerLoader getPlayerMethods() {
        return managerLoader;
    }

}

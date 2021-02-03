package bryangaming.code;

import bryangaming.code.api.EffectsModifyImpl;
import bryangaming.code.debug.DebugLogger;
import net.milkbowl.vault.permission.*;
import bryangaming.code.utils.*;
import bryangaming.code.modules.*;
import bryangaming.code.registry.*;
import org.bukkit.*;
import org.bukkit.plugin.*;

public class Manager {
    private final EffectRanks plugin;

    private Permission econ;

    private DebugLogger debug;

    private CacheManager cacheManager;
    private StringFormat variable;
    private MethodManager methodManager;

    private CommandsRegistry commandsRegistry;
    private ListenersRegistry listenersRegistry;

    private EffectsModifyImpl effectsModifyImpl;

    private ConfigManager configManager;

    public Manager(final EffectRanks plugin) {
        this.plugin = plugin;
        this.setup();
    }

    public void setup() {

        debug = new DebugLogger(this);
        setupPermisions();

        cacheManager = new CacheManager(this);
        configManager = new ConfigManager(this);

        variable = new StringFormat(this);

        methodManager = new MethodManager(this);

        commandsRegistry = new CommandsRegistry(this);
        listenersRegistry = new ListenersRegistry(this);

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

    public ConfigManager getFiles() {
        return configManager;
    }

    public CacheManager getCache() {
        return cacheManager;
    }

    public StringFormat getVariables() {
        return variable;
    }

    public MethodManager getPlayerMethods() {
        return methodManager;
    }

}

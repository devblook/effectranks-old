package me.bryang.effectranks.loader;

import me.bryang.effectranks.PluginService;

import me.bryang.effectranks.commands.EffectRanksCommand;
import me.bryang.effectranks.commands.PowerCommand;
import me.bryang.effectranks.commands.modules.CustomLanguage;
import me.fixeddev.commandflow.*;
import me.fixeddev.commandflow.bukkit.*;
import me.fixeddev.commandflow.annotated.part.defaults.*;
import me.fixeddev.commandflow.annotated.part.*;
import me.fixeddev.commandflow.bukkit.factory.*;
import me.fixeddev.commandflow.annotated.*;

public class CommandLoader
{
    private final PluginService pluginService;

    private AnnotatedCommandTreeBuilder builder;
    private CommandManager commandManager;

    public CommandLoader(final PluginService pluginService) {
        this.pluginService = pluginService;
        setup();
    }

    private void setup() {

        createCommandManager();
        registerCommands(new EffectRanksCommand(pluginService));
        registerCommands(new PowerCommand(pluginService));

        pluginService.getPlugin().getLogger().info("messages loaded!");
    }

    public void registerCommands(CommandClass commandClass) {
        commandManager.registerCommands(builder.fromClass(commandClass));
    }

    private void createCommandManager() {

        commandManager = new BukkitCommandManager("EffectRanks");
        commandManager.getTranslator().setProvider(new CustomLanguage(pluginService));



        PartInjector injector = PartInjector.create();

        injector.install(new DefaultsModule());
        injector.install(new BukkitModule());

        builder = new AnnotatedCommandTreeBuilderImpl(injector);
    }
}

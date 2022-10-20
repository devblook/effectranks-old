package me.bryang.effectranks.commands;

import me.bryang.effectranks.PluginService;
import me.bryang.effectranks.api.Service;
import me.bryang.effectranks.commands.modules.CustomLanguage;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;

import java.util.ArrayList;
import java.util.List;

public class CommandService implements Service {
    private final PluginService pluginService;

    private CommandManager commandManager;

    public CommandService(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    @Override
    public void start() {
        PartInjector injector = PartInjector.create();
        injector.install(new DefaultsModule());
        injector.install(new BukkitModule());

        commandManager = new BukkitCommandManager(pluginService.getPlugin().getName());
        commandManager.getTranslator().setProvider(new CustomLanguage(pluginService));
        AnnotatedCommandTreeBuilder builder = new AnnotatedCommandTreeBuilderImpl(injector);

        builder.fromClass(new EffectRanksCommand(pluginService));
        builder.fromClass(new PowerCommand(pluginService));

        pluginService.getPlugin().getLogger().info("messages loaded!");
    }

    @Override
    public void stop() {
        commandManager.unregisterAll();
    }
}

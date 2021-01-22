package bryangaming.code.registry;

import bryangaming.code.Manager;

import bryangaming.code.commands.PowerCommand;
import bryangaming.code.commands.EffectRanksCommand;
import bryangaming.code.commands.modules.CustomLanguage;
import me.fixeddev.commandflow.*;
import me.fixeddev.commandflow.bukkit.*;
import me.fixeddev.commandflow.annotated.part.defaults.*;
import me.fixeddev.commandflow.annotated.part.*;
import me.fixeddev.commandflow.bukkit.factory.*;
import me.fixeddev.commandflow.annotated.*;

public class CommandsRegistry
{
    private final Manager manager;

    private AnnotatedCommandTreeBuilder builder;
    private CommandManager commandManager;

    public CommandsRegistry(final Manager manager) {
        this.manager = manager;
        setup();
    }

    private void setup() {

        createCommandManager();
        registerCommands(new EffectRanksCommand(manager));
        registerCommands(new PowerCommand(manager));

        manager.getPlugin().getLogger().info("Commands loaded!");
    }

    public void registerCommands(CommandClass commandClass) {
        commandManager.registerCommands(builder.fromClass(commandClass));
    }

    private void createCommandManager() {

        commandManager = new BukkitCommandManager("EffectRanks");
        commandManager.getTranslator().setProvider(new CustomLanguage(manager));

        PartInjector injector = PartInjector.create();

        injector.install(new DefaultsModule());
        injector.install(new BukkitModule());

        builder = new AnnotatedCommandTreeBuilderImpl(injector);
    }
}

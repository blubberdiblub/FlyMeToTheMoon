package org.blubberdiblub.spigotmc.flymetothemoon.managers;

import org.blubberdiblub.spigotmc.flymetothemoon.EnablerDisabler;
import org.blubberdiblub.spigotmc.flymetothemoon.PluginEntryPoint;
import org.blubberdiblub.spigotmc.flymetothemoon.commands.CommandInterface;
import org.blubberdiblub.spigotmc.flymetothemoon.commands.Fly;
import org.bukkit.command.PluginCommand;

import java.util.logging.Level;
import java.util.logging.Logger;


public class CommandManager implements EnablerDisabler
{
    private final PluginEntryPoint plugin;
    private final Logger logger;

    private final CommandInterface flyCommand;

    public CommandManager(final PluginEntryPoint plugin, final Logger logger)
    {
        this.plugin = plugin;
        this.logger = logger;

        this.flyCommand = new Fly(plugin, logger);

        logger.log(Level.FINEST, "instantiated {0}", this);
    }

    @Override
    public void enable()
    {
        logger.log(Level.FINER, "registering fly command");

        PluginCommand cmd = plugin.getCommand("fly");
        flyCommand.setPluginCommand(cmd);
        flyCommand.enable();
    }

    @Override
    public void disable()
    {
        logger.log(Level.FINER, "unregistering fly command");

        flyCommand.disable();
    }
}

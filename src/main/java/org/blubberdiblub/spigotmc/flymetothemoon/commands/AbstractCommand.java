package org.blubberdiblub.spigotmc.flymetothemoon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class AbstractCommand implements CommandExecutor, CommandInterface
{
    final Logger logger;
    private @Nullable PluginCommand pluginCommand;

    AbstractCommand(final Logger logger)
    {
        super();

        this.logger = logger;
        this.pluginCommand = null;

        logger.log(Level.INFO, "instantiated {0}", this);
    }

    @Override
    protected void finalize() throws Throwable
    {
        logger.log(Level.INFO, "finalizing {0}", this);

        super.finalize();
    }

    @Override
    public void setPluginCommand(final @Nullable PluginCommand pluginCommand)
    {
        logger.log(Level.INFO, "{0}.setPluginCommand({1})",
                   new Object[]{AbstractCommand.class.getSimpleName(), pluginCommand});

        this.disable();
        this.pluginCommand = pluginCommand;
    }

    @Override
    public void enable()
    {
        logger.log(Level.INFO, "{0}.enable()", AbstractCommand.class.getSimpleName());

        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }
    }

    @Override
    public void disable()
    {
        logger.log(Level.INFO, "{0}.disable()", AbstractCommand.class.getSimpleName());

        if (pluginCommand != null) {
            pluginCommand.setExecutor((CommandSender culprit, Command command, String label, String[] args) -> {
                culprit.sendMessage("Command is disabled");
                return true;
            });
            pluginCommand = null;
        }
    }
}

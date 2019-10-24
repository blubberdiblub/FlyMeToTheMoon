package org.blubberdiblub.spigotmc.flymetothemoon.commands.fly;

import org.blubberdiblub.spigotmc.flymetothemoon.FlyMeToTheMoonPlugin;
import org.blubberdiblub.spigotmc.flymetothemoon.utils.FlyingReconcilerInterface;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Disable extends AbstractSubCommand
{
    public Disable(final @NotNull String commandName, final @NotNull FlyMeToTheMoonPlugin plugin,
                   final @NotNull Logger logger)
    {
        super(commandName, plugin, logger);
    }

    @Override
    public boolean execute(final CommandSender culprit, final Player victim, final String... args)
    {
        logger.log(Level.INFO, "{0}.execute({1}, {2}, {3})",
                   new Object[]{Disable.class.getSimpleName(), culprit, victim, args});

        final FlyingReconcilerInterface flyingReconciler = plugin.getFlyingReconciler();
        final FlyingReconcilerInterface.Result result = flyingReconciler.changeAllowFlight(victim, false);

        this.reportResult(culprit, victim, result);
        return true;
    }
}

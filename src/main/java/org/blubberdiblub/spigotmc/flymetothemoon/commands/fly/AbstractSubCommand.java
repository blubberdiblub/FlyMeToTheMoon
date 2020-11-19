package org.blubberdiblub.spigotmc.flymetothemoon.commands.fly;

import org.blubberdiblub.spigotmc.flymetothemoon.FlyMeToTheMoonPlugin;
import org.blubberdiblub.spigotmc.flymetothemoon.utils.FlyingReconcilerInterface;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class AbstractSubCommand implements SubCommandInterface
{
    private final String commandName;

    final FlyMeToTheMoonPlugin plugin;
    final Logger logger;

    AbstractSubCommand(final @NotNull String commandName, final @NotNull FlyMeToTheMoonPlugin plugin, final @NotNull Logger logger)
    {
        this.commandName = commandName.toLowerCase(Locale.ENGLISH);

        this.plugin = plugin;
        this.logger = logger;

        logger.log(Level.FINEST, "instantiated {0}", this);
    }

    @Override
    public boolean matches(String token)
    {
        logger.log(Level.FINE, "{0}.matches({1})", new Object[]{AbstractSubCommand.class.getSimpleName(), token});

        return commandName.equalsIgnoreCase(token);
    }

    void reportResult(final CommandSender culprit, final Player victim, final FlyingReconcilerInterface.Result result) {

        final String subject = (victim == culprit) ? "You" : victim.getName();
        final String dontDoesnt = (victim == culprit) ? " don't" : " doesn't";

        switch (result) {
        case ALLOWED:

            culprit.sendMessage(subject + " can now fly.");
            break;

        case DISALLOWED:

            culprit.sendMessage(subject + " can no longer fly.");
            break;

        case UNCHANGED:

            culprit.sendMessage("Nothing changed.");
            break;

        case ERROR_NO_PERMISSION:

            culprit.sendMessage(subject + dontDoesnt + " have the necessary permission.");
            break;
        }
    }
}

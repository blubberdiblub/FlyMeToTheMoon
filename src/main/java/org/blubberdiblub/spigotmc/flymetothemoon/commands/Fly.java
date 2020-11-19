package org.blubberdiblub.spigotmc.flymetothemoon.commands;

import org.blubberdiblub.spigotmc.flymetothemoon.FlyMeToTheMoonPlugin;
import org.blubberdiblub.spigotmc.flymetothemoon.commands.fly.Disable;
import org.blubberdiblub.spigotmc.flymetothemoon.commands.fly.Enable;
import org.blubberdiblub.spigotmc.flymetothemoon.commands.fly.SubCommandInterface;
import org.blubberdiblub.spigotmc.flymetothemoon.commands.fly.Toggle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Fly extends AbstractCommand
{
    private final SubCommandInterface enableSubCommand;
    private final SubCommandInterface disableSubCommand;
    private final SubCommandInterface toggleSubCommand;

    public Fly(final @NotNull FlyMeToTheMoonPlugin plugin, final @NotNull Logger logger)
    {
        super(logger);

        this.enableSubCommand = new Enable("on", plugin, logger);
        this.disableSubCommand = new Disable("off", plugin, logger);
        this.toggleSubCommand = new Toggle("toggle", plugin, logger);
    }

    @Override
    public boolean onCommand(final CommandSender culprit, final Command command, final String label,
                             final String[] args)
    {
        logger.log(Level.FINE, "{0}.onCommand()", Fly.class.getSimpleName());

        if (!(culprit instanceof Player)) {
            culprit.sendMessage("must be a player to use this command");
            return true;
        }

        final Player victim = (Player) culprit;

        if (args.length != 1) {
            return false;
        }

        final String subCommand = args[0];
        final String[] remainderArgs = Arrays.copyOfRange(args, 1, args.length);

        if (enableSubCommand.matches(subCommand)) {
            return enableSubCommand.execute(culprit, victim, remainderArgs);
        }

        if (disableSubCommand.matches(subCommand)) {
            return disableSubCommand.execute(culprit, victim, remainderArgs);
        }

        if (!toggleSubCommand.matches(subCommand)) {
            return false;
        }

        return toggleSubCommand.execute(culprit, victim, remainderArgs);
    }
}

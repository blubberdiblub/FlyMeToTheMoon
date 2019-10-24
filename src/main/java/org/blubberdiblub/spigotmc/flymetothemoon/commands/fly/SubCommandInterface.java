package org.blubberdiblub.spigotmc.flymetothemoon.commands.fly;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public interface SubCommandInterface
{
    boolean matches(String token);

    boolean execute(CommandSender culprit, Player victim, String... args);
}

package org.blubberdiblub.spigotmc.flymetothemoon.commands;

import org.blubberdiblub.spigotmc.flymetothemoon.EnablerDisabler;
import org.bukkit.command.PluginCommand;


public interface CommandInterface extends EnablerDisabler
{
    void setPluginCommand(PluginCommand pluginCommand);
}

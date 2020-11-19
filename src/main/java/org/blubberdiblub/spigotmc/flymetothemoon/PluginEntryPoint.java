package org.blubberdiblub.spigotmc.flymetothemoon;

import org.blubberdiblub.spigotmc.flymetothemoon.listeners.PlayerListener;
import org.blubberdiblub.spigotmc.flymetothemoon.managers.CommandManager;
import org.blubberdiblub.spigotmc.flymetothemoon.state.PlayerStateInterface;
import org.blubberdiblub.spigotmc.flymetothemoon.state.PlayerStateManager;
import org.blubberdiblub.spigotmc.flymetothemoon.utils.FlyingReconciler;
import org.blubberdiblub.spigotmc.flymetothemoon.utils.FlyingReconcilerInterface;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.permission.Permissions;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;


@Plugin(name = "FlyMeToTheMoon", version = "0.6")
@ApiVersion(ApiVersion.Target.v1_15)
@Author("Niels Boehm")
@Permissions({
@Permission(name = "flymetothemoon.commands.fly", defaultValue = PermissionDefault.OP,
            desc = "allows usage of the fly command"),
@Permission(name = "flymetothemoon.fly.survival", defaultValue = PermissionDefault.OP,
            desc = "allows flying in survival mode"),
@Permission(name = "flymetothemoon.fly.creative", defaultValue = PermissionDefault.OP,
            desc = "allows flying in creative mode"),
@Permission(name = "flymetothemoon.fly.adventure", defaultValue = PermissionDefault.OP,
            desc = "allows flying in adventure mode")
             })
@Commands({
                  @Command(name = "fly", permission = "flymetothemoon.commands.fly", desc = "does stuff",
                           usage = "/<command> [on|off|toggle]")
          })
public class PluginEntryPoint extends JavaPlugin implements FlyMeToTheMoonPlugin
{
    private final Logger logger = this.getLogger();

    private final PlayerStateInterface playerStateManager;
    private final FlyingReconcilerInterface flyingReconciler;
    private final EnablerDisabler playerListener;
    private final EnablerDisabler commandManager;

    public PluginEntryPoint()
    {
        super();

        playerStateManager = new PlayerStateManager(this, logger);
        flyingReconciler = new FlyingReconciler(this, logger);
        playerListener = new PlayerListener(this, logger);
        commandManager = new CommandManager(this, logger);

        logger.log(Level.FINEST, "instantiated {0}", this);
    }

    @Override
    public void onEnable()
    {
        logger.log(Level.FINER, "{0}.onEnable()", PluginEntryPoint.class.getSimpleName());

        playerStateManager.enable();
        playerListener.enable();
        commandManager.enable();
    }

    @Override
    public void onDisable()
    {
        logger.log(Level.FINER, "{0}.onDisable()", PluginEntryPoint.class.getSimpleName());

        commandManager.disable();
        playerListener.disable();
        playerStateManager.disable();
    }

    @Override
    public @NotNull PlayerStateInterface getPlayerStateManager()
    {
        return playerStateManager;
    }

    @Override
    public @NotNull FlyingReconcilerInterface getFlyingReconciler()
    {
        return flyingReconciler;
    }

    @Override
    @NonNls
    public @NotNull String flyingPermissionName(final @NotNull GameMode gameMode)
    {
        return "flymetothemoon.fly." + gameMode.name().toLowerCase();
    }

    @Override
    public boolean hasPermissionToFly(final @NotNull Player player, final @NotNull GameMode gameMode)
    {
        return (gameMode == GameMode.SPECTATOR) || player.hasPermission(this.flyingPermissionName(gameMode));
    }
}

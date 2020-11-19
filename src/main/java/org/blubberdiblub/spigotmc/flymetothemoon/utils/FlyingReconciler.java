package org.blubberdiblub.spigotmc.flymetothemoon.utils;

import org.blubberdiblub.spigotmc.flymetothemoon.FlyMeToTheMoonPlugin;
import org.blubberdiblub.spigotmc.flymetothemoon.state.PlayerStateInterface;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;


public class FlyingReconciler implements FlyingReconcilerInterface
{
    private final FlyMeToTheMoonPlugin plugin;
    private final Logger logger;

    public FlyingReconciler(final FlyMeToTheMoonPlugin plugin, final Logger logger)
    {
        this.plugin = plugin;
        this.logger = logger;

        logger.log(Level.FINEST, "instantiated {0}", this);
    }

    @Override
    public Result changeEnableFlight(final Player player, final boolean enableFlight)
    {
        logger.log(Level.FINE, "{0}.changeEnableFlight({1}, {2})",
                   new Object[]{FlyingReconciler.class.getSimpleName(), player, enableFlight});

        return this.reconcile(player, enableFlight, null, null);
    }

    @Override
    public Result changeEnableFlight(final Player player, final boolean enableFlight, final boolean from,
                                     final boolean makeFlying)
    {
        logger.log(Level.FINE, "{0}.changeEnableFlight({1}, {2}, {3}, {4})",
                   new Object[]{FlyingReconciler.class.getSimpleName(), player, enableFlight, from, makeFlying});

        return this.reconcile(player, enableFlight, from, makeFlying);
    }

    private Result reconcile(final Player player, final boolean enableFlight, final Boolean from,
                             final Boolean makeFlying)
    {
        final @NotNull PlayerStateInterface playerStateManager = plugin.getPlayerStateManager();
        final boolean previousAllowedFlight = (from != null) ? from : player.getAllowFlight();
        final @NotNull GameMode gameMode = player.getGameMode();
        final boolean isFlying;

        if (gameMode != GameMode.SPECTATOR) {
            if (!enableFlight) {
                player.setAllowFlight(false);
                playerStateManager.setBoth(player, false, false);
                return previousAllowedFlight ? Result.DISALLOWED : Result.UNCHANGED;
            }

            if (!plugin.hasPermissionToFly(player, gameMode)) {
                player.setAllowFlight(false);
                playerStateManager.setBoth(player, false, false);
                return Result.ERROR_NO_PERMISSION;
            }

            isFlying = (makeFlying != null) ? makeFlying : player.isFlying();
        } else {
            isFlying = true;
        }

        player.setAllowFlight(true);
        player.setFlying(isFlying);
        playerStateManager.setBoth(player, true, isFlying);
        return previousAllowedFlight ? Result.UNCHANGED : Result.ALLOWED;
    }
}

package org.blubberdiblub.spigotmc.flymetothemoon.listeners;

import org.blubberdiblub.spigotmc.flymetothemoon.FlyMeToTheMoonPlugin;
import org.blubberdiblub.spigotmc.flymetothemoon.state.PlayerStateInterface;
import org.blubberdiblub.spigotmc.flymetothemoon.utils.FlyingReconcilerInterface;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;


public class PlayerListener implements EventListener
{
    private final FlyMeToTheMoonPlugin plugin;
    private final Logger logger;

    public PlayerListener(final FlyMeToTheMoonPlugin plugin, final Logger logger)
    {
        this.plugin = plugin;
        this.logger = logger;

        logger.log(Level.INFO, "instantiated {0}", this);
    }

    @Override
    protected void finalize() throws Throwable
    {
        logger.log(Level.INFO, "finalizing {0}", this);

        super.finalize();
    }

    @Override
    public void enable()
    {
        logger.log(Level.INFO, "registering PlayerManager events");

        final Server server = plugin.getServer();
        final PluginManager pm = server.getPluginManager();
        pm.registerEvents(this, plugin);
    }

    @Override
    public void disable()
    {
        logger.log(Level.INFO, "unregistering PlayerManager events");

        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent event)
    {
        final World source = event.getFrom();
        final Player player = event.getPlayer();
        final boolean allowFlight = player.getAllowFlight();
        final GameMode gameMode = player.getGameMode();
        final World target = player.getWorld();

        logger.log(Level.INFO, "{0} {3} {4} changed from {1} to {2}",
                   new Object[]{player, source, target, allowFlight, gameMode});
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void verifyFlyingPermission(final @NotNull PlayerToggleFlightEvent event)
    {
        final @NotNull Player player = event.getPlayer();
        final @NotNull GameMode gameMode = player.getGameMode();

        if (plugin.hasPermissionToFly(player, gameMode)) {
            return;
        }

        player.setAllowFlight(false);
        final PlayerStateInterface playerStateManager = plugin.getPlayerStateManager();
        playerStateManager.setAllowFlight(player, false);

        if (!event.isFlying()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerToggleFlight(final @NotNull PlayerToggleFlightEvent event)
    {
        final boolean isFlying = event.isFlying();
        final boolean cancelled = event.isCancelled();
        final @NotNull Player player = event.getPlayer();

        logger.log(Level.INFO, "{0} toggled flying to {1} and {2}", new Object[]{
                player, isFlying,
                cancelled ? "was cancelled" : "succeeded"
        });

        final @NotNull PlayerStateInterface playerStateManager = plugin.getPlayerStateManager();
        playerStateManager.setFlying(player, isFlying);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(final @NotNull PlayerQuitEvent event)
    {
        final @NotNull Player player = event.getPlayer();
        final @NotNull World world = player.getWorld();

        logger.log(Level.INFO, "{0} left {1}", new Object[]{player, world});

        final PlayerStateInterface playerStateManager = plugin.getPlayerStateManager();
        playerStateManager.setBoth(player, player.getAllowFlight(), player.isFlying());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();
        final GameMode gameMode = player.getGameMode();
        final World world = player.getWorld();

        logger.log(Level.INFO, "{0} ({2}/{3}) {4} joined {1}",
                   new Object[]{player, world, player.getAllowFlight(), player.isFlying(), player.getGameMode()});

        final PlayerStateInterface playerStateManager = plugin.getPlayerStateManager();
        final boolean allowFlight = player.getAllowFlight() || playerStateManager.getAllowFlight(player);
        final boolean isFlying = player.isFlying() || playerStateManager.isFlying(player);

        this.setFlyingStateDelayed(player, allowFlight || isFlying, isFlying);
        if (!isFlying) {
            return;
        }

        if (!plugin.hasPermissionToFly(player, gameMode)) {
            player.addAttachment(plugin, plugin.flyingPermissionName(gameMode), true, 1);
        }

        final GameMode worldGameMode = plugin.getServer().getDefaultGameMode();
        if ((worldGameMode != gameMode) && !plugin.hasPermissionToFly(player, worldGameMode)) {
            player.addAttachment(plugin, plugin.flyingPermissionName(worldGameMode), true, 2);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerGameModeChange(final PlayerGameModeChangeEvent event)
    {
        final Player player = event.getPlayer();
        final boolean allowFlight = player.getAllowFlight();
        final boolean isFlying = player.isFlying();
        final GameMode source = player.getGameMode();
        final GameMode target = event.getNewGameMode();

        logger.log(Level.INFO, "{0}.onPlayerGameModeChange() {1} ({2}/{3}) {4} -> {5}",
                   new Object[]{PlayerListener.class.getSimpleName(), player, allowFlight, isFlying, source, target});

        this.setFlyingStateDelayed(player, allowFlight, isFlying);
    }

    private void setFlyingStateDelayed(final Player player, final boolean allowFlight, final boolean isFlying)
    {
        final FlyingReconcilerInterface flyingReconciler = plugin.getFlyingReconciler();
        final BukkitRunnable tickDelayedGameModeSetter = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                final GameMode gameMode = player.getGameMode();
                logger.log(Level.INFO, "{0}.run() {1} {2} {3}",
                           new Object[]{BukkitRunnable.class.getSimpleName(), player, gameMode, allowFlight});

                final FlyingReconcilerInterface.Result result =
                        flyingReconciler.changeAllowFlight(player, allowFlight || player.getAllowFlight(),
                                                           allowFlight, isFlying || player.isFlying());

                switch (result) {
                case ERROR_NO_PERMISSION:
                    player.sendMessage("You don't have permission to fly in this game mode.");
                    break;

                case DISALLOWED:
                    player.sendMessage("You can no longer fly.");
                    break;

                case ALLOWED:
                    player.sendMessage("You can now fly.");
                    break;

                case UNCHANGED:
                    // no message
                    break;
                }
            }
        };
        tickDelayedGameModeSetter.runTask(plugin);
    }
}

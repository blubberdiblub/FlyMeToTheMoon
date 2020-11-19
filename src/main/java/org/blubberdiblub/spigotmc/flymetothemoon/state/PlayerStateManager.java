package org.blubberdiblub.spigotmc.flymetothemoon.state;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PlayerStateManager implements PlayerStateInterface
{
    private final Logger logger;

    private Map<UUID, PlayerState> playerStates = new HashMap<>(20);

    public PlayerStateManager(final Logger logger) {
        this.logger = logger;

        logger.log(Level.FINEST, "instantiated {0}", this);
    }

    @Override
    public void enable()
    {
        logger.log(Level.FINER, "{0}.enable()", PlayerStateManager.class.getSimpleName());
    }

    @Override
    public void disable()
    {
        logger.log(Level.FINER, "{0}.disable()", PlayerStateManager.class.getSimpleName());
    }

    @Override
    public void setName(final @NotNull Player player, final @NotNull String name)
    {
        logger.log(Level.FINE, "{0}.setName()", PlayerStateManager.class.getSimpleName());

        final UUID uuid = player.getUniqueId();
        final @Nullable PlayerState playerState = playerStates.get(uuid);

        if (playerState == null) {
            playerStates.put(uuid, new PlayerState(name, player.getAllowFlight(), player.isFlying()));
            return;
        }

        playerState.setName(name);
    }

    @Override
    public boolean getAllowFlight(final @NotNull Player player)
    {
        logger.log(Level.FINE, "{0}.getAllowFlight()", PlayerStateManager.class.getSimpleName());

        final UUID uuid = player.getUniqueId();
        final @Nullable PlayerState playerState = playerStates.get(uuid);

        return (playerState != null) ? playerState.getAllowFlight() : player.getAllowFlight();
    }

    @Override
    public void setAllowFlight(final @NotNull Player player, final boolean allowFlight)
    {
        logger.log(Level.FINE, "{0}.setAllowFlight()", PlayerStateManager.class.getSimpleName());

        final UUID uuid = player.getUniqueId();
        final @Nullable PlayerState playerState = playerStates.get(uuid);

        if (playerState == null) {
            playerStates.put(uuid, new PlayerState(player.getName(), allowFlight, player.isFlying()));
            return;
        }

        playerState.setAllowFlight(allowFlight);
    }

    @Override
    public boolean isFlying(final @NotNull Player player)
    {
        logger.log(Level.FINE, "{0}.isFlying()", PlayerStateManager.class.getSimpleName());

        final UUID uuid = player.getUniqueId();
        final @Nullable PlayerState playerState = playerStates.get(uuid);

        return (playerState != null) ? playerState.isFlying() : player.isFlying();
    }

    @Override
    public void setFlying(final @NotNull Player player, final boolean isFlying)
    {
        logger.log(Level.FINE, "{0}.setFlying()", PlayerStateManager.class.getSimpleName());

        final UUID uuid = player.getUniqueId();
        final @Nullable PlayerState playerState = playerStates.get(uuid);

        if (playerState == null) {
            playerStates.put(uuid, new PlayerState(player.getName(), player.getAllowFlight(), isFlying));
            return;
        }

        playerState.setFlying(isFlying);
    }

    @Override
    public void setBoth(final @NotNull Player player, final boolean allowFlight, final boolean isFlying)
    {
        logger.log(Level.FINE, "{0}.setBoth()", PlayerStateManager.class.getSimpleName());

        final UUID uuid = player.getUniqueId();
        final @Nullable PlayerState playerState = playerStates.get(uuid);

        if (playerState == null) {
            playerStates.put(uuid, new PlayerState(player.getName(), allowFlight, isFlying));
            return;
        }

        playerState.setAllowFlight(allowFlight);
        playerState.setFlying(isFlying);
    }
}

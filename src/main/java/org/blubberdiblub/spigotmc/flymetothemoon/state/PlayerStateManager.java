package org.blubberdiblub.spigotmc.flymetothemoon.state;

import org.blubberdiblub.spigotmc.flymetothemoon.FlyMeToTheMoonPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
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
    private final NamespacedKey enableFlyingKey;

    private final Map<UUID, PlayerState> playerStates = new HashMap<>(20);

    private static final byte FALSE = 0;
    private static final byte TRUE = 1;

    public PlayerStateManager(final FlyMeToTheMoonPlugin plugin, final Logger logger) {
        this.logger = logger;
        this.enableFlyingKey = new NamespacedKey(plugin, "flying.enable");

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
    public boolean getEnableFlight(final @NotNull Player player)
    {
        logger.log(Level.FINE, "{0}.getEnableFlight()", PlayerStateManager.class.getSimpleName());

        final UUID uuid = player.getUniqueId();
        final @Nullable PlayerState playerState = playerStates.get(uuid);

        if (playerState != null) {
            return playerState.getEnableFlight();
        }

        if (player.getAllowFlight()) {
            return true;
        }

        final PersistentDataContainer container = player.getPersistentDataContainer();
        return container.getOrDefault(enableFlyingKey, PersistentDataType.BYTE, FALSE) != FALSE;
    }

    @Override
    public void setEnableFlight(final @NotNull Player player, final boolean enableFlight)
    {
        logger.log(Level.FINE, "{0}.setEnableFlight()", PlayerStateManager.class.getSimpleName());

        final UUID uuid = player.getUniqueId();
        final @Nullable PlayerState playerState = playerStates.get(uuid);

        player.getPersistentDataContainer().set(enableFlyingKey, PersistentDataType.BYTE, (enableFlight) ? TRUE : FALSE);

        if (playerState == null) {
            playerStates.put(uuid, new PlayerState(player.getName(), enableFlight, player.isFlying()));
            return;
        }

        playerState.setEnableFlight(enableFlight);
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
    public void setBoth(final @NotNull Player player, final boolean enableFlight, final boolean isFlying)
    {
        logger.log(Level.FINE, "{0}.setBoth()", PlayerStateManager.class.getSimpleName());

        final UUID uuid = player.getUniqueId();
        final @Nullable PlayerState playerState = playerStates.get(uuid);

        player.getPersistentDataContainer().set(enableFlyingKey, PersistentDataType.BYTE, (enableFlight) ? TRUE : FALSE);

        if (playerState == null) {
            playerStates.put(uuid, new PlayerState(player.getName(), enableFlight, isFlying));
            return;
        }

        playerState.setEnableFlight(enableFlight);
        playerState.setFlying(isFlying);
    }
}

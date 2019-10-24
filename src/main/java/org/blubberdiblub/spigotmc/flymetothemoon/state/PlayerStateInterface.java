package org.blubberdiblub.spigotmc.flymetothemoon.state;

import org.blubberdiblub.spigotmc.flymetothemoon.EnablerDisabler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public interface PlayerStateInterface extends EnablerDisabler
{
    void setName(@NotNull Player player, @NotNull String name);

    boolean getAllowFlight(@NotNull Player player);

    void setAllowFlight(@NotNull Player player, boolean enabled);

    boolean isFlying(@NotNull Player player);

    void setFlying(@NotNull Player player, boolean isFlying);

    void setBoth(@NotNull Player player, boolean allowFlight, boolean isFlying);
}

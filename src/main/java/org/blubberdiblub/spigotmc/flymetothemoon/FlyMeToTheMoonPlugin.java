package org.blubberdiblub.spigotmc.flymetothemoon;

import org.blubberdiblub.spigotmc.flymetothemoon.state.PlayerStateInterface;
import org.blubberdiblub.spigotmc.flymetothemoon.utils.FlyingReconcilerInterface;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public interface FlyMeToTheMoonPlugin extends Plugin
{
    @NotNull PlayerStateInterface getPlayerStateManager();

    @NotNull FlyingReconcilerInterface getFlyingReconciler();

    @NonNls
    @NotNull String flyingPermissionName(final @NotNull GameMode gameMode);

    boolean hasPermissionToFly(final @NotNull Player player, final @NotNull GameMode gameMode);
}

package org.blubberdiblub.spigotmc.flymetothemoon.utils;

import org.bukkit.entity.Player;


public interface FlyingReconcilerInterface
{
    Result changeEnableFlight(Player player, final boolean enableFlight);
    Result changeEnableFlight(Player player, final boolean enableFlight, final boolean from, final boolean makeFlying);

    enum Result
    {
        ALLOWED,
        DISALLOWED,
        UNCHANGED,
        ERROR_NO_PERMISSION
    }
}

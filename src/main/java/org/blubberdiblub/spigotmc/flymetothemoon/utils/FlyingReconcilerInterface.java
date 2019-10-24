package org.blubberdiblub.spigotmc.flymetothemoon.utils;

import org.bukkit.entity.Player;


public interface FlyingReconcilerInterface
{
    Result changeAllowFlight(Player player, final boolean allowFlight);
    Result changeAllowFlight(Player player, final boolean allowFlight, final boolean from, final boolean makeFlying);

    public enum Result
    {
        ALLOWED,
        DISALLOWED,
        UNCHANGED,
        ERROR_NO_PERMISSION
    }
}

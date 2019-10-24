package org.blubberdiblub.spigotmc.flymetothemoon.state;

import org.jetbrains.annotations.Nullable;


class PlayerState
{
    private @Nullable String name;
    private boolean allowFlight;
    private boolean isFlying;

    PlayerState(final @Nullable String name, final boolean allowFlight, final boolean isFlying)
    {
        this.name = name;
        this.allowFlight = allowFlight;
        this.isFlying = isFlying;
    }

    void setName(final @Nullable String name)
    {
        this.name = name;
    }

    void setAllowFlight(final boolean allowFlight)
    {
        this.allowFlight = allowFlight;
    }

    void setFlying(final boolean isFlying)
    {
        this.isFlying = isFlying;
    }

    @Nullable String getName()
    {
        return name;
    }

    boolean getAllowFlight()
    {
        return allowFlight;
    }

    boolean isFlying()
    {
        return isFlying;
    }
}

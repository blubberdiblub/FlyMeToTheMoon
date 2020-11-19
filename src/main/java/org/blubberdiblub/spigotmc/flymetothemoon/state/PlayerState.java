package org.blubberdiblub.spigotmc.flymetothemoon.state;

import org.jetbrains.annotations.Nullable;


class PlayerState
{
    private @Nullable String name;
    private boolean enableFlight;
    private boolean isFlying;

    PlayerState(final @Nullable String name, final boolean enableFlight, final boolean isFlying)
    {
        this.name = name;
        this.enableFlight = enableFlight;
        this.isFlying = isFlying;
    }

    void setName(final @Nullable String name)
    {
        this.name = name;
    }

    void setEnableFlight(final boolean enableFlight)
    {
        this.enableFlight = enableFlight;
    }

    void setFlying(final boolean isFlying)
    {
        this.isFlying = isFlying;
    }

    @Nullable String getName()
    {
        return name;
    }

    boolean getEnableFlight()
    {
        return enableFlight;
    }

    boolean isFlying()
    {
        return isFlying;
    }
}

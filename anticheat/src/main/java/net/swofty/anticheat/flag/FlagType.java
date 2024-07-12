package net.swofty.anticheat.flag;

import lombok.Getter;
import net.swofty.anticheat.flag.flags.OnGroundSpoofFlag;
import net.swofty.anticheat.flag.flags.TimeoutPingPacketsFlag;

import java.util.function.Supplier;

public enum FlagType {
    ON_GROUND_SPOOF(OnGroundSpoofFlag::new),
    TIMEOUT_PING_PACKETS(TimeoutPingPacketsFlag::new),
    ;

    @Getter
    private final Supplier<Flag> flagSupplier;

    FlagType(Supplier<Flag> flagSupplier) {
        this.flagSupplier = flagSupplier;
    }
}

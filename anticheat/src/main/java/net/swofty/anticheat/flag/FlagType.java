package net.swofty.anticheat.flag;

import lombok.Getter;
import net.swofty.anticheat.flag.flags.*;

import java.util.function.Supplier;

public enum FlagType {
    // Prediction-based checks (highest precision)
    PREDICTION(PredictionFlag::new),

    // Movement checks
    ON_GROUND_SPOOF(OnGroundSpoofFlag::new),
    SPEED(SpeedFlag::new),
    FLIGHT(FlightFlag::new),
    TIMER(TimerFlag::new),
    PHASE(PhaseFlag::new),
    JESUS(JesusFlag::new),

    // Combat checks
    REACH(ReachFlag::new),
    VELOCITY(VelocityFlag::new),
    AUTOCLICKER(AutoClickerFlag::new),
    AIM(AimFlag::new),
    KILLAURA(KillAuraFlag::new),

    // Packet checks
    TIMEOUT_PING_PACKETS(TimeoutPingPacketsFlag::new),
    BADPACKETS(BadPacketsFlag::new),
    ;

    @Getter
    private final Supplier<Flag> flagSupplier;

    FlagType(Supplier<Flag> flagSupplier) {
        this.flagSupplier = flagSupplier;
    }
}

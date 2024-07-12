package net.swofty.anticheat.flag;

import lombok.Getter;
import lombok.Setter;
import net.swofty.anticheat.event.AntiCheatListener;

@Getter
@Setter
public abstract class Flag extends AntiCheatListener {
    private final long timestamp;

    private double certainty = 0;

    public Flag() {
        this.timestamp = System.currentTimeMillis();
    }
}

package net.swofty.anticheat.engine;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PingRequest {
    private final int pingId;
    private final long initiatedTime;
    private long time;

    public PingRequest(int pingId) {
        this.pingId = pingId;
        this.time = -1;
        this.initiatedTime = System.currentTimeMillis();
    }
}

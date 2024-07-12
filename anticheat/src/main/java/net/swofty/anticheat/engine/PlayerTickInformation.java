package net.swofty.anticheat.engine;

import lombok.Getter;
import lombok.Setter;
import net.swofty.anticheat.math.Pos;
import net.swofty.anticheat.math.Vel;

@Getter
public class PlayerTickInformation {
    private final Pos pos;
    private final Vel vel;
    private final boolean onGround;

    @Setter
    private long ping;
    private PlayerTickInformation next;
    private PlayerTickInformation previous;

    public PlayerTickInformation(Pos pos, Vel vel, boolean onGround) {
        this.pos = pos;
        this.vel = vel;
        this.onGround = onGround;
    }

    public void updateContext(PlayerTickInformation next, PlayerTickInformation previous) {
        this.next = next;
        this.previous = previous;
    }
}

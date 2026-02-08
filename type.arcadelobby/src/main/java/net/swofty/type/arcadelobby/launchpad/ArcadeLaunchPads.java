package net.swofty.type.arcadelobby.launchpad;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.lobby.launchpad.LaunchPad;
import net.swofty.type.lobby.launchpad.LaunchPadHandler;

import java.util.List;
import java.util.function.Consumer;

@Getter
public enum ArcadeLaunchPads implements LaunchPad {
    SPAWN(LaunchPadHandler.getSlimeBlocksNear(new Pos(-14, 66, 0)),
            new Pos(1.5, 62, 0.5),
            (player) -> {},
            "");
    ;

    private final List<Pos> slimeBlocks;
    private final Pos destination;
    private final Consumer<HypixelPlayer> afterFinished;
    private final String rejectionMessage;

    ArcadeLaunchPads(List<Pos> slimeBlocks, Pos destination, Consumer<HypixelPlayer> afterFinished, String rejectionMessage) {
        this.slimeBlocks = slimeBlocks;
        this.destination = destination;
        this.afterFinished = afterFinished;
        this.rejectionMessage = rejectionMessage;
    }
}

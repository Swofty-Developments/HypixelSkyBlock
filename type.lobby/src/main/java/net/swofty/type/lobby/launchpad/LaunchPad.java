package net.swofty.type.lobby.launchpad;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;
import java.util.function.Consumer;

public interface LaunchPad {
    List<Pos> getSlimeBlocks();

    Pos getDestination();

    Consumer<HypixelPlayer> getAfterFinished();

    String getRejectionMessage();
}

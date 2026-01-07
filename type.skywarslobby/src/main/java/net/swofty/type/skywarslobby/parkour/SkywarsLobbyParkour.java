package net.swofty.type.skywarslobby.parkour;

import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointParkourData;
import net.swofty.type.lobby.parkour.Parkour;

import java.util.List;

public class SkywarsLobbyParkour implements Parkour {

    @Override
    public DatapointParkourData.ParkourType getId() {
        return DatapointParkourData.ParkourType.SKYWARS_LOBBY;
    }

    @Override
    public Pos getStartLocation() {
        return new Pos(-23, 63, -24);
    }

    @Override
    public List<Point> getCheckpoints() {
        return List.of(
                new BlockVec(-23, 63, -24),   // Start
                new BlockVec(73, 111, -38),   // Checkpoint 1
                new BlockVec(40, 98, 44),     // Checkpoint 2
                new BlockVec(-8, 85, 66),     // Checkpoint 3
                new BlockVec(-58, 88, -9),    // Checkpoint 4
                new BlockVec(-40, 65, -31)    // End
        );
    }
}

package net.swofty.type.mainlobby.parkour;

import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointParkourData;
import net.swofty.type.lobby.parkour.Parkour;

import java.util.List;

public class MainLobbyParkour implements Parkour {

    @Override
    public DatapointParkourData.ParkourType getId() {
        return DatapointParkourData.ParkourType.MAIN_LOBBY;
    }

    @Override
    public Pos getStartLocation() {
        return new Pos(-79.5, 90, -32.5, 90, 0);
    }

    @Override
    public List<Point> getCheckpoints() {
        return List.of(
            new BlockVec(-82, 90, -3),
            new BlockVec(-188, 79, -39),
            new BlockVec(-161, 89, -30),
            new BlockVec(-143, 102, -10),
            new BlockVec(-135, 111, -9),
            new BlockVec(-86, 119, 0)
        );
    }
}

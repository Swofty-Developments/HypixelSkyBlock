package net.swofty.type.prototypelobby.parkour;

import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointParkourData;
import net.swofty.type.lobby.parkour.Parkour;

import java.util.List;

public class PrototypeLobbyParkour implements Parkour {

	@Override
	public DatapointParkourData.ParkourType getId() {
		return DatapointParkourData.ParkourType.PROTOTYPE_LOBBY;
	}

	@Override
	public Pos getStartLocation() {
		return new Pos(2.5, 77, 42.5, -135, 0);
	}

	@Override
	public List<Point> getCheckpoints() {
		return List.of(
				new BlockVec(3, 77, 41),
				new BlockVec(40, 91, -10),
				new BlockVec(-38, 94, -25),
				new BlockVec(-7, 81, 38)
		);
	}
}

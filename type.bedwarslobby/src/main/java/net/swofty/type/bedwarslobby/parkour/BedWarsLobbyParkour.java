package net.swofty.type.bedwarslobby.parkour;

import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointParkourData;
import net.swofty.type.lobby.parkour.Parkour;

import java.util.List;

public class BedWarsLobbyParkour implements Parkour {

	@Override
	public DatapointParkourData.ParkourType getId() {
		return DatapointParkourData.ParkourType.BED_WARS_LOBBY;
	}

	@Override
	public Pos getStartLocation() {
		return new Pos(-32.5, 72, -25.5, 135, 0);
	}

	@Override
	public List<Point> getCheckpoints() {
		return List.of(
				new BlockVec(-34, 72, -27),
				new BlockVec(-36, 97, -113),
				new BlockVec(95, 92, -111),
				new BlockVec(144, 98, 2),
				new BlockVec(88, 99, 106),
				new BlockVec(45, 92, 117),
				new BlockVec(-70, 75, 118),
				new BlockVec(-41, 72, 38)
		);
	}
}

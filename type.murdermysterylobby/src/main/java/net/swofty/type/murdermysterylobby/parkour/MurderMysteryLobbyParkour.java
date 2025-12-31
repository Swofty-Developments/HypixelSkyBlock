package net.swofty.type.murdermysterylobby.parkour;

import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointParkourData;
import net.swofty.type.lobby.parkour.Parkour;

import java.util.List;

public class MurderMysteryLobbyParkour implements Parkour {

	@Override
	public DatapointParkourData.ParkourType getId() {
		return DatapointParkourData.ParkourType.MURDER_MYSTERY_LOBBY;
	}

	@Override
	public Pos getStartLocation() {
		return new Pos(-4, 68, -17);
	}

	@Override
	public List<Point> getCheckpoints() {
		return List.of(
				new BlockVec(-4, 68, -17),   // Start
				new BlockVec(28, 78, -37),   // Checkpoint 1
				new BlockVec(72, 41, -35),   // Checkpoint 2
				new BlockVec(50, 51, 32),    // Checkpoint 3
				new BlockVec(43, 74, 17)     // End
		);
	}
}

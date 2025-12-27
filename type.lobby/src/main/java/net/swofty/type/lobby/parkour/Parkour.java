package net.swofty.type.lobby.parkour;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointParkourData;

import java.util.List;

public interface Parkour {
	DatapointParkourData.ParkourType getId();
	Pos getStartLocation();
	List<Point> getCheckpoints();
}

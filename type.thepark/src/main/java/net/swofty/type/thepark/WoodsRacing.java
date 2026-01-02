package net.swofty.type.thepark;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.data.datapoints.DatapointMapStringLong;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.spruce.race.*;
import net.swofty.type.skyblockgeneric.race.Race;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class WoodsRacing implements Race {

	private final Pos start = new Pos(-362, 89, 42);

	@Override
	public String getId() {
		return "woods_racing";
	}

	@Override
	public String getTitle() {
		return "§a§lWOODS RACING";
	}

	@Override
	public Point startPosition() {
		return start;
	}

	@Override
	public Point endPosition() {
		return start;
	}

	@Override
	public List<Point> getCheckpoints() {
		return List.of(new Pos(-417, 128, -111));
	}

	@Override
	public void onFinish(SkyBlockPlayer player, long time, boolean isRecord) {
		player.sendMessage(getTitle() + " §eRace finished in " + String.format("%02d:%05.2f", time / 60000, (time % 60000) / 1000.0) + "!" + (isRecord ? " §dNew record!" : ""));

		// this behavior could be moved to the missions with the ActionPlayerFinishRace event
		int sec = (int) (time / 1000);
		MissionData data = player.getMissionData();
		if (data.isCurrentlyActive(MissionCompleteTheRaceTwoMinutes.class)) {
			if (sec <= 120) {
				data.endMission(MissionCompleteTheRaceTwoMinutes.class);
			}
		}
		if (data.isCurrentlyActive(MissionCompleteTheRaceOneMinute.class)) {
			if (sec <= 60) {
				data.endMission(MissionCompleteTheRaceOneMinute.class);
			}
		}
		if (data.isCurrentlyActive(MissionCompleteTheRaceThird.class)) {
			if (sec <= 32) {
				data.endMission(MissionCompleteTheRaceThird.class);
			}
		}
		if (data.isCurrentlyActive(MissionCompleteTheRaceFourth.class)) {
			if (sec <= 18) {
				data.endMission(MissionCompleteTheRaceFourth.class);
			}
		}
	}

	@Override
	public void onCheckpoint(SkyBlockPlayer player, int checkpointIndex, long time) {
		player.sendMessage(getTitle() + " §aYou reached the other end! Now go back to the start!");
	}

	@Override
	public int timeLimit() {
		return 180;
	}

	@Override
	public boolean canStart(SkyBlockPlayer player) {
		return player.getMissionData().hasCompleted(MissionTalkToGustave.class);
	}
}

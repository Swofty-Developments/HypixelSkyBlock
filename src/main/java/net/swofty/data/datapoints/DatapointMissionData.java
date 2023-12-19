package net.swofty.data.datapoints;

import net.swofty.data.Datapoint;
import net.swofty.mission.MissionData;
import net.swofty.serializer.JacksonSerializer;
import net.swofty.serializer.MissionDataSerializer;
import net.swofty.user.categories.Rank;

public class DatapointMissionData extends Datapoint<MissionData> {
    private static final MissionDataSerializer serializer = new MissionDataSerializer();

    public DatapointMissionData(String key, MissionData value) {
        super(key, value, serializer);
    }

    public DatapointMissionData(String key) {
        super(key, null, serializer);
    }
}
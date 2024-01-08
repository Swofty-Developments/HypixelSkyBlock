package net.swofty.types.generic.data.datapoints;

import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.serializer.MissionDataSerializer;

public class DatapointMissionData extends Datapoint<MissionData> {
    private static final MissionDataSerializer serializer = new MissionDataSerializer();

    public DatapointMissionData(String key, MissionData value) {
        super(key, value, serializer);
    }

    public DatapointMissionData(String key) {
        super(key, null, serializer);
    }
}
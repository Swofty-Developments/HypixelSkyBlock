package net.swofty.commons.skyblock.data.datapoints;

import net.swofty.commons.skyblock.data.Datapoint;
import net.swofty.commons.skyblock.serializer.MissionDataSerializer;
import net.swofty.commons.skyblock.mission.MissionData;

public class DatapointMissionData extends Datapoint<MissionData> {
    private static final MissionDataSerializer serializer = new MissionDataSerializer();

    public DatapointMissionData(String key, MissionData value) {
        super(key, value, serializer);
    }

    public DatapointMissionData(String key) {
        super(key, null, serializer);
    }
}
package net.swofty.type.skyblockgeneric.data.datapoints;

import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import org.json.JSONObject;

import java.util.Map;

public class DatapointMissionData extends SkyBlockDatapoint<MissionData> {
    private static final Serializer<MissionData> serializer = new Serializer<>() {
        @Override
        public String serialize(MissionData value) {
            return new JSONObject(value.serialize()).toString();
        }

        @Override
        public MissionData deserialize(String json) {
            MissionData missionData = new MissionData();
            Map<String, Object> map = new JSONObject(json).toMap();
            missionData.deserialize(map);
            return missionData;
        }

        @Override
        public MissionData clone(MissionData value) {
            MissionData missionData = new MissionData();
            missionData.deserialize(value.serialize());
            return missionData;
        }
    };

    public DatapointMissionData(String key, MissionData value) {
        super(key, value, serializer);
    }

    public DatapointMissionData(String key) {
        super(key, null, serializer);
    }
}
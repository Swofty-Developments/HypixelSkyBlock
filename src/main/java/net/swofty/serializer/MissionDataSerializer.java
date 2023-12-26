package net.swofty.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.swofty.mission.MissionData;

import java.util.Map;

public class MissionDataSerializer implements Serializer<MissionData> {
    private final ObjectMapper mapper;

    public MissionDataSerializer() {
        mapper = new ObjectMapper();
    }

    @Override
    public String serialize(MissionData value) throws JsonProcessingException {
        return mapper.writeValueAsString(value.serialize());
    }

    @Override
    public MissionData deserialize(String json) throws JsonProcessingException {
        JsonNode node = mapper.readTree(json);
        Map<String, Object> map = mapper.convertValue(node, Map.class);

        // Create a new MissionData instance and deserialize the JSON into it.
        MissionData missionData = new MissionData();
        missionData.deserialize(map);

        return missionData;
    }
}
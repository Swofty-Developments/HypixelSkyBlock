package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.mission.MissionData;

import java.util.Map;

public class MissionDataSerializer implements Serializer<MissionData> {
    private final ObjectMapper mapper;

    public MissionDataSerializer() {
        mapper = new ObjectMapper();
    }

    @SneakyThrows
    @Override
    public String serialize(MissionData value) {
        return mapper.writeValueAsString(value.serialize());
    }

    @SneakyThrows
    @Override
    public MissionData deserialize(String json) {
        JsonNode node = mapper.readTree(json);
        Map<String, Object> map = mapper.convertValue(node, Map.class);

        // Create a new MissionData instance and deserialize the JSON into it.
        MissionData missionData = new MissionData();
        missionData.deserialize(map);

        return missionData;
    }

    @Override
    public MissionData clone(MissionData value) {
        return value;
    }
}
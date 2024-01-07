package net.swofty.commons.skyblock.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.swofty.commons.skyblock.user.PlayerShopData;

import java.util.Map;

public class PlayerShopDataSerializer implements Serializer<PlayerShopData> {
    private final ObjectMapper mapper;

    public PlayerShopDataSerializer() {
        mapper = new ObjectMapper();
    }

    @Override
    public String serialize(PlayerShopData value) throws JsonProcessingException {
        return mapper.writeValueAsString(value.serialize());
    }

    @Override
    public PlayerShopData deserialize(String json) throws JsonProcessingException {
        JsonNode node = mapper.readTree(json);
        Map<String, Object> map = mapper.convertValue(node, Map.class);

        PlayerShopData shopData = new PlayerShopData();
        shopData.deserialize(map);

        return shopData;
    }
}

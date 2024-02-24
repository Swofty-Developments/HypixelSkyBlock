package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.user.PlayerShopData;

import java.util.Map;

public class PlayerShopDataSerializer implements Serializer<PlayerShopData> {
    private final ObjectMapper mapper;

    public PlayerShopDataSerializer() {
        mapper = new ObjectMapper();
    }

    @SneakyThrows
    @Override
    public String serialize(PlayerShopData value) {
        return mapper.writeValueAsString(value.serialize());
    }

    @SneakyThrows
    @Override
    public PlayerShopData deserialize(String json) {
        JsonNode node = mapper.readTree(json);
        Map<String, Object> map = mapper.convertValue(node, Map.class);

        PlayerShopData shopData = new PlayerShopData();
        shopData.deserialize(map);

        return shopData;
    }

    @Override
    public PlayerShopData clone(PlayerShopData value) {
        return value;
    }
}

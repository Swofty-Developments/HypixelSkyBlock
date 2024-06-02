package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import net.minestom.server.item.Material;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.datapoints.DatapointPetData;
import net.swofty.types.generic.item.SkyBlockItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserPetDataSerializer implements Serializer<DatapointPetData.UserPetData> {
    private final ObjectMapper mapper;

    public UserPetDataSerializer() {
        this.mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION);

        SimpleModule module = new SimpleModule();
        module.addSerializer(SkyBlockItem.class, new SkyBlockItemSerializer());
        module.addDeserializer(SkyBlockItem.class, new SkyBlockItemDeserializer());
        module.addKeyDeserializer(SkyBlockItem.class, new SkyBlockItemKeyDeserializer());
        module.addSerializer(Material.class, new MaterialSerializer());
        module.addDeserializer(Material.class, new MaterialDeserializer());
        mapper.registerModule(module);
    }

    @SneakyThrows
    @Override
    public String serialize(DatapointPetData.UserPetData value) {
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode petsMapNode = rootNode.putObject("petsMap");

        for (Map.Entry<SkyBlockItem, Boolean> entry : value.getPetsMap().entrySet()) {
            SkyBlockItem item = entry.getKey();
            boolean enabled = entry.getValue();

            String itemJson = mapper.writeValueAsString(item);
            petsMapNode.put(itemJson, enabled);
        }

        return mapper.writeValueAsString(rootNode);
    }

    @SneakyThrows
    @Override
    public DatapointPetData.UserPetData deserialize(String json) {
        JsonNode rootNode = mapper.readTree(json);
        JsonNode petsMapNode = rootNode.get("petsMap");

        HashMap<SkyBlockItem, Boolean> petsMap = new HashMap<>();

        if (petsMapNode != null && petsMapNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = petsMapNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String itemJson = field.getKey();
                boolean enabled = field.getValue().asBoolean();

                SkyBlockItem item = mapper.readValue(itemJson, SkyBlockItem.class);
                petsMap.put(item, enabled);
            }
        }

        return new DatapointPetData.UserPetData(petsMap);
    }

    @Override
    public DatapointPetData.UserPetData clone(DatapointPetData.UserPetData value) {
        return new DatapointPetData.UserPetData(value.getPetsMap());
    }
}
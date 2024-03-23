package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import net.minestom.server.item.Material;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.datapoints.DatapointPetData;
import net.swofty.types.generic.data.datapoints.DatapointStorage;
import net.swofty.types.generic.item.SkyBlockItem;

public class UserPetDataSerializer implements Serializer<DatapointPetData.UserPetData> {
    private final ObjectMapper mapper;

    public UserPetDataSerializer() {
        this.mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

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
        return mapper.writeValueAsString(value);
    }

    @SneakyThrows
    @Override
    public DatapointPetData.UserPetData deserialize(String json) {
        return mapper.readValue(json, DatapointPetData.UserPetData.class);
    }

    @Override
    public DatapointPetData.UserPetData clone(DatapointPetData.UserPetData value) {
        return new DatapointPetData.UserPetData(value.getPetsMap());
    }
}
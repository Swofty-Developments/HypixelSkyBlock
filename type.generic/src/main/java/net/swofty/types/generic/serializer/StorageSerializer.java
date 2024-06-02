package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import net.minestom.server.item.Material;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.datapoints.DatapointStorage;
import net.swofty.types.generic.item.SkyBlockItem;

public class StorageSerializer implements Serializer<DatapointStorage.PlayerStorage> {
    private final ObjectMapper mapper;

    public StorageSerializer() {
        this.mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        SimpleModule module = new SimpleModule();
        module.addSerializer(SkyBlockItem.class, new SkyBlockItemSerializer());
        module.addDeserializer(SkyBlockItem.class, new SkyBlockItemDeserializer());
        module.addSerializer(Material.class, new MaterialSerializer());
        module.addDeserializer(Material.class, new MaterialDeserializer());
        mapper.registerModule(module);
    }

    @SneakyThrows
    @Override
    public String serialize(DatapointStorage.PlayerStorage value) {
        return mapper.writeValueAsString(value);
    }

    @SneakyThrows
    @Override
    public DatapointStorage.PlayerStorage deserialize(String json) {
        return mapper.readValue(json, DatapointStorage.PlayerStorage.class);
    }

    @Override
    public DatapointStorage.PlayerStorage clone(DatapointStorage.PlayerStorage value) {
        return new DatapointStorage.PlayerStorage(value.slots);
    }
}
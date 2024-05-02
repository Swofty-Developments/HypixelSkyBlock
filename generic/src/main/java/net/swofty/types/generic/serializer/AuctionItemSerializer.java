package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.item.SkyBlockItem;

public class AuctionItemSerializer<T> implements Serializer<T> {
    private final ObjectMapper mapper;
    private final Class<T> clazz;

    public AuctionItemSerializer(Class<T> clazz) {
        this.mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.clazz = clazz;

        SimpleModule module = new SimpleModule();
        module.addSerializer(SkyBlockItem.class, new SkyBlockItemSerializer());
        module.addDeserializer(SkyBlockItem.class, new SkyBlockItemDeserializer());
        mapper.registerModule(module);
    }

    @SneakyThrows
    @Override
    public String serialize(T value) {
        return mapper.writeValueAsString(value);
    }

    @SneakyThrows
    @Override
    public T deserialize(String json) {
        return mapper.readValue(json, clazz);
    }

    @Override
    public T clone(T value) {
        return value;
    }
}
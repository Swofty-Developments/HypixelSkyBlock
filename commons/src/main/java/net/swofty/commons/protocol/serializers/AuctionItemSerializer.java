package net.swofty.commons.protocol.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.protocol.Serializer;

public class AuctionItemSerializer<T> implements Serializer<T> {
    private final ObjectMapper mapper;
    private final Class<T> clazz;

    public AuctionItemSerializer(Class<T> clazz) {
        this.mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.clazz = clazz;

        SimpleModule module = new SimpleModule();
        module.addSerializer(UnderstandableSkyBlockItem.class, new SkyBlockItemSerializer());
        module.addDeserializer(UnderstandableSkyBlockItem.class, new SkyBlockItemDeserializer());
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
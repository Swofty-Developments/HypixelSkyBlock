package net.swofty.commons.skyblock.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonSerializer<T> implements Serializer<T> {
    private final ObjectMapper mapper;
    private final Class<T> clazz;

    public JacksonSerializer(Class<T> clazz) {
        this.mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.clazz = clazz;
    }

    @Override
    public String serialize(T value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

    @Override
    public T deserialize(String json) throws JsonProcessingException {
        return mapper.readValue(json, clazz);
    }
}
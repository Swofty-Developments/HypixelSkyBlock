package net.swofty.service.generic;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Serializer<T> {
    String serialize(T value);

    T deserialize(String json);

    T clone(T value);
}
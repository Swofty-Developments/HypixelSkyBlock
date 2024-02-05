package net.swofty.service.generic;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface Serializer<T> {
    String serialize(T value) throws JsonProcessingException;

    T deserialize(String json) throws JsonProcessingException;

    T clone(T value);
}
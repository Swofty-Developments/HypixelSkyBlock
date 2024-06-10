package net.swofty.commons.protocol;

public interface Serializer<T> {
    String serialize(T value);

    T deserialize(String json);

    T clone(T value);
}
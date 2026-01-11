package net.swofty.type.skyblockgeneric.server.attribute;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import tools.jackson.core.JacksonException;

public abstract class ServerAttribute<T> {
    @Getter
	private final String key;
    @Setter
	@Getter
    private T value;
    protected Serializer<T> serializer;

    protected ServerAttribute(String key, T value, Serializer<T> serializer) {
        this.key = key;
        this.value = value;
        this.serializer = serializer;
    }

	public String getSerializedValue() throws JacksonException {
        return serializer.serialize(value);
    }

    public void deserializeValue(String json) {
        this.value = serializer.deserialize(json);
    }

}
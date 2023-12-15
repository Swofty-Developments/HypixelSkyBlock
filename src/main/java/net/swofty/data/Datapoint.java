package net.swofty.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.swofty.serializer.Serializer;

public abstract class Datapoint<T> {
    private DataHandler dataHandler;
    private String key;
    private T value;
    protected Serializer<T> serializer;
    private DataHandler.Data data;

    protected Datapoint(String key, T value, Serializer<T> serializer) {
        this.key = key;
        this.value = value;
        this.serializer = serializer;
    }

    public Datapoint<T> setUser(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
        return this;
    }

    public Datapoint<T> setData(DataHandler.Data data) {
        this.data = data;
        return this;
    }

    public String getKey() {
        return key;
    }

    public String getSerializedValue() throws JsonProcessingException {
        return serializer.serialize(value);
    }

    public void deserializeValue(String json) throws JsonProcessingException {
        this.value = serializer.deserialize(json);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;

        Player player = MinecraftServer.getConnectionManager().getPlayer(dataHandler.getUuid());
        if (player != null) {
            data.onChange.accept(player, this);
        }
    }
}
package net.swofty.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.swofty.serializer.Serializer;

public abstract class Datapoint<T> {
    DataHandler dataHandler;
    @Getter
    private String key;
    @Getter
    private T value;
    protected Serializer<T> serializer;
    DataHandler.Data data;

    protected Datapoint(String key, T value, Serializer<T> serializer) {
        this.key = key;
        this.value = value;
        this.serializer = serializer;
    }

    @SneakyThrows
    public Datapoint deepClone() {
        Datapoint toReturn;
        if (this.value != null) {
            toReturn = this.getClass().getConstructor(String.class, this.value.getClass()).newInstance(this.key, this.value);
        } else {
            toReturn = this.getClass().getConstructor(String.class).newInstance(this.key);
        }
        return toReturn;
    }

    public Datapoint<T> setUser(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
        return this;
    }

    public Datapoint<T> setData(DataHandler.Data data) {
        this.data = data;
        return this;
    }

    public String getSerializedValue() throws JsonProcessingException {
        return serializer.serialize(value);
    }

    public void deserializeValue(String json) throws JsonProcessingException {
        this.value = serializer.deserialize(json);
    }

    public void setValue(T value) {
        this.value = value;

        Player player = MinecraftServer.getConnectionManager().getPlayer(dataHandler.getUuid());
        if (player != null && data.onChange != null) {
            data.onChange.accept(player, this);
        }
    }
}
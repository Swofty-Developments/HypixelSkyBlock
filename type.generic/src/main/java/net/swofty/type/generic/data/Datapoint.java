package net.swofty.type.generic.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.swofty.commons.protocol.Serializer;

import java.util.Objects;

public abstract class Datapoint<T> {
    protected DataHandler dataHandler;
    @Getter private String key;
    @Getter public T value;
    @Getter protected Serializer<T> serializer;
    protected Enum<?> data;

    protected Datapoint(String key, T value, Serializer<T> serializer) {
        this.key = key;
        this.value = value;
        this.serializer = serializer;
    }

    @SneakyThrows
    public Datapoint<T> deepClone() {
        Datapoint<T> toReturn;
        if (this.value != null) {
            T clonedValue = serializer.clone(this.value);
            toReturn = this.getClass().getConstructor(String.class, this.value.getClass())
                    .newInstance(this.key, clonedValue);
        } else {
            toReturn = this.getClass().getConstructor(String.class).newInstance(this.key);
        }
        return toReturn;
    }

    public Datapoint<T> setUser(DataHandler dataHandler) { this.dataHandler = dataHandler; return this; }
    public Datapoint<T> setData(Enum<?> data) { this.data = data; return this; }

    public String getSerializedValue() throws JsonProcessingException { return serializer.serialize(value); }
    public void deserializeValue(String json) { this.value = serializer.deserialize(json); }

    /** Copy value from another datapoint without triggering onChange. */
    @SuppressWarnings("unchecked")
    public void setFrom(Datapoint<?> other) { this.value = ((Datapoint<T>) other).getValue(); }

    @SneakyThrows
    public void setValueBypassOnChange(T value) { this.value = value; }

    @SneakyThrows
    public void setValue(T value) {
        if (Objects.equals(value, this.value)) return;
        this.value = value;

        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(dataHandler.getUuid());
        if (player != null && hasOnChange()) triggerOnChange(player, this);
    }

    protected boolean hasOnChange() {
        if (data instanceof HypixelDataHandler.Data d) return d.onChange != null;
        return false;
    }

    protected void triggerOnChange(Player player, Datapoint<?> datapoint) {
        if (data instanceof HypixelDataHandler.Data d && d.onChange != null) {
            d.onChange.accept(player, datapoint);
        }
    }
}
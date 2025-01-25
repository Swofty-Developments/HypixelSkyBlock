package net.swofty.commons.protocol;

public abstract class ProtocolObject<T, R> {
    public abstract Serializer<T> getSerializer();
    public abstract Serializer<R> getReturnSerializer();

    public String translateToString(T message) {
        return getSerializer().serialize(message);
    }

    public String translateReturnToString(R message) {
        return getReturnSerializer().serialize(message);
    }

    public T translateFromString(String string) {
        return getSerializer().deserialize(string);
    }

    public R translateReturnFromString(String string) {
        return getReturnSerializer().deserialize(string);
    }

    public String channel() {
        return getClass().getSimpleName();
    }
}

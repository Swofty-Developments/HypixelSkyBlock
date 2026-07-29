package net.swofty.commons.protocol;

public abstract class RedisProtocol<T, R> {
    private final Serializer<T> serializer;
    private final Serializer<R> returnSerializer;

    protected RedisProtocol() {
        this.serializer = null;
        this.returnSerializer = null;
    }

    protected RedisProtocol(Class<T> requestType, Class<R> responseType) {
        this(new JacksonSerializer<>(requestType), new JacksonSerializer<>(responseType));
    }

    protected RedisProtocol(Serializer<T> serializer, Serializer<R> returnSerializer) {
        this.serializer = serializer;
        this.returnSerializer = returnSerializer;
    }

    public Serializer<T> getSerializer() {
        if (serializer == null) {
            throw new UnsupportedOperationException("Protocol must provide a request serializer");
        }
        return serializer;
    }

    public Serializer<R> getReturnSerializer() {
        if (returnSerializer == null) {
            throw new UnsupportedOperationException("Protocol must provide a response serializer");
        }
        return returnSerializer;
    }

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

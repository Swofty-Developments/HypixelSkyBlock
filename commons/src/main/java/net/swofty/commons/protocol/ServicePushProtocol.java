package net.swofty.commons.protocol;

public abstract class ServicePushProtocol<T, R> extends ProtocolObject<T, R> {
    private final JacksonSerializer<T> requestSerializer;
    private final JacksonSerializer<R> responseSerializer;

    protected ServicePushProtocol(Class<T> requestType, Class<R> responseType) {
        this.requestSerializer = new JacksonSerializer<>(requestType);
        this.responseSerializer = new JacksonSerializer<>(responseType);
    }

    @Override
    public Serializer<T> getSerializer() {
        return requestSerializer;
    }

    @Override
    public Serializer<R> getReturnSerializer() {
        return responseSerializer;
    }
}

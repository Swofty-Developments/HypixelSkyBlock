package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;

import java.util.List;

public class BazaarInitializeProtocolObject extends ProtocolObject<
        BazaarInitializeProtocolObject.BazaarInitializationRequest,
        BazaarInitializeProtocolObject.BazaarInitializeResponse> {

    @Override
    public Serializer<BazaarInitializationRequest> getSerializer() {
        return new Serializer<BazaarInitializationRequest>() {
            @Override
            public String serialize(BazaarInitializationRequest value) {
                return value.itemsToInitialize.toString();
            }

            @Override
            public BazaarInitializationRequest deserialize(String json) {
                return new BazaarInitializationRequest(List.of(json.split(",")));
            }

            @Override
            public BazaarInitializationRequest clone(BazaarInitializationRequest value) {
                return new BazaarInitializationRequest(value.itemsToInitialize);
            }
        };
    }

    @Override
    public Serializer<BazaarInitializeResponse> getReturnSerializer() {
        return new Serializer<BazaarInitializeResponse>() {
            @Override
            public String serialize(BazaarInitializeResponse value) {
                return "";
            }

            @Override
            public BazaarInitializeResponse deserialize(String json) {
                return new BazaarInitializeResponse();
            }

            @Override
            public BazaarInitializeResponse clone(BazaarInitializeResponse value) {
                return new BazaarInitializeResponse();
            }
        };
    }

    public record BazaarInitializationRequest(List<String> itemsToInitialize) { }

    public record BazaarInitializeResponse() { }
}

package net.swofty.commons.protocol.objects.auctions;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class AuctionFetchItemProtocol extends RedisProtocol<
        AuctionFetchItemProtocol.AuctionFetchItemMessage,
        AuctionFetchItemProtocol.AuctionFetchItemResponse> {
    private static final Serializer<AuctionFetchItemMessage> SERIALIZER =
            new JacksonSerializer<>(AuctionFetchItemMessage.class);
    private static final Serializer<AuctionFetchItemResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(AuctionFetchItemResponse.class);

    @Override
    public Serializer<AuctionFetchItemMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<AuctionFetchItemResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record AuctionFetchItemMessage(UUID uuid) {}

    public record AuctionFetchItemResponse(AuctionItem item, boolean success, @Nullable String error) {}
}

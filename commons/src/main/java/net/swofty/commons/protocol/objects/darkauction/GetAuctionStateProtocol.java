package net.swofty.commons.protocol.objects.darkauction;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.skyblock.auctions.DarkAuctionPhase;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class GetAuctionStateProtocol extends RedisProtocol<
        GetAuctionStateProtocol.GetAuctionStateMessage,
        GetAuctionStateProtocol.GetAuctionStateResponse> {

    private static final Serializer<GetAuctionStateMessage> MESSAGE_SERIALIZER =
            new JacksonSerializer<>(GetAuctionStateMessage.class);
    private static final Serializer<GetAuctionStateResponse> RESPONSE_SERIALIZER =
            new JacksonSerializer<>(GetAuctionStateResponse.class);

    @Override
    public Serializer<GetAuctionStateMessage> getSerializer() {
        return MESSAGE_SERIALIZER;
    }

    @Override
    public Serializer<GetAuctionStateResponse> getReturnSerializer() {
        return RESPONSE_SERIALIZER;
    }

    public record GetAuctionStateMessage() {}

    public record GetAuctionStateResponse(
            boolean auctionActive,
            UUID auctionId,
            DarkAuctionPhase phase,
            int currentRound,
            String currentItemType,
            long currentBid,
            UUID highestBidderId,
            String highestBidderName,
            List<String> roundItems,
            boolean success,
            @Nullable String error
    ) {}
}

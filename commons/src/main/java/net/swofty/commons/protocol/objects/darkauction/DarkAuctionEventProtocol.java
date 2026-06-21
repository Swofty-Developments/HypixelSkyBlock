package net.swofty.commons.protocol.objects.darkauction;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.commons.protocol.RedisProtocol;
import net.swofty.commons.protocol.Serializer;
import net.swofty.commons.skyblock.auctions.DarkAuctionPhase;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class DarkAuctionEventProtocol extends RedisProtocol<
        DarkAuctionEventProtocol.DarkAuctionMessage,
        DarkAuctionEventProtocol.DarkAuctionResponse> {

    public enum EventType {
        AUCTION_START,      // Spawn Sirius, open door
        AUCTION_BEGIN,      // Start bidding (after 35 sec queue)
        ROUND_START,        // New round with item
        BID_PLACED,         // Someone bid (sync across hubs)
        ROUND_END,          // Round finished, winner announced
        AUCTION_END         // Auction complete, cleanup
    }

    private static final Serializer<DarkAuctionMessage> MESSAGE_SERIALIZER =
            new JacksonSerializer<>(DarkAuctionMessage.class);
    private static final Serializer<DarkAuctionResponse> RESPONSE_SERIALIZER =
            new JacksonSerializer<>(DarkAuctionResponse.class);

    @Override
    public Serializer<DarkAuctionMessage> getSerializer() {
        return MESSAGE_SERIALIZER;
    }

    @Override
    public Serializer<DarkAuctionResponse> getReturnSerializer() {
        return RESPONSE_SERIALIZER;
    }

    public record DarkAuctionMessage(
            EventType eventType,
            UUID auctionId,
            DarkAuctionPhase phase,
            int currentRound,
            String currentItemType,
            long currentBid,
            UUID highestBidderId,
            String highestBidderName,
            int countdown,
            List<String> roundItems,
            UUID refundPlayerId,
            long refundAmount
    ) {}

    public record DarkAuctionResponse(
            boolean success,
            int playersInAuction,
            @Nullable String error
    ) {}
}

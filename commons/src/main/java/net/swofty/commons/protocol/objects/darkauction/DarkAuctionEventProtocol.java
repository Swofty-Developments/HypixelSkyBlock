package net.swofty.commons.protocol.objects.darkauction;

import net.swofty.commons.skyblock.auctions.DarkAuctionPhase;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DarkAuctionEventProtocol extends ProtocolObject<
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

    @Override
    public Serializer<DarkAuctionMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(DarkAuctionMessage value) {
                JSONObject json = new JSONObject();
                json.put("eventType", value.eventType.name());
                json.put("auctionId", value.auctionId.toString());
                json.put("phase", value.phase.name());
                json.put("currentRound", value.currentRound);
                json.put("currentItemType", value.currentItemType);
                json.put("currentBid", value.currentBid);
                json.put("highestBidderId", value.highestBidderId != null ? value.highestBidderId.toString() : null);
                json.put("highestBidderName", value.highestBidderName);
                json.put("countdown", value.countdown);

                JSONArray itemsArray = new JSONArray();
                for (String item : value.roundItems) {
                    itemsArray.put(item);
                }
                json.put("roundItems", itemsArray);

                // Refund info for BID_PLACED events
                json.put("refundPlayerId", value.refundPlayerId != null ? value.refundPlayerId.toString() : null);
                json.put("refundAmount", value.refundAmount);

                return json.toString();
            }

            @Override
            public DarkAuctionMessage deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);

                EventType eventType = EventType.valueOf(jsonObject.getString("eventType"));
                UUID auctionId = UUID.fromString(jsonObject.getString("auctionId"));
                DarkAuctionPhase phase = DarkAuctionPhase.valueOf(jsonObject.getString("phase"));
                int currentRound = jsonObject.getInt("currentRound");
                String currentItemType = jsonObject.optString("currentItemType", null);
                long currentBid = jsonObject.getLong("currentBid");

                String bidderIdStr = jsonObject.optString("highestBidderId", null);
                UUID highestBidderId = bidderIdStr != null && !bidderIdStr.equals("null") ? UUID.fromString(bidderIdStr) : null;
                String highestBidderName = jsonObject.optString("highestBidderName", null);
                int countdown = jsonObject.getInt("countdown");

                List<String> roundItems = new ArrayList<>();
                JSONArray itemsArray = jsonObject.optJSONArray("roundItems");
                if (itemsArray != null) {
                    for (int i = 0; i < itemsArray.length(); i++) {
                        roundItems.add(itemsArray.getString(i));
                    }
                }

                String refundPlayerIdStr = jsonObject.optString("refundPlayerId", null);
                UUID refundPlayerId = refundPlayerIdStr != null && !refundPlayerIdStr.equals("null") ? UUID.fromString(refundPlayerIdStr) : null;
                long refundAmount = jsonObject.optLong("refundAmount", 0);

                return new DarkAuctionMessage(eventType, auctionId, phase, currentRound, currentItemType,
                        currentBid, highestBidderId, highestBidderName, countdown, roundItems,
                        refundPlayerId, refundAmount);
            }

            @Override
            public DarkAuctionMessage clone(DarkAuctionMessage value) {
                return new DarkAuctionMessage(value.eventType, value.auctionId, value.phase, value.currentRound,
                        value.currentItemType, value.currentBid, value.highestBidderId,
                        value.highestBidderName, value.countdown, new ArrayList<>(value.roundItems),
                        value.refundPlayerId, value.refundAmount);
            }
        };
    }

    @Override
    public Serializer<DarkAuctionResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(DarkAuctionResponse value) {
                JSONObject json = new JSONObject();
                json.put("success", value.success);
                json.put("playersInAuction", value.playersInAuction);
                return json.toString();
            }

            @Override
            public DarkAuctionResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                return new DarkAuctionResponse(
                        jsonObject.getBoolean("success"),
                        jsonObject.getInt("playersInAuction")
                );
            }

            @Override
            public DarkAuctionResponse clone(DarkAuctionResponse value) {
                return new DarkAuctionResponse(value.success, value.playersInAuction);
            }
        };
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
            int playersInAuction
    ) {}
}

package net.swofty.commons.protocol.objects.darkauction;

import net.swofty.commons.skyblock.auctions.DarkAuctionPhase;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetAuctionStateProtocol extends ProtocolObject<
        GetAuctionStateProtocol.GetAuctionStateMessage,
        GetAuctionStateProtocol.GetAuctionStateResponse> {

    @Override
    public Serializer<GetAuctionStateMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetAuctionStateMessage value) {
                return "";
            }

            @Override
            public GetAuctionStateMessage deserialize(String json) {
                return new GetAuctionStateMessage();
            }

            @Override
            public GetAuctionStateMessage clone(GetAuctionStateMessage value) {
                return new GetAuctionStateMessage();
            }
        };
    }

    @Override
    public Serializer<GetAuctionStateResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(GetAuctionStateResponse value) {
                JSONObject json = new JSONObject();
                json.put("auctionActive", value.auctionActive);
                if (value.auctionActive) {
                    json.put("auctionId", value.auctionId.toString());
                    json.put("phase", value.phase.toString());
                    json.put("currentRound", value.currentRound);
                    json.put("currentItemType", value.currentItemType);
                    json.put("currentBid", value.currentBid);
                    json.put("highestBidderId", value.highestBidderId != null ? value.highestBidderId.toString() : null);
                    json.put("highestBidderName", value.highestBidderName);

                    JSONArray itemsArray = new JSONArray();
                    for (String item : value.roundItems) {
                        itemsArray.put(item);
                    }
                    json.put("roundItems", itemsArray);
                }
                return json.toString();
            }

            @Override
            public GetAuctionStateResponse deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                boolean auctionActive = jsonObject.getBoolean("auctionActive");

                if (!auctionActive) {
                    return new GetAuctionStateResponse(false, null, null, 0, null, 0, null, null, List.of());
                }

                UUID auctionId = UUID.fromString(jsonObject.getString("auctionId"));
                String phase = jsonObject.getString("phase");
                int currentRound = jsonObject.getInt("currentRound");
                String currentItemType = jsonObject.optString("currentItemType", null);
                long currentBid = jsonObject.getLong("currentBid");

                String bidderIdStr = jsonObject.optString("highestBidderId", null);
                UUID highestBidderId = bidderIdStr != null && !bidderIdStr.equals("null") ? UUID.fromString(bidderIdStr) : null;
                String highestBidderName = jsonObject.optString("highestBidderName", null);

                List<String> roundItems = new ArrayList<>();
                JSONArray itemsArray = jsonObject.optJSONArray("roundItems");
                if (itemsArray != null) {
                    for (int i = 0; i < itemsArray.length(); i++) {
                        roundItems.add(itemsArray.getString(i));
                    }
                }

                return new GetAuctionStateResponse(true, auctionId, DarkAuctionPhase.valueOf(phase), currentRound, currentItemType,
                        currentBid, highestBidderId, highestBidderName, roundItems);
            }

            @Override
            public GetAuctionStateResponse clone(GetAuctionStateResponse value) {
                return new GetAuctionStateResponse(value.auctionActive, value.auctionId, value.phase,
                        value.currentRound, value.currentItemType, value.currentBid,
                        value.highestBidderId, value.highestBidderName, new ArrayList<>(value.roundItems));
            }
        };
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
            List<String> roundItems
    ) {}
}

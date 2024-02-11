package net.swofty.service.auction.endpoints;

import net.swofty.commons.auctions.AuctionsFilter;
import net.swofty.commons.auctions.AuctionsSorting;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.auction.AuctionService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import org.bson.Document;
import org.json.JSONObject;

import java.util.List;

public class EndpointFetchItems implements ServiceEndpoint {
    @Override
    public String channel() {
        return "fetch-items";
    }

    @Override
    public JSONObject onMessage(ServiceProxyRequest message) {
        JSONObject json = new JSONObject(message.getMessage());

        AuctionsSorting sorting = AuctionsSorting.valueOf(json.getString("sorting"));
        AuctionsFilter filter = AuctionsFilter.valueOf(json.getString("filter"));
        String category = json.getString("category");

        List<Document> results = (List<Document>) AuctionService.cacheService.getAuctions(category, filter);

        if (results.isEmpty())
            return new JSONObject().put("items", new Document[0]);

        // Sort according to sorting
        switch (sorting) {
            case HIGHEST_BID:
                results.sort((o1, o2) -> {
                    int price1 = o1.getInteger("starting-price");
                    int price2 = o2.getInteger("starting-price");
                    return Integer.compare(price2, price1);
                });
                break;
            case LOWEST_BID:
                results.sort((o1, o2) -> {
                    int price1 = o1.getInteger("starting-price");
                    int price2 = o2.getInteger("starting-price");
                    return Integer.compare(price1, price2);
                });
                break;
            case ENDING_SOON:
                results.sort((o1, o2) -> {
                    long time1 = o1.getLong("end");
                    long time2 = o2.getLong("end");
                    return Long.compare(time2, time1);
                });
                break;
            case MOST_BIDS:
                results.sort((o1, o2) -> {
                    int bids1 = o1.getList("bids", String.class).size();
                    int bids2 = o2.getList("bids", String.class).size();
                    return Integer.compare(bids1, bids2);
                });
                break;
        }

        JSONObject toReturn = new JSONObject();
        // Convert all the documents to JSON and add them to an items array
        toReturn.put("items", results.stream().map(Document::toJson).toList());

        return toReturn;
    }
}

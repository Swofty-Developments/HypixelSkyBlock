package net.swofty.service.auction.endpoints;

import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.auctions.AuctionsFilter;
import net.swofty.commons.auctions.AuctionsSorting;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.service.auction.AuctionService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.types.generic.auction.AuctionItem;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EndpointFetchItems implements ServiceEndpoint {
    @Override
    public String channel() {
        return "fetch-items";
    }

    @Override
    public Map<String, Object> onMessage(ServiceProxyRequest message, Map<String, Object> messageData) {
        AuctionsSorting sorting = (AuctionsSorting) messageData.get("sorting");
        AuctionsFilter filter = (AuctionsFilter) messageData.get("filter");
        AuctionCategories category = (AuctionCategories) messageData.get("category");

        List<Document> results = (List<Document>) AuctionService.cacheService.getAuctions(category.toString(), filter);

        if (results.isEmpty()) {
            return new HashMap<>(Map.of("items", new ArrayList<>()));
        }

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
                    return Long.compare(time1, time2);
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

        HashMap<String, Object> toReturn = new HashMap<>();
        // Convert all the documents to JSON and add them to an items array
        toReturn.put("items", results.stream().map(AuctionItem::fromDocument).toList());

        return toReturn;
    }
}

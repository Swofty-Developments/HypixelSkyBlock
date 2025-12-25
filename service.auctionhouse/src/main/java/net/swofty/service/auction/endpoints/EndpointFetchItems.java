package net.swofty.service.auction.endpoints;

import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionsFilter;
import net.swofty.commons.skyblock.auctions.AuctionsSorting;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemsProtocolObject;
import net.swofty.service.auction.AuctionService;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class EndpointFetchItems implements ServiceEndpoint<
        AuctionFetchItemsProtocolObject.AuctionFetchItemsMessage,
        AuctionFetchItemsProtocolObject.AuctionFetchItemsResponse> {

    @Override
    public AuctionFetchItemsProtocolObject associatedProtocolObject() {
        return new AuctionFetchItemsProtocolObject();
    }

    @Override
    public AuctionFetchItemsProtocolObject.AuctionFetchItemsResponse onMessage(ServiceProxyRequest message, AuctionFetchItemsProtocolObject.AuctionFetchItemsMessage messageObject) {
        AuctionsSorting sorting = messageObject.sorting();
        AuctionsFilter filter = messageObject.filter();
        AuctionCategories category = messageObject.category();

        List<Document> results = AuctionService.cacheService.getAuctions(category.toString(), filter);

        if (results.isEmpty()) {
            return new AuctionFetchItemsProtocolObject.AuctionFetchItemsResponse(new ArrayList<>());
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

        return new AuctionFetchItemsProtocolObject.AuctionFetchItemsResponse(results.stream().map(AuctionItem::fromDocument).toList());
    }
}

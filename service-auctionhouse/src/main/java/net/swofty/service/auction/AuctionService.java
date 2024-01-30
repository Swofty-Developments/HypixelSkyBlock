package net.swofty.service.auction;

import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;

public class AuctionService implements SkyBlockService {
    public static void main(String[] args) {
        SkyBlockService.init(new AuctionService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.AUCTION_HOUSE;
    }
}

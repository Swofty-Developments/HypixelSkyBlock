package net.swofty.commons.auctions;

public enum DarkAuctionPhase {
    QUEUE,      // 35 seconds to enter
    BIDDING,    // Active bidding
    BETWEEN,    // Between rounds
    COMPLETE    // Done
}

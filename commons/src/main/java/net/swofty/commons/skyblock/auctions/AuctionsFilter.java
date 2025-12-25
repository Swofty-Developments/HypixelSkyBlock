package net.swofty.commons.skyblock.auctions;

public enum AuctionsFilter {
    SHOW_ALL,
    BIN_ONLY,
    AUCTIONS_ONLY,
    ;

    public AuctionsFilter next() {
        return values()[(ordinal() + 1) % values().length];
    }

    public AuctionsFilter previous() {
        return values()[(ordinal() - 1 + values().length) % values().length];
    }
}

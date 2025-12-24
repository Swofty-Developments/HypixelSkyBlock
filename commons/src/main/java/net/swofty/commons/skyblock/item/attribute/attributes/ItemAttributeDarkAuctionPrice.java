package net.swofty.commons.skyblock.item.attribute.attributes;

import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeDarkAuctionPrice extends ItemAttribute<Long> {
    @Override
    public String getKey() {
        return "dark_auction_price";
    }

    @Override
    public Long getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return 0L;
    }

    @Override
    public Long loadFromString(String string) {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    @Override
    public String saveIntoString() {
        return String.valueOf(getValue());
    }
}

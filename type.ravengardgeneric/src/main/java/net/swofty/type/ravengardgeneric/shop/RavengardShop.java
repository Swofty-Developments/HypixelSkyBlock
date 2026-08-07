package net.swofty.type.ravengardgeneric.shop;

import java.util.List;

public record RavengardShop(String id, String title, String banner,
                            List<Integer> shelfSlots, List<PoolEntry> pool) {

    public record PoolEntry(String item, int price) {
    }
}

package net.swofty.type.ravengardgeneric.shop;

import java.util.List;

public record RavengardShop(String id, String title, String banner, List<Entry> stock) {

    public record Entry(int slot, String item, int price, boolean outOfStock) {
    }
}

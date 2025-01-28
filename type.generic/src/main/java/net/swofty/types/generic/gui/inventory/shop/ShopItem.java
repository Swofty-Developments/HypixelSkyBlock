package net.swofty.types.generic.gui.inventory.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.shop.ShopPrice;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class ShopItem {
    private final SkyBlockItem item;
    private final int amount;
    private final ShopPrice price;
    private final boolean stackable;
    private List<String> lore = null;
    @Setter
    private String displayName = null;
    private boolean hasStock = true;

    public ShopItem(SkyBlockItem item, int amount, ShopPrice price, boolean stackable, boolean hasStock) {
        this.item = item;
        this.amount = amount;
        this.price = price;
        this.stackable = stackable;
        this.hasStock = hasStock;
    }

    public void setDisplayLore(List<String> lores) {
        this.lore = lores;
    }

    public static ShopItem Stackable(SkyBlockItem item, int amount, ShopPrice price) {
        return new ShopItem(item, amount, price, true);
    }

    public static ShopItem Single(SkyBlockItem item, int amount, ShopPrice price) {
        return new ShopItem(item, amount, price, false);
    }
}
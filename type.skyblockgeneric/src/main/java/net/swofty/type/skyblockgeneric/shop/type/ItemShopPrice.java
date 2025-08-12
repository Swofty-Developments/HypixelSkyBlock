package net.swofty.type.skyblockgeneric.shop.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.shop.ShopPrice;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemShopPrice implements ShopPrice {
    ItemType type;
    int amount;

    @Override
    public List<String> getGUIDisplay() {
        return List.of("§6" + type.rarity.getColor() + type.getDisplayName() + " §8x" + amount);
    }

    @Override
    public String getNamePlural() {
        return "configuration/items";
    }

    @Override
    public boolean canAfford(HypixelPlayer player) {
        return player.getAmountInInventory(type) >= amount;
    }

    @Override
    public void processPurchase(HypixelPlayer player) {
        player.takeItem(type, amount);
    }

    @Override
    public ShopPrice multiply(int amount) {
        return new ItemShopPrice(type, this.amount * amount);
    }

    @Override
    public ShopPrice divide(double amount) {
        return new ItemShopPrice(type, Math.max((int) (this.amount / amount), 1));
    }

}

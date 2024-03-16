package net.swofty.types.generic.shop.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.shop.ShopPrice;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CoinAndItemShopPrice implements ShopPrice {
    ItemType item;
    int itemAmount;
    int coinAmount;

    @Override
    public List<String> getGUIDisplay() {
        return List.of(
                "§6" + item.rarity.getColor() + item.getDisplayName() + " §8x" + itemAmount,
                "§6" + coinAmount + " Coin" + (coinAmount != 1 ? "s" : "")
        );
    }

    @Override
    public String getNamePlural() {
        return "items and coins";
    }

    @Override
    public boolean canAfford(SkyBlockPlayer player) {
        return player.getAmountInInventory(item) >= itemAmount && player.getCoins() >= coinAmount;
    }

    @Override
    public void processPurchase(SkyBlockPlayer player) {
        player.takeItem(item, itemAmount);
        player.takeCoins(coinAmount);
    }

    @Override
    public ShopPrice multiply(int amount) {
        return new CoinAndItemShopPrice(item, itemAmount * amount, coinAmount * amount);
    }

    @Override
    public ShopPrice divide(double amount) {
        return new CoinAndItemShopPrice(item,
                Math.max((int) (itemAmount / amount), 1), Math.max((int) (coinAmount / amount), 1));
    }
}

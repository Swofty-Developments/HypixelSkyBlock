package net.swofty.type.skyblockgeneric.shop.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.shop.ShopPrice;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

/**
 * @author <a href="https://github.com/Neruxov">Neruxov</a>
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CoinShopPrice implements ShopPrice {

    double amount;

    @Override
    public List<String> getGUIDisplay() {
        return List.of("§6" + StringUtility.decimalify(amount, 1) + " Coin" + (amount != 1 ? "s" : ""));
    }

    @Override
    public String getNamePlural() {
        return "coins";
    }

    @Override
    public boolean canAfford(HypixelPlayer player) {
        return player.getCoins() >= amount;
    }

    @Override
    public void processPurchase(HypixelPlayer player) {
        player.removeCoins(amount);
    }

    @Override
    public ShopPrice multiply(int amount) {
        return new CoinShopPrice(this.amount * amount);
    }

    @Override
    public ShopPrice divide(double amount) {
        return new CoinShopPrice(this.amount / amount);
    }

}

package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.util.BedWarsInventoryManipulator;

import java.util.function.Function;

@Getter
public abstract class ShopItem {

    private final String id;
    private final String name;
    private final String description;
    private final Function<BedwarsGameType, Integer> price;
    private final int amount;
    private final Currency currency;
    private final ItemStack display;

    public ShopItem(String id, String name, String description, int price, int amount, Currency currency, Material display) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = (_) -> price;
        this.amount = amount;
        this.currency = currency;
        this.display = ItemStack.of(display);
    }

    public ShopItem(String id, String name, String description, Function<BedwarsGameType, Integer> price, int amount, Currency currency, Material display) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.currency = currency;
        this.display = ItemStack.of(display);
    }

    /**
     * Called when a player purchases an item. This method should be used to give items to the player or apply effects.
     *
     * @param player the player who purchased the item
     */
    public abstract void onPurchase(BedWarsPlayer player);

    public boolean isOwned(Player player) {
        return true;
    }

    /**
     * Handles the purchase of an item meanwhile taking the currency from the player's inventory.
     *
     * @param player   the player making the purchase
     * @param gameType the gametype to determine the price
     */
    public void handlePurchase(BedWarsPlayer player, BedwarsGameType gameType) {
        BedWarsInventoryManipulator.removeItems(player, currency.getMaterial(), price.apply(gameType));
        onPurchase(player);
    }

}

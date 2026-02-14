package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.bedwars.BedWarsGameType;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.bedwarsgame.util.BedWarsInventoryManipulator;
import net.swofty.type.generic.data.datapoints.DatapointBedWarsHotbar;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Getter
public abstract class ShopItem {

    private final String id;
    private final String name;
    private final String description;
    private final Function<BedWarsGameType, Integer> price;
    private final int amount;
    private final Currency currency;
    private final ItemStack display;
    @Nullable
    private final DatapointBedWarsHotbar.HotbarItemType hotbarItemType;

    public ShopItem(String id, String name, String description, int price, int amount, Currency currency, Material display) {
        this(id, name, description, (_) -> price, amount, currency, display, null);
    }

    public ShopItem(String id, String name, String description, int price, int amount, Currency currency, Material display,
                    @Nullable DatapointBedWarsHotbar.HotbarItemType hotbarItemType) {
        this(id, name, description, (_) -> price, amount, currency, display, hotbarItemType);
    }

    public ShopItem(String id, String name, String description, Function<BedWarsGameType, Integer> price, int amount, Currency currency, Material display) {
        this(id, name, description, price, amount, currency, display, null);
    }

    public ShopItem(String id, String name, String description, Function<BedWarsGameType, Integer> price, int amount, Currency currency, Material display,
                    @Nullable DatapointBedWarsHotbar.HotbarItemType hotbarItemType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.currency = currency;
        this.display = ItemStack.of(display);
        this.hotbarItemType = hotbarItemType;
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
    public void handlePurchase(BedWarsPlayer player, BedWarsGameType gameType) {
        BedWarsInventoryManipulator.removeItems(player, currency.getMaterial(), price.apply(gameType));
        onPurchase(player);
    }

    protected void giveItem(BedWarsPlayer player, ItemStack itemStack) {
        BedWarsInventoryManipulator.addItemWithHotbarPriority(player, itemStack, hotbarItemType);
    }
}

package net.swofty.type.bedwarsgame.item;

import lombok.Getter;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;
import net.swofty.commons.bedwars.BedWarsGameType;
import net.swofty.type.bedwarsgame.shop.Currency;
import net.swofty.type.generic.data.datapoints.DatapointBedWarsHotbar;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Getter
public abstract class SimpleInteractableItem {

    private final String id;
    @Nullable
    private final ShopData shopData;

    public SimpleInteractableItem(String id) {
        this(id, null);
    }

    public SimpleInteractableItem(String id, @Nullable ShopData shopData) {
        this.id = id;
        this.shopData = shopData;
    }

    public abstract ItemStack getBlandItem();

    public ItemStack getItemStack() {
        return getBlandItem().with(DataComponents.CUSTOM_DATA,
                new CustomData(CompoundBinaryTag.builder().putString("item", id).build()));
    }

    public void onItemFinishUse(PlayerFinishItemUseEvent event) {
        // stub
    }

    public void onItemUseOnBlock(PlayerUseItemOnBlockEvent event) {
        // stub
    }

    public void onItemUse(PlayerUseItemEvent event) {
        // stub
    }

    public void onItemDrop(ItemDropEvent event) {
        // stub
    }

    public void onItemInteract(PlayerInstanceEvent event) {
        // stub
    }

    public void onBlockPlace(PlayerBlockPlaceEvent event) {
        // stub
    }

    public boolean isShopBacked() {
        return shopData != null;
    }

    public record ShopData(
        String name,
        String description,
        Function<BedWarsGameType, Integer> price,
        int amount,
        Currency currency,
        @Nullable DatapointBedWarsHotbar.HotbarItemType hotbarItemType,
        int... categories
    ) {
        public ShopData(String name, String description, int price, int amount, Currency currency,
                        @Nullable DatapointBedWarsHotbar.HotbarItemType hotbarItemType, int... categories) {
            this(name, description, (_) -> price, amount, currency, hotbarItemType, categories);
        }
    }
}

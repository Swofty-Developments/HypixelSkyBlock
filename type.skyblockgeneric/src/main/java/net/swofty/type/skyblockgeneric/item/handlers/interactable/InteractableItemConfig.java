package net.swofty.type.skyblockgeneric.item.handlers.interactable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Getter
@Setter
@Builder
public class InteractableItemConfig {
    private BiConsumer<HypixelPlayer, SkyBlockItem> rightClickHandler;
    private BiConsumer<HypixelPlayer, SkyBlockItem> leftClickHandler;
    private BiFunction<HypixelPlayer, SkyBlockItem, Boolean> inventoryInteractHandler;

    public InteractableItemConfig(BiConsumer<HypixelPlayer, SkyBlockItem> rightClickHandler,
                                  BiConsumer<HypixelPlayer, SkyBlockItem> leftClickHandler,
                                  BiFunction<HypixelPlayer, SkyBlockItem, Boolean> inventoryInteractHandler) {
        this.rightClickHandler = rightClickHandler;
        this.leftClickHandler = leftClickHandler;
        this.inventoryInteractHandler = inventoryInteractHandler;
    }

    public void onRightClick(HypixelPlayer player, SkyBlockItem item) {
        if (rightClickHandler != null) rightClickHandler.accept(player, item);
    }

    public void onLeftClick(HypixelPlayer player, SkyBlockItem item) {
        if (leftClickHandler != null) leftClickHandler.accept(player, item);
    }

    public boolean onInventoryInteract(HypixelPlayer player, SkyBlockItem item) {
        return inventoryInteractHandler != null && inventoryInteractHandler.apply(player, item);
    }
}

package net.swofty.types.generic.item.handlers.interactable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Getter
@Setter
@Builder
public class InteractableItemConfig {
    private BiConsumer<SkyBlockPlayer, SkyBlockItem> rightClickHandler;
    private BiConsumer<SkyBlockPlayer, SkyBlockItem> leftClickHandler;
    private BiFunction<SkyBlockPlayer, SkyBlockItem, Boolean> inventoryInteractHandler;

    public InteractableItemConfig(BiConsumer<SkyBlockPlayer, SkyBlockItem> rightClickHandler,
                                  BiConsumer<SkyBlockPlayer, SkyBlockItem> leftClickHandler,
                                  BiFunction<SkyBlockPlayer, SkyBlockItem, Boolean> inventoryInteractHandler) {
        this.rightClickHandler = rightClickHandler;
        this.leftClickHandler = leftClickHandler;
        this.inventoryInteractHandler = inventoryInteractHandler;
    }

    public void onRightClick(SkyBlockPlayer player, SkyBlockItem item) {
        if (rightClickHandler != null) rightClickHandler.accept(player, item);
    }

    public void onLeftClick(SkyBlockPlayer player, SkyBlockItem item) {
        if (leftClickHandler != null) leftClickHandler.accept(player, item);
    }

    public boolean onInventoryInteract(SkyBlockPlayer player, SkyBlockItem item) {
        return inventoryInteractHandler != null && inventoryInteractHandler.apply(player, item);
    }
}

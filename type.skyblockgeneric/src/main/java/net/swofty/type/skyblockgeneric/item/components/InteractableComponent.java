package net.swofty.type.skyblockgeneric.item.components;

import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.SkyBlockItemComponent;
import net.swofty.type.generic.item.handlers.interactable.InteractableRegistry;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class InteractableComponent extends SkyBlockItemComponent {
    private final BiConsumer<HypixelPlayer, SkyBlockItem> rightClickHandler;
    private final BiConsumer<HypixelPlayer, SkyBlockItem> leftClickHandler;
    private final BiFunction<HypixelPlayer, SkyBlockItem, Boolean> inventoryInteractHandler;

    public InteractableComponent(BiConsumer<HypixelPlayer, SkyBlockItem> rightClickHandler,
                                 BiConsumer<HypixelPlayer, SkyBlockItem> leftClickHandler,
                                 BiFunction<HypixelPlayer, SkyBlockItem, Boolean> inventoryInteractHandler) {
        this.rightClickHandler = rightClickHandler;
        this.leftClickHandler = leftClickHandler;
        this.inventoryInteractHandler = inventoryInteractHandler;
    }

    public InteractableComponent(String handlerId) {
        this(InteractableRegistry.getHandler(handlerId).getRightClickHandler(),
                InteractableRegistry.getHandler(handlerId).getLeftClickHandler(),
                InteractableRegistry.getHandler(handlerId).getInventoryInteractHandler());
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
package net.swofty.types.generic.item.components;

import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.item.handlers.interactable.InteractableRegistry;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class InteractableComponent extends SkyBlockItemComponent {
    private final BiConsumer<SkyBlockPlayer, SkyBlockItem> rightClickHandler;
    private final BiConsumer<SkyBlockPlayer, SkyBlockItem> leftClickHandler;
    private final Predicate<SkyBlockPlayer> inventoryInteractHandler;

    public InteractableComponent(BiConsumer<SkyBlockPlayer, SkyBlockItem> rightClickHandler,
                                 BiConsumer<SkyBlockPlayer, SkyBlockItem> leftClickHandler,
                                 Predicate<SkyBlockPlayer> inventoryInteractHandler) {
        this.rightClickHandler = rightClickHandler;
        this.leftClickHandler = leftClickHandler;
        this.inventoryInteractHandler = inventoryInteractHandler;
    }

    public InteractableComponent(String handlerId) {
        this(InteractableRegistry.getHandler(handlerId).getRightClickHandler(),
                InteractableRegistry.getHandler(handlerId).getLeftClickHandler(),
                InteractableRegistry.getHandler(handlerId).getInventoryInteractHandler());
    }

    public void onRightClick(SkyBlockPlayer player, SkyBlockItem item) {
        if (rightClickHandler != null) rightClickHandler.accept(player, item);
    }

    public void onLeftClick(SkyBlockPlayer player, SkyBlockItem item) {
        if (leftClickHandler != null) leftClickHandler.accept(player, item);
    }

    public boolean onInventoryInteract(SkyBlockPlayer player) {
        return inventoryInteractHandler != null && inventoryInteractHandler.test(player);
    }
}
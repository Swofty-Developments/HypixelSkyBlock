package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.starter;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.levels.SkyBlockLevelCause;
import net.swofty.types.generic.levels.causes.NewAccessoryLevelCause;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.List;
import java.util.stream.Stream;

public class GUIStarterAccessories extends SkyBlockAbstractInventory {
    private final List<NewAccessoryLevelCause> causes;

    public GUIStarterAccessories(ItemType... causes) {
        super(InventoryType.CHEST_6_ROW);
        this.causes = Stream.of(causes).map(SkyBlockLevelCause::getAccessoryCause).toList();
        doAction(new SetTitleAction(Component.text("Starter -> Accessories")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Fill background
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        // Setup close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No special cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed for sudden quit
    }
}
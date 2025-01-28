package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.levels.SkyBlockLevelRequirement;
import net.swofty.types.generic.levels.SkyBlockLevelUnlock;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUISkyBlockLevel extends SkyBlockAbstractInventory {
    private static final Map<Integer, List<Integer>> SLOTS_MAP = new HashMap<>(
            Map.of(
                    1, List.of(13),
                    2, List.of(12, 14),
                    3, List.of(11, 13, 15),
                    4, List.of(10, 12, 14, 16),
                    5, List.of(11, 12, 13, 14, 15)
            )
    );

    private final SkyBlockLevelRequirement levelRequirement;

    public GUISkyBlockLevel(SkyBlockLevelRequirement levelRequirement) {
        super(InventoryType.CHEST_4_ROW);
        this.levelRequirement = levelRequirement;
        doAction(new SetTitleAction(Component.text("Level " + levelRequirement.asInt() + " Rewards")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Navigation buttons
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(30)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To SkyBlock Levels").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockLevels());
                    return true;
                })
                .build());

        // Unlock rewards
        List<SkyBlockLevelUnlock> unlocks = levelRequirement.getUnlocks();
        List<Integer> slots = SLOTS_MAP.get(unlocks.size());

        for (int i = 0; i < unlocks.size(); i++) {
            SkyBlockLevelUnlock unlock = unlocks.get(i);
            final int index = i;

            attachItem(GUIItem.builder(slots.get(index))
                    .item(() -> unlock.getItemDisplay(owner, levelRequirement.asInt()))
                    .build());
        }
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {}
}
package net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIMinionRecipes extends SkyBlockAbstractInventory {
    private static final Map<Integer, int[]> SLOTS = new HashMap<>(Map.of(
            10, new int[] { 11, 12, 13, 14, 15, 20, 21, 22, 23, 24 },
            11, new int[] { 11, 12, 13, 14, 15, 21, 22, 23, 30, 31, 32 }
    ));

    private final SkyBlockAbstractInventory previousGUI;
    private final MinionRegistry minionRegistry;

    public GUIMinionRecipes(MinionRegistry minionRegistry, SkyBlockAbstractInventory previousGUI) {
        super(InventoryType.CHEST_6_ROW);
        this.previousGUI = previousGUI;
        this.minionRegistry = minionRegistry;
        doAction(new SetTitleAction(Component.text(StringUtility.toNormalCase(minionRegistry.toString()) + " Minion Recipes")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + previousGUI.getTitleAsString()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(previousGUI);
                    return true;
                })
                .build());

        List<SkyBlockMinion.MinionTier> craftableMinionTiers = new ArrayList<>();
        for (SkyBlockMinion.MinionTier minionTier : minionRegistry.asSkyBlockMinion().getTiers()) {
            if (minionTier.craftable()) craftableMinionTiers.add(minionTier);
        }

        int[] slots = SLOTS.get(craftableMinionTiers.size());
        int i = 0;
        for (SkyBlockMinion.MinionTier minionTier : craftableMinionTiers) {
            int slot = slots[i];
            i++;
            SkyBlockItem minion = new SkyBlockItem(minionRegistry.getItemType());
            minion.getAttributeHandler().setMinionData(new ItemAttributeMinionData.MinionData(minionTier.tier(), 0));

            attachItem(GUIItem.builder(slot)
                    .item(ItemStackCreator.getStackHead(minion.getDisplayName(),
                            minionTier.texture(), 1, minion.getLore()).build())
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUIRecipe(minion,
                                new GUIMinionRecipes(minionRegistry, previousGUI),
                                minionTier.tier() - 1));
                        return true;
                    })
                    .build());
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}

    @Override
    public boolean allowHotkeying() {
        return false;
    }
}
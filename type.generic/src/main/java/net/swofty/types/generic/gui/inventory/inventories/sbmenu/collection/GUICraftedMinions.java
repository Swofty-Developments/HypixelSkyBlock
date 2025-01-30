package net.swofty.types.generic.gui.inventory.inventories.sbmenu.collection;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMinionData;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.SkyBlockPaginatedInventory;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIMinionRecipes;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.MinionComponent;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.*;

public class GUICraftedMinions extends SkyBlockPaginatedInventory<SkyBlockItem> {
    private static final String STATE_MINION_UNLOCKED = "minion_unlocked";
    private static final String STATE_MINION_LOCKED = "minion_locked";
    private static final String STATE_MINION_MAXED = "minion_maxed";

    private final SkyBlockAbstractInventory previousGUI;

    public GUICraftedMinions(SkyBlockAbstractInventory previousGUI) {
        super(InventoryType.CHEST_6_ROW);
        this.previousGUI = previousGUI;
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

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
                        "§7To " + previousGUI.getTitle()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(previousGUI);
                    return true;
                })
                .build());
    }

    @Override
    public int[] getPaginatedSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    @Override
    public PaginationList<SkyBlockItem> fillPaged(SkyBlockPlayer player, PaginationList<SkyBlockItem> paged) {
        paged.addAll(Arrays.stream(ItemType.values())
                .map(SkyBlockItem::new)
                .filter(item -> item.hasComponent(MinionComponent.class))
                .toList());
        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, SkyBlockItem item) {
        return false;
    }

    @Override
    public void performSearch(SkyBlockPlayer player, String query, int page, int maxPage) {
        if (page > 1) {
            attachItem(createNavigationButton(45, query, page, false));
        }
        if (page < maxPage) {
            attachItem(createNavigationButton(53, query, page, true));
        }
    }

    @Override
    public Component getTitle(SkyBlockPlayer player, String query, int page, PaginationList<SkyBlockItem> paged) {
        return Component.text("Crafted Minions");
    }

    @Override
    public GUIItem createItemFor(SkyBlockItem item, int slot, SkyBlockPlayer player) {
        MinionRegistry minionRegistry = item.getAttributeHandler().getMinionType();
        DatapointMinionData.ProfileMinionData playerData = player.getDataHandler()
                .get(DataHandler.Data.MINION_DATA, DatapointMinionData.class).getValue();

        List<Integer> tiers = playerData.craftedMinions().stream()
                .filter(entry -> Objects.equals(entry.getKey(), minionRegistry.name()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(List.of());

        boolean unlocked = !tiers.isEmpty();
        int minionAmount = tiers.size();
        boolean maxed = minionAmount == minionRegistry.asSkyBlockMinion().getTiers().size();

        return GUIItem.builder(slot)
                .requireState(unlocked ? (maxed ? STATE_MINION_MAXED : STATE_MINION_UNLOCKED) : STATE_MINION_LOCKED)
                .item(() -> {
                    if (!unlocked) {
                        return ItemStackCreator.getStack("§c" + StringUtility.toNormalCase(minionRegistry.name()) + " Minion",
                                Material.GRAY_DYE, 1, "§7You haven't crafted this minion.").build();
                    }

                    ArrayList<String> lore = new ArrayList<>();
                    for (SkyBlockMinion.MinionTier minionTier : minionRegistry.asSkyBlockMinion().getTiers()) {
                        if (tiers.contains(minionTier.tier())) {
                            lore.add("§a✔ Tier " + StringUtility.getAsRomanNumeral(minionTier.tier()));
                        } else {
                            lore.add("§c✖ Tier " + StringUtility.getAsRomanNumeral(minionTier.tier()));
                        }
                    }

                    lore.add("");
                    lore.add("§eClick to view recipes!");

                    String color = maxed ? "§a" : "§e";
                    return ItemStackCreator.getStackHead(
                            color + StringUtility.toNormalCase(minionRegistry.name()) + " Minion",
                            minionRegistry.asSkyBlockMinion().getTiers().getFirst().texture(),
                            1,
                            lore).build();
                })
                .onClick((ctx, clickedItem) -> {
                    if (!unlocked) return false;

                    ctx.player().openInventory(new GUIMinionRecipes(minionRegistry,
                            new GUICraftedMinions(new GUICollections())));
                    return true;
                })
                .build();
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}
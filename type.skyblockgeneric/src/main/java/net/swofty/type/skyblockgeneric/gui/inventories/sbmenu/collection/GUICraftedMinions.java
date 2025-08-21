package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.collection;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.HypixelPaginatedGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.PaginationList;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointMinionData;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIMinionRecipes;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.MinionComponent;
import net.swofty.type.skyblockgeneric.minion.MinionRegistry;
import net.swofty.type.skyblockgeneric.minion.SkyBlockMinion;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;

public class GUICraftedMinions extends HypixelPaginatedGUI<SkyBlockItem> {

    HypixelInventoryGUI previousGUI;

    public GUICraftedMinions(HypixelInventoryGUI previousGUI) {
        super(InventoryType.CHEST_6_ROW);
        this.previousGUI = previousGUI;
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
    protected PaginationList<SkyBlockItem> fillPaged(HypixelPlayer player, PaginationList<SkyBlockItem> paged) {
        paged.addAll(Arrays.stream(ItemType.values()).map(SkyBlockItem::new).toList());
        paged.removeIf(item -> !(item.hasComponent(MinionComponent.class)));
        return paged;
    }

    @Override
    protected boolean shouldFilterFromSearch(String query, SkyBlockItem item) {
        return false;
    }

    @Override
    protected void performSearch(HypixelPlayer player, String query, int page, int maxPage) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, ""));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, previousGUI));

        if (page > 1) {
            set(createNavigationButton(this, 45, query, page, false));
        }
        if (page < maxPage) {
            set(createNavigationButton(this, 53, query, page, true));
        }
    }

    @Override
    protected String getTitle(HypixelPlayer player, String query, int page, PaginationList<SkyBlockItem> paged) {
        return "Crafted Minions";
    }

    @Override
    protected GUIClickableItem createItemFor(SkyBlockItem item, int slot, HypixelPlayer player) {

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (e.getClickedItem().material() != Material.GRAY_DYE) {
                    new GUIMinionRecipes(item.getAttributeHandler().getMinionType(), new GUICraftedMinions(new GUICollections())).open(player);
                }
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStack.Builder itemStack;
                MinionRegistry minionRegistry = item.getAttributeHandler().getMinionType();
                DatapointMinionData.ProfileMinionData playerData = player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.MINION_DATA, DatapointMinionData.class).getValue();
                ArrayList<String> lore = new ArrayList<>();
                List<Integer> tiers = List.of();
                boolean unlocked = false;
                int minionAmount = 0;

                for (Map.Entry<String, List<Integer>> minion : playerData.craftedMinions()) {
                    if (Objects.equals(minion.getKey(), minionRegistry.name())) {
                        tiers = minion.getValue();
                    }
                }
                for (SkyBlockMinion.MinionTier minionTier : minionRegistry.asSkyBlockMinion().getTiers()) {
                    if (tiers.contains(minionTier.tier())) {
                        lore.add("§a✔ Tier " + StringUtility.getAsRomanNumeral(minionTier.tier()));
                        unlocked = true;
                        minionAmount++;
                    } else {
                        lore.add("§c✖ Tier " + StringUtility.getAsRomanNumeral(minionTier.tier()));
                    }
                }
                if (unlocked) {
                    lore.add("");
                    lore.add("§eClick to view recipes!");
                    String color;
                    color = (minionAmount == minionRegistry.asSkyBlockMinion().getTiers().size()) ? "§a" : "§e";
                    itemStack = ItemStackCreator.getStackHead(color + StringUtility.toNormalCase(minionRegistry.name()) + " Minion",
                            minionRegistry.asSkyBlockMinion().getTiers().getFirst().texture(), 1, lore);
                } else {
                    itemStack = ItemStackCreator.getStack("§c" + StringUtility.toNormalCase(minionRegistry.name()) + " Minion",
                            Material.GRAY_DYE, 1, "§7You haven't crafted this minion.");
                }
                return itemStack;
            }
        };
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}

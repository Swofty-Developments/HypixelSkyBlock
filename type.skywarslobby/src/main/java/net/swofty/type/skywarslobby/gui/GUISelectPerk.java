package net.swofty.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointSkywarsUnlocks;
import net.swofty.type.generic.data.handlers.SkywarsDataHandler;
import net.swofty.type.generic.gui.inventory.HypixelPaginatedGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.PaginationList;
import net.swofty.type.skywarslobby.perk.SkywarsPerk;
import net.swofty.type.skywarslobby.perk.SkywarsPerkRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Paginated GUI for selecting a perk to put in a specific slot.
 */
public class GUISelectPerk extends HypixelPaginatedGUI<SkywarsPerk> {
    private static final int[] PAGINATED_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    private static final String MODE = "NORMAL";
    private final int slotIndex;

    public GUISelectPerk(int slotIndex) {
        super(InventoryType.CHEST_6_ROW);
        this.slotIndex = slotIndex;
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected PaginationList<SkywarsPerk> fillPaged(HypixelPlayer player, PaginationList<SkywarsPerk> paged) {
        List<SkywarsPerk> perks = SkywarsPerkRegistry.getSelectablePerksSortedByRarity(MODE, true);
        for (SkywarsPerk perk : perks) {
            paged.add(perk);
        }
        return paged;
    }

    @Override
    protected boolean shouldFilterFromSearch(String query, SkywarsPerk item) {
        return !item.getName().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    protected void performSearch(HypixelPlayer player, String query, int page, int maxPage) {
        // Navigation
        if (page > 1) {
            set(createNavigationButton(this, 45, query, page, false));
        }
        if (page < maxPage) {
            set(new GUIClickableItem(53) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer player) {
                    return ItemStackCreator.getStack(
                            "§eLeft-click for next page!",
                            Material.ARROW,
                            1,
                            "§bRight-click for last page!"
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    new GUISelectPerk(slotIndex).open(player, query, page + 1);
                }
            });
        }

        // Go Back (slot 49)
        set(GUIClickableItem.getGoBackItem(49, new GUISelectNormalPerks()));
    }

    @Override
    protected String getTitle(HypixelPlayer player, String query, int page, PaginationList<SkywarsPerk> paged) {
        return "Select Perk";
    }

    @Override
    protected GUIClickableItem createItemFor(SkywarsPerk perk, int slot, HypixelPlayer player) {
        return new GUIClickableItem(slot) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
                if (handler == null) return ItemStack.builder(Material.BARRIER);

                DatapointSkywarsUnlocks.SkywarsUnlocks unlocks = handler.get(
                        SkywarsDataHandler.Data.UNLOCKS,
                        DatapointSkywarsUnlocks.class
                ).getValue();

                boolean owned = unlocks.hasPerk(perk.getId());
                boolean alreadyActive = unlocks.isPerkSelectedForMode(MODE, perk.getId());
                long coins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();

                List<String> lore = new ArrayList<>();
                lore.add("§8Perk");
                lore.add("");
                lore.add("§7" + perk.getEffectDescription());
                lore.add("");
                lore.add("§7Rarity: " + perk.getRarity().getFormattedName());
                lore.add("");

                // Status and action
                String specialStatus = perk.getSpecialStatus();
                if (specialStatus != null) {
                    lore.add("§c§l!! " + specialStatus);
                } else if (alreadyActive) {
                    lore.add("§aThis perk is already active.");
                } else if (owned) {
                    lore.add("§eClick to select!");
                } else if (perk.isPurchasableWithCoins()) {
                    lore.add("§7Cost: §6" + String.format("%,d", perk.getCost()));
                    if (perk.isSoulWellDrop()) {
                        lore.add("§bAlso found in the Soul Well!");
                    }
                    lore.add("");
                    if (coins >= perk.getCost()) {
                        lore.add("§eClick to purchase!");
                    } else {
                        lore.add("§cNot enough coins!");
                    }
                } else if (perk.costsOpal()) {
                    lore.add("§7Cost: §9" + perk.getOpalCost() + " Opal" + (perk.getOpalCost() > 1 ? "s" : ""));
                    if (perk.isSoulWellDrop()) {
                        lore.add("§bAlso found in the Soul Well!");
                    }
                    lore.add("");
                    lore.add("§9Purchase with Opals in Angel's Descent");
                } else if (perk.isFree()) {
                    lore.add("§aFREE");
                    if (perk.isSoulWellDrop()) {
                        lore.add("§bAlso found in the Soul Well!");
                    }
                    lore.add("");
                    lore.add("§eClick to unlock!");
                }

                String nameColor = owned ? "§a" : (specialStatus != null ? "§c" : "§a");
                return ItemStackCreator.getStack(nameColor + perk.getName(), perk.getIconMaterial(), 1, lore);
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);
                if (handler == null) return;

                DatapointSkywarsUnlocks.SkywarsUnlocks unlocks = handler.get(
                        SkywarsDataHandler.Data.UNLOCKS,
                        DatapointSkywarsUnlocks.class
                ).getValue();

                String specialStatus = perk.getSpecialStatus();
                if (specialStatus != null) {
                    player.sendMessage("§c" + specialStatus + " - cannot purchase here.");
                    return;
                }

                boolean owned = unlocks.hasPerk(perk.getId());
                boolean alreadyActive = unlocks.isPerkSelectedForMode(MODE, perk.getId());

                if (alreadyActive) {
                    player.sendMessage("§cThis perk is already active in another slot!");
                    return;
                }

                if (owned) {
                    // Select the perk for this slot
                    unlocks.selectPerkForSlot(MODE, slotIndex, perk.getId());
                    player.sendMessage("§aSelected §e" + perk.getName() + " §afor slot #" + (slotIndex + 1) + "!");
                    new GUISelectNormalPerks().open(player);
                } else if (perk.isPurchasableWithCoins()) {
                    long coins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();
                    if (coins >= perk.getCost()) {
                        handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class)
                                .setValue(coins - perk.getCost());
                        unlocks.unlockPerk(perk.getId());
                        unlocks.selectPerkForSlot(MODE, slotIndex, perk.getId());
                        player.sendMessage("§aYou purchased and selected §e" + perk.getName() + "§a!");
                        new GUISelectNormalPerks().open(player);
                    } else {
                        player.sendMessage("§cYou don't have enough coins to purchase this perk!");
                    }
                } else if (perk.isFree()) {
                    unlocks.unlockPerk(perk.getId());
                    unlocks.selectPerkForSlot(MODE, slotIndex, perk.getId());
                    player.sendMessage("§aUnlocked and selected §e" + perk.getName() + "§a!");
                    new GUISelectNormalPerks().open(player);
                } else {
                    player.sendMessage("§cThis perk cannot be purchased here.");
                }
            }
        };
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}

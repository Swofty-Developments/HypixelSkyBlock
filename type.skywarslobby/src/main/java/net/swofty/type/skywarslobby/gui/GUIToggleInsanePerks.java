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
 * GUI for toggling Insane mode perks on/off.
 * Shows all perks (owned + unowned) with toggle/purchase options.
 */
public class GUIToggleInsanePerks extends HypixelPaginatedGUI<SkywarsPerk> {
    private static final int[] PAGINATED_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    private static final String MODE = "INSANE";

    public GUIToggleInsanePerks() {
        super(InventoryType.CHEST_6_ROW);
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected PaginationList<SkywarsPerk> fillPaged(HypixelPlayer player, PaginationList<SkywarsPerk> paged) {
        // Show all selectable perks (owned + unowned)
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
                    new GUIToggleInsanePerks().open(player, query, page + 1);
                }
            });
        }

        // Go Back (slot 49)
        set(GUIClickableItem.getGoBackItem(49, new GUIKitsPerks()));
    }

    @Override
    protected String getTitle(HypixelPlayer player, String query, int page, PaginationList<SkywarsPerk> paged) {
        return "Toggle Insane Perks";
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
                boolean enabled = unlocks.isInsanePerkEnabled(MODE, perk.getId());
                long coins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();

                List<String> lore = new ArrayList<>();
                lore.add("§8Perk");
                lore.add("");
                lore.add("§7" + perk.getEffectDescription());
                lore.add("");
                lore.add("§7Rarity: " + perk.getRarity().getFormattedName());
                lore.add("");

                String specialStatus = perk.getSpecialStatus();
                if (specialStatus != null) {
                    lore.add("§c§l!! " + specialStatus);
                } else if (owned) {
                    if (enabled) {
                        lore.add("§a§lENABLED");
                    } else {
                        lore.add("§c§lDISABLED");
                    }
                    lore.add("");
                    lore.add("§eClick to toggle!");
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
                ItemStack.Builder builder = ItemStackCreator.getStack(nameColor + perk.getName(), perk.getIconMaterial(), 1, lore);

                // Add enchant glow if enabled
                if (owned && enabled) {
                    return ItemStackCreator.enchant(builder);
                }
                return builder;
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

                if (owned) {
                    // Toggle the perk
                    boolean wasEnabled = unlocks.isInsanePerkEnabled(MODE, perk.getId());
                    unlocks.toggleInsanePerk(MODE, perk.getId());

                    if (wasEnabled) {
                        player.sendMessage("§c✖ §7Disabled §e" + perk.getName() + " §7for Insane mode.");
                    } else {
                        player.sendMessage("§a✔ §7Enabled §e" + perk.getName() + " §7for Insane mode.");
                    }
                    new GUIToggleInsanePerks().open(player);
                } else if (perk.isPurchasableWithCoins()) {
                    long coins = handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class).getValue();
                    if (coins >= perk.getCost()) {
                        handler.get(SkywarsDataHandler.Data.COINS, DatapointLong.class)
                                .setValue(coins - perk.getCost());
                        unlocks.unlockPerk(perk.getId());
                        player.sendMessage("§aYou purchased §e" + perk.getName() + "§a! It is now enabled.");
                        new GUIToggleInsanePerks().open(player);
                    } else {
                        player.sendMessage("§cYou don't have enough coins to purchase this perk!");
                    }
                } else if (perk.isFree()) {
                    unlocks.unlockPerk(perk.getId());
                    player.sendMessage("§aUnlocked §e" + perk.getName() + "§a! It is now enabled.");
                    new GUIToggleInsanePerks().open(player);
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

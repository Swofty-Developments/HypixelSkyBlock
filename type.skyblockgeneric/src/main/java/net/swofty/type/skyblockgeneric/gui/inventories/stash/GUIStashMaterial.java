package net.swofty.type.skyblockgeneric.gui.inventories.stash;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelPaginatedGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.PaginationList;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointStash;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIStashMaterial extends HypixelPaginatedGUI<Map.Entry<ItemType, Integer>> {

    public GUIStashMaterial() {
        super(InventoryType.CHEST_6_ROW);
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
    public PaginationList<Map.Entry<ItemType, Integer>> fillPaged(HypixelPlayer player, PaginationList<Map.Entry<ItemType, Integer>> paged) {
        SkyBlockPlayer skyBlockPlayer = (SkyBlockPlayer) player;
        Map<ItemType, Integer> materialStash = skyBlockPlayer.getStash().getMaterialStash();
        paged.addAll(materialStash.entrySet());
        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, Map.Entry<ItemType, Integer> item) {
        return !item.getKey().getDisplayName().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void performSearch(HypixelPlayer p, String query, int page, int maxPage) {
        // Border
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));

        // Close button (slot 49)
        set(GUIClickableItem.getCloseItem(49));

        // Fill Inventory button (slot 48 - emerald)
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                e.setCancelled(true);

                DatapointStash.PlayerStash stash = player.getStash();
                int pickedUp = 0;

                List<ItemType> types = new ArrayList<>(stash.getMaterialStash().keySet());
                for (ItemType type : types) {
                    int amount = stash.getMaterialStash().getOrDefault(type, 0);
                    int maxStackSize = type.material.maxStackSize();

                    while (amount > 0 && player.hasEmptySlots(1)) {
                        int toPickup = Math.min(amount, maxStackSize);
                        int removed = stash.removeFromMaterialStash(type, toPickup);
                        if (removed > 0) {
                            SkyBlockItem newItem = new SkyBlockItem(type);
                            newItem.setAmount(removed);
                            player.addAndUpdateItem(newItem);
                            player.sendMessage("§aFrom stash: §f" + type.getDisplayName() + " §7x" + removed);
                            pickedUp += removed;
                            amount -= removed;
                        } else {
                            break;
                        }
                    }

                    if (!player.hasEmptySlots(1)) break;
                }

                if (pickedUp == 0) {
                    player.sendMessage("§cCouldn't unstash your material stash! Your inventory is full!");
                } else if (stash.getMaterialStashCount() == 0) {
                    player.sendMessage("§aYou picked up all items from your material stash!");
                } else {
                    player.sendMessage("§eYou still have §c" + stash.getMaterialStashCount() +
                            " §ematerials totalling §c" + stash.getMaterialTypeCount() +
                            " §etypes of materials in there!");
                }

                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                int count = player.getStash().getMaterialStashCount();
                int types = player.getStash().getMaterialTypeCount();
                return ItemStackCreator.getStack("§aFill Inventory", Material.EMERALD, 1,
                        "§7Pick up as many stashed materials",
                        "§7as can fit in your inventory.",
                        "",
                        "§7Materials stashed: §e" + StringUtility.commaify(count),
                        "§7Material types: §e" + types,
                        "",
                        "§eClick to fill inventory!");
            }
        });

        // Insert Into Sacks button (slot 50 - normal chest)
        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                e.setCancelled(true);

                DatapointStash.PlayerStash stash = player.getStash();
                int inserted = 0;

                List<ItemType> types = new ArrayList<>(stash.getMaterialStash().keySet());
                for (ItemType type : types) {
                    int amount = stash.getMaterialStash().getOrDefault(type, 0);
                    if (player.canInsertItemIntoSacks(type, amount)) {
                        player.getSackItems().increase(type, amount);
                        stash.removeFromMaterialStash(type, amount);
                        inserted += amount;
                    }
                }

                if (inserted > 0) {
                    player.sendMessage("§aYou have successfully transferred your items from this stash to your sacks!");
                } else {
                    player.sendMessage("§cNo items could be transferred to your sacks!");
                }

                // Refresh the GUI
                new GUIStashMaterial().open(player, query, 1);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§aInsert Into Sacks", Material.CHEST, 1,
                        "§7Move materials from your stash",
                        "§7into your sacks.",
                        "",
                        "§eClick to insert!");
            }
        });

        // Navigation buttons
        if (page > 1) {
            set(createNavigationButton(this, 45, query, page, false));
        }
        if (page < maxPage) {
            set(createNavigationButton(this, 53, query, page, true));
        }
    }

    @Override
    public String getTitle(HypixelPlayer player, String query, int page, PaginationList<Map.Entry<ItemType, Integer>> paged) {
        int maxPage = paged.getPageCount();
        if (maxPage <= 1) {
            return "Material Stash";
        }
        return "Material Stash (" + page + "/" + maxPage + ")";
    }

    @Override
    public GUIClickableItem createItemFor(Map.Entry<ItemType, Integer> entry, int slot, HypixelPlayer p) {
        ItemType itemType = entry.getKey();
        int amount = entry.getValue();

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                e.setCancelled(true);

                int currentAmount = player.getStash().getMaterialStash().getOrDefault(itemType, 0);
                if (currentAmount == 0) {
                    // Material no longer in stash
                    new GUIStashMaterial().open(player);
                    return;
                }

                int maxStackSize = itemType.material.maxStackSize();
                int amountToPickup = Math.min(currentAmount, maxStackSize);

                // Check if player has inventory space
                if (!player.hasEmptySlots(1)) {
                    player.sendMessage("§cCouldn't unstash your material stash! Your inventory is full!");
                    return;
                }

                // Remove from stash and add to inventory
                int removed = player.getStash().removeFromMaterialStash(itemType, amountToPickup);
                if (removed > 0) {
                    SkyBlockItem newItem = new SkyBlockItem(itemType);
                    newItem.setAmount(removed);
                    player.addAndUpdateItem(newItem);
                    player.sendMessage("§aFrom stash: §f" + itemType.getDisplayName() + " §7x" + removed);

                    // Check if stash is now empty
                    if (player.getStash().getMaterialStashCount() == 0) {
                        player.sendMessage("§aYou picked up all items from your material stash!");
                    } else {
                        player.sendMessage("§eYou still have §c" + player.getStash().getMaterialStashCount() +
                                " §ematerials totalling §c" + player.getStash().getMaterialTypeCount() +
                                " §etypes of materials in there!");
                    }
                }

                // Refresh the GUI
                new GUIStashMaterial().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                int currentAmount = player.getStash().getMaterialStash().getOrDefault(itemType, amount);

                SkyBlockItem item = new SkyBlockItem(itemType);
                item.setAmount(Math.min(currentAmount, 64));
                ItemStack.Builder stack = new NonPlayerItemUpdater(item.getItemStack()).getUpdatedItem();
                ArrayList<String> lore = new ArrayList<>(item.getLore());
                lore.add("");
                lore.add("§7Amount: §e" + StringUtility.commaify(currentAmount));
                lore.add("");
                lore.add("§eClick to pickup!");
                return ItemStackCreator.updateLore(stack, lore);
            }
        };
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        // No special handling needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}

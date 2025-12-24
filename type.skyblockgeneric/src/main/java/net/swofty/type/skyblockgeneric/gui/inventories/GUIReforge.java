package net.swofty.type.skyblockgeneric.gui.inventories;

import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.item.reforge.Reforge;
import net.swofty.commons.skyblock.item.reforge.ReforgeLoader;
import net.swofty.commons.skyblock.item.reforge.ReforgeType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.ReforgableComponent;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIReforge extends HypixelInventoryGUI {
    private static final Map<Rarity, Integer> COST_MAP = new HashMap<>();
    private final int[] borderSlots = {
            0, 8, 9, 17, 18, 26, 27, 35, 36, 44
    };

    static {
        COST_MAP.put(Rarity.COMMON, 250);
        COST_MAP.put(Rarity.UNCOMMON, 500);
        COST_MAP.put(Rarity.RARE, 1000);
        COST_MAP.put(Rarity.EPIC, 2500);
        COST_MAP.put(Rarity.LEGENDARY, 5000);
        COST_MAP.put(Rarity.MYTHIC, 10000);
    }

    public GUIReforge() {
        super("Reforge Item", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(40));

        updateFromItem(null);
    }

    public void updateFromItem(SkyBlockItem item) {
        border(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE));

        if (item == null) {
            set(new GUIClickableItem(13) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    ItemStack stack = player.getInventory().getCursorItem();

                    if (stack.get(DataComponents.CUSTOM_NAME) == null) return;

                    e.setCancelled(true);
                    SkyBlockItem item = new SkyBlockItem(stack);
                    player.getInventory().setCursorItem(ItemStack.AIR);
                    updateFromItem(item);
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStack.builder(Material.AIR);
                }
            });
            set(new GUIClickableItem(22) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    player.sendMessage("§cPlace an item in the empty slot above to reforge it!");
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack(
                            "§eReforge Item", Material.ANVIL, 1,
                            "§7Place an item above to reforge it!",
                            "§7Reforging items adds a random",
                            "§7modifier to the item that grants stat",
                            "§7boosts."
                    );
                }
            });
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStack stack = player.getInventory().getCursorItem();

                if (stack == ItemStack.AIR) {
                    e.setCancelled(true);
                    player.getInventory().setCursorItem(PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build());
                    updateFromItem(null);
                }
            }

            @Override
            public boolean canPickup() {
                return true;
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
            }
        });

        if (item.getAmount() > 1 || !(item.hasComponent(ReforgableComponent.class))) {
            set(new GUIItem(22) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack(
                            "§cError!", Material.BARRIER, 1,
                            "§7You cannot reforge this item!"
                    );
                }
            });
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        // Check if there are valid reforges for this item type
        ReforgeType reforgeType = item.getComponent(ReforgableComponent.class).getReforgeType();
        List<Reforge> validReforges = ReforgeLoader.getReforgesForType(reforgeType);

        if (validReforges.isEmpty()) {
            set(new GUIItem(22) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack(
                            "§cError!", Material.BARRIER, 1,
                            "§7No reforges available for this item type!"
                    );
                }
            });
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        border(ItemStackCreator.createNamedItemStack(Material.LIME_STAINED_GLASS_PANE));
        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                int cost = COST_MAP.get(item.getAttributeHandler().getRarity());

                if (player.getCoins() - cost < 0) {
                    player.sendMessage("§cYou don't have enough Coins!");
                    return;
                }

                player.removeCoins(cost);

                ReforgeType itemReforgeType = item.getComponent(ReforgableComponent.class).getReforgeType();
                List<Reforge> availableReforges = ReforgeLoader.getReforgesForType(itemReforgeType);

                if (availableReforges.isEmpty()) {
                    player.sendMessage("§cNo reforges available for this item!");
                    return;
                }

                Reforge selectedReforge = availableReforges.get(MathUtility.random(0, availableReforges.size() - 1));

                // Get old reforge info for the message
                Reforge oldReforge = item.getAttributeHandler().getReforge();
                String oldPrefix = (oldReforge != null) ? " " + oldReforge.getPrefix() : "";

                try {
                    item.getAttributeHandler().setReforge(selectedReforge);
                } catch (IllegalArgumentException ex) {
                    player.sendMessage("§c" + ex.getMessage());
                    return;
                }

                String itemName = StringUtility.toNormalCase(item.getAttributeHandler().getTypeAsString());

                player.sendMessage("§aYou reforged your" +
                        item.getAttributeHandler().getRarity().getColor() + oldPrefix + " " + itemName + "§a into a " +
                        item.getAttributeHandler().getRarity().getColor() + selectedReforge.getPrefix() + " " + itemName + "§a!");

                updateFromItem(item);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(
                        "§eReforge Item", Material.ANVIL, 1,
                        "§7Reforges the above item, giving it a",
                        "§7random stat modifier that boosts its",
                        "§7stats.",
                        "§2 ",
                        "§7Cost",
                        "§6" + COST_MAP.get(item.getAttributeHandler().getRarity()) + " Coins",
                        "§2 ",
                        "§eClick to reforge!"
                );
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        ((SkyBlockPlayer) e.getPlayer()).addAndUpdateItem(new SkyBlockItem(e.getInventory().getItemStack(13)));
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        player.addAndUpdateItem(new SkyBlockItem(inventory.getItemStack(13)));
    }

    @Override
    public void border(ItemStack.Builder stack) {
        for (int i : borderSlots) {
            set(i, stack);
        }
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
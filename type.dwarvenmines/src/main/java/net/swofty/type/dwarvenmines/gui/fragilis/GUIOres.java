package net.swofty.type.dwarvenmines.gui.fragilis;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

// TODO: hardcoded
public class GUIOres extends HypixelInventoryGUI {

    public GUIOres() {
        super("Ores", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        border(FILLER_ITEM);
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Ores",
                        Material.BOOK,
                        1,
                        "§8Block Classification",
                        "",
                        "§7These blocks are affected by: ",
                        "§8 - §6☘ Ore Fortune",
                        "§8 - §6☘ Mining Fortune",
                        "§8 - §e▚ Mining Spread"
                );
            }
        });
        set(new GUIItem(10) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Coal Ore",
                        Material.COAL_ORE,
                        1,
                        "§8Ore"
                );
            }
        });
        set(new GUIItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Iron Ore",
                        Material.IRON_ORE,
                        1,
                        "§8Ore"
                );
            }
        });
        set(new GUIItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Gold Ore",
                        Material.GOLD_ORE,
                        1,
                        "§8Ore"
                );
            }
        });
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Lapis Lazuli Ore",
                        Material.LAPIS_ORE,
                        1,
                        "§8Ore"
                );
            }
        });
        set(new GUIItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Redstone Ore",
                        Material.REDSTONE_ORE,
                        1,
                        "§8Ore"
                );
            }
        });
        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Emerald Ore",
                        Material.EMERALD_ORE,
                        1,
                        "§8Ore"
                );
            }
        });
        set(new GUIItem(16) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Diamond Ore",
                        Material.DIAMOND_ORE,
                        1,
                        "§8Ore"
                );
            }
        });
        set(new GUIItem(19) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Pure Coal",
                        Material.COAL_BLOCK,
                        1,
                        "§8Ore",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §23",
                        "§7 Block Strength: §e600"
                );
            }
        });
        set(new GUIItem(20) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Pure Iron",
                        Material.IRON_BLOCK,
                        1,
                        "§8Ore",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §23",
                        "§7 Block Strength: §e600"
                );
            }
        });
        set(new GUIItem(21) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Pure Gold",
                        Material.GOLD_BLOCK,
                        1,
                        "§8Ore",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §23",
                        "§7 Block Strength: §e600"
                );
            }
        });
        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Pure Lapis",
                        Material.LAPIS_BLOCK,
                        1,
                        "§8Ore",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §23",
                        "§7 Block Strength: §e600"
                );
            }
        });
        set(new GUIItem(23) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Pure Redstone",
                        Material.REDSTONE_BLOCK,
                        1,
                        "§8Ore",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §23",
                        "§7 Block Strength: §e600"
                );
            }
        });
        set(new GUIItem(24) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Pure Emerald",
                        Material.EMERALD_BLOCK,
                        1,
                        "§8Ore",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §23",
                        "§7 Block Strength: §e600"
                );
            }
        });
        set(new GUIItem(25) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Pure Diamond",
                        Material.DIAMOND_BLOCK,
                        1,
                        "§8Ore",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §23",
                        "§7 Block Strength: §e600"
                );
            }
        });
        set(new GUIItem(28) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Nether Quartz Ore",
                        Material.NETHER_QUARTZ_ORE,
                        1,
                        "§8Ore"
                );
            }
        });
        set(new GUIItem(29) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Pure Quartz",
                        Material.QUARTZ_BLOCK,
                        1,
                        "§8Ore",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §23",
                        "§7 Block Strength: §e600"
                );
            }
        });
        set(new GUIItem(30) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Sulphur Ore",
                        Material.SPONGE,
                        1,
                        "§8Ore",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §28",
                        "§7 Block Strength: §e500"
                );
            }
        });
        set(GUIClickableItem.getGoBackItem(48, new GUIHandyBlockGuide()));
        set(GUIClickableItem.getCloseItem(49));
        updateItemStacks(getInventory(), getPlayer());
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

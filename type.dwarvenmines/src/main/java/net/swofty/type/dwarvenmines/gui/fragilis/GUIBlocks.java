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
public class GUIBlocks extends HypixelInventoryGUI {

    public GUIBlocks() {
        super("Blocks", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        border(FILLER_ITEM);
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Blocks",
                        Material.BOOK,
                        1,
                        "§8Block Classification",
                        "",
                        "§7These blocks are affected by: ",
                        "§8 - §6☘ Block Fortune",
                        "§8 - §6☘ Mining Fortune",
                        "§8 - §e▚ Mining Spread"
                );
            }
        });
        set(new GUIItem(10) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Cobblestone",
                        Material.COBBLESTONE,
                        1,
                        "§8Block"
                );
            }
        });
        set(new GUIItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Stone",
                        Material.STONE,
                        1,
                        "§8Block"
                );
            }
        });
        set(new GUIItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Sand",
                        Material.SAND,
                        1,
                        "§8Block"
                );
            }
        });
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Gravel",
                        Material.GRAVEL,
                        1,
                        "§8Block"
                );
            }
        });
        set(new GUIItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Ice",
                        Material.ICE,
                        1,
                        "§8Block"
                );
            }
        });
        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9End Stone",
                        Material.END_STONE,
                        1,
                        "§8Block"
                );
            }
        });
        set(new GUIItem(16) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Obsidian",
                        Material.OBSIDIAN,
                        1,
                        "§8Block"
                );
            }
        });
        set(new GUIItem(19) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Netherrack",
                        Material.NETHERRACK,
                        1,
                        "§8Block"
                );
            }
        });
        set(new GUIItem(20) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Glowstone",
                        Material.GLOWSTONE,
                        1,
                        "§8Block"
                );
            }
        });
        set(new GUIItem(21) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Red Sand",
                        Material.RED_SAND,
                        1,
                        "§8Block"
                );
            }
        });
        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Mycelium",
                        Material.MYCELIUM,
                        1,
                        "§8Block"
                );
            }
        });
        set(new GUIItem(23) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Hard Stone",
                        Material.STONE,
                        1,
                        "§8Block",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §24",
                        "§7 Block Strength: §e50"
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

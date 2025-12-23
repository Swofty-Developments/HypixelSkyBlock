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
public class GUIDwarvenMetals extends HypixelInventoryGUI {

    public GUIDwarvenMetals() {
        super("Dwarven Metals", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        border(FILLER_ITEM);
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aDwarven Metals",
                        Material.BOOK,
                        1,
                        "§8Block Classification",
                        "",
                        "§7These blocks are affected by: ",
                        "§8 - §6☘ Dwarven Metal Fortune",
                        "§8 - §6☘ Mining Fortune",
                        "§8 - §e▚ Mining Spread"
                );
            }
        });
        set(new GUIItem(10) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMithril Ore",
                        Material.PRISMARINE_BRICKS,
                        1,
                        "§8Dwarven Metal",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §24",
                        "§7 Block Strength: §e800"
                );
            }
        });
        set(new GUIItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aTitanium Ore",
                        Material.POLISHED_DIORITE,
                        1,
                        "§8Dwarven Metal",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §25",
                        "§7 Block Strength: §e2,000"
                );
            }
        });
        set(new GUIItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aUmber",
                        Material.RED_SANDSTONE,
                        1,
                        "§8Dwarven Metal",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §29",
                        "§7 Block Strength: §e5,600"
                );
            }
        });
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aTungsten",
                        Material.CLAY,
                        1,
                        "§8Dwarven Metal",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §29",
                        "§7 Block Strength: §e5,600"
                );
            }
        });
        set(new GUIItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aGlacite",
                        Material.PACKED_ICE,
                        1,
                        "§8Dwarven Metal",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §29",
                        "§7 Block Strength: §e6,000"
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

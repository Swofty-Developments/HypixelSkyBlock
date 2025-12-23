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

public class GUIHandyBlockGuide extends HypixelInventoryGUI {

    public GUIHandyBlockGuide() {
        super("Handy Block Guide", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§2Handy Block Guide",
                        Material.BOOK,
                        1,
                        "§7View a list of important mineable blocks:",
                        "§8 - §6Ores",
                        "§8 - §9Blocks",
                        "§8 - §aDwarven Metals",
                        "§8 - §dGemstones"
                );
            }
        });
        set(new GUIClickableItem(19) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIOres().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Ores",
                        Material.GOLD_ORE,
                        1,
                        "§8Block Classification",
                        "",
                        "§7These blocks are affected by: ",
                        "§8 - §6☘ Ore Fortune",
                        "§8 - §6☘ Mining Fortune",
                        "§8 - §e▚ Mining Spread",
                        "",
                        "§eClick to view!"
                );
            }
        });
        set(new GUIClickableItem(21) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIBlocks().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§9Blocks",
                        Material.COBBLESTONE,
                        1,
                        "§8Block Classification",
                        "",
                        "§7These blocks are affected by: ",
                        "§8 - §6☘ Block Fortune",
                        "§8 - §6☘ Mining Fortune",
                        "§8 - §e▚ Mining Spread",
                        "",
                        "§eClick to view!"
                );
            }
        });
        set(new GUIClickableItem(23) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIDwarvenMetals().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aDwarven Metals",
                        Material.PRISMARINE,
                        1,
                        "§8Block Classification",
                        "",
                        "§7These blocks are affected by: ",
                        "§8 - §6☘ Dwarven Metal Fortune",
                        "§8 - §6☘ Mining Fortune",
                        "§8 - §e▚ Mining Spread",
                        "",
                        "§eClick to view!"
                );
            }
        });
        set(new GUIClickableItem(25) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIGemstones().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dGemstones",
                        Material.RED_STAINED_GLASS,
                        1,
                        "§8Block Classification",
                        "",
                        "§7These blocks are affected by: ",
                        "§8 - §6☘ Gemstone Fortune",
                        "§8 - §6☘ Mining Fortune",
                        "§8 - §e▚ Gemstone Spread",
                        "§8 - §5✧ Pristine",
                        "",
                        "§eClick to view!"
                );
            }
        });
        set(GUIClickableItem.getCloseItem(40));
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

package net.swofty.type.hub.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

// TOOD: Fishing ship name can be changed. And the parts can be changed
public class GUIGFishingShip extends HypixelInventoryGUI {

    public GUIGFishingShip() {
        super("{Fishing Ship}", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6{Fishing Ship}",
                        Material.OAK_BOAT,
                        1,
                        "§7Your §6Ship §7will help you travel to",
                        "§7different §9fishing islands §7in SkyBlock.",
                        "",
                        "§7For now, it can only get you to the",
                        "§2Backwater Bayou§7.",
                        "",
                        "§7Helm: §fCracked Ship Helm",
                        "§7Engine: §fRusty Ship Engine",
                        "§7Hull: §fRusty Ship Hull"
                );
            }
        });
        set(new GUIItem(21) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§fCracked Ship Helm",
                        "d8d4a54d1fcf47b2efc99ba4cc772250aee5c2f26ed1a19052213e0f3323ca1d",
                        1,
                        "§7A cracked ship helm, incapable of",
                        "§7changing its heading which appears",
                        "§7due east.",
                        "",
                        "§6§lUPGRADE TO §8➜ §9Bronze Ship Helm",
                        "§7Crafted from §aBronze Bowls§7, which",
                        "§7are rarely dropped by §cDumpster",
                        "§cDivers §7in the §2Backwater Bayou§7.",
                        "",
                        "§eClick a Ship Part in your inventory to",
                        "§eupgrade this part!"
                );
            }
        });
        // TODO: this is "missing" by default, need to implement that state
        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§fRusty Ship Engine",
                        "53e84793917c890f7f8a2c4078a29e8ba939790498727af9342c2b6f6ac43c9c",
                        1,
                        "§7Rusted by the waters, but it seems to",
                        "§7be able to run...for now.",
                        "",
                        "§6§lUPGRADE TO §8➜ §9Bronze Ship Engine",
                        "§7Purchased from §2Junker Joel §7in the",
                        "§2Backwater Bayou§7.",
                        "",
                        "§eClick a Ship Part in your inventory to",
                        "§eupgrade this part!"
                );
            }
        });
        set(new GUIItem(23) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        "§fRusty Ship Hull",
                        "f42d53ca6e7d80a99a699c2036dcf6e233394feb9f46fb2ff9d9a819690894a9",
                        1,
                        "§7A hull rusted and dilapidated beyond",
                        "§7repair. It's a miracle the ship",
                        "§7remains afloat.",
                        "",
                        "§6§lUPGRADE TO §8➜ §9Bronze Ship Hull",
                        "§7Crafted from §aTorn Cloth§7, which is",
                        "§7rarely dropped by §cBanshees §7in the",
                        "§2Backwater Bayou§7.",
                        "",
                        "§eClick a Ship Part in your inventory to",
                        "§eupgrade this part!"
                );
            }
        });
        set(new GUIItem(44) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aRename Ship",
                        Material.NAME_TAG,
                        1,
                        "§7You may be going on long voyages",
                        "§7with your §6Ship§7, best to give it a name!",
                        "",
                        "§7Current Name: §6Zephyr",
                        "",
                        "§eClick to rename!"
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

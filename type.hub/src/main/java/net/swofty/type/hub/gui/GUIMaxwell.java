package net.swofty.type.hub.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bags.GUIAccessoryBag;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIMaxwell extends HypixelInventoryGUI {

    public GUIMaxwell() {
        super("Accessory Bag Thaumaturgy", InventoryType.CHEST_6_ROW);
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem(47) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                p.openView(new GUIAccessoryBag());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStackHead("§aAccessory Bag Shortcut", "961a918c0c49ba8d053e522cb91abc74689367b4d8aa06bfc1ba9154730985ff", 1,
                        "§7Quickly access your accessory bag",
                        "§7from right here!",
                        "",
                        "§eClick to open!");
            }
        });
        set(new GUIItem(48) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                int mythic = player.getAccessoryBag().getUniqueAccessories(Rarity.MYTHIC).size();
                int legendary = player.getAccessoryBag().getUniqueAccessories(Rarity.LEGENDARY).size();
                int epic = player.getAccessoryBag().getUniqueAccessories(Rarity.EPIC).size();
                int rare = player.getAccessoryBag().getUniqueAccessories(Rarity.RARE).size();
                int uncommon = player.getAccessoryBag().getUniqueAccessories(Rarity.UNCOMMON).size();
                int common = player.getAccessoryBag().getUniqueAccessories(Rarity.COMMON).size();
                int special = player.getAccessoryBag().getUniqueAccessories(Rarity.SPECIAL).size();
                int verySpecial = player.getAccessoryBag().getUniqueAccessories(Rarity.VERY_SPECIAL).size();

                return ItemStackCreator.getStack("§aAccessories Breakdown", Material.FILLED_MAP, 1,
                        "§8From your bag",
                        "",
                        "§622 MP §7x §d" + mythic + " Accs. §7= §6" + mythic*22 + " MP",
                        "§616 MP §7x §6" + legendary + " Accs. §7= §6" + legendary*16 + " MP",
                        "§612 MP §7x §5" + epic + " Accs. §7= §6" + epic*12 + " MP",
                        "§68 MP §7x §9" + rare + " Accs. §7= §6" + rare*8 + " MP",
                        "§65 MP §7x §a" + uncommon + " Accs. §7= §6" + uncommon*5 + " MP",
                        "§63 MP §7x §f" + common + " Accs. §7= §6" + common*3 + " MP",
                        "§63 MP §7x §c" + special + " Accs. §7= §6" + special*3 + " MP",
                        "§65 MP §7x §c" + verySpecial + " Accs. §7= §6" + verySpecial*5 + " MP",
                        "",
                        "§7Total: §6" + StringUtility.commaify(player.getMagicalPower()) + " Magical Power");
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
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}

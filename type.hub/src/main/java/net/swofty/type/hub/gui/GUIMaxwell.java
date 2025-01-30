package net.swofty.type.hub.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.Rarity;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags.GUIAccessoryBag;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIMaxwell extends SkyBlockAbstractInventory {

    public GUIMaxwell() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Accessory Bag Thaumaturgy")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Accessory Bag Shortcut
        attachItem(GUIItem.builder(47)
                .item(ItemStackCreator.getStackHead("§aAccessory Bag Shortcut",
                        "961a918c0c49ba8d053e522cb91abc74689367b4d8aa06bfc1ba9154730985ff", 1,
                        "§7Quickly access your accessory bag",
                        "§7from right here!",
                        "",
                        "§eClick to open!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIAccessoryBag());
                    return true;
                })
                .build());

        // Accessories Breakdown
        attachItem(GUIItem.builder(48)
                .item(() -> {
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
                            "§7Total: §6" + StringUtility.commaify(player.getMagicalPower()) + " Magical Power").build();
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No special cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed
    }
}
package net.swofty.type.hub.gui.rosetta;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIRosetta extends HypixelInventoryGUI {
    public GUIRosetta() {
        super("Rosetta's Starter Gear", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem(19) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.openView(new GUIRosettaIronArmor());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§eIron Armor", Material.IRON_HELMET, 1,
                        "§7Plain old iron armor.",
                        "",
                        "§eClick to view set!");
            }
        });

        set(new GUIClickableItem(21) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.openView(new GUIRosettaArmor());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§eRosetta's Armor", Material.DIAMOND_HELMET, 1,
                        "§7Custom-designed and",
                        "§7hand-crafted diamond armor.",
                        "",
                        "§eClick to view set!");
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.openView(new GUISquireArmor());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§eSquire Armor", Material.CHAINMAIL_HELMET, 1,
                        "§7Solid set to venture into the",
                        "§7deep caverns.",
                        "",
                        "§eClick to view set!");
            }
        });

        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.openView(new GUIMercenaryArmor());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§eMercenary Armor", Material.IRON_HELMET, 1,
                        "§7Kickstart your warrior",
                        "§7journey!",
                        "",
                        "§eClick to view set!");
            }
        });

        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.openView(new GUICelesteArmor());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§eCeleste Armor", Material.LEATHER_HELMET, 1,
                        "§7Dip a toe into the world of",
                        "§7magic.",
                        "",
                        "§eClick to view set!");
            }
        });

        set(new GUIClickableItem(34) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.openView(new GUIStarlightArmor());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStackCreator.getStack("§eStarlight Armor", Material.GOLDEN_HELMET, 1,
                        "§7This set was designed with the",
                        "§7help of Barry the Wizard.",
                        "",
                        "§eClick to view set!");
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}

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
public class GUIGemstones extends HypixelInventoryGUI {

    public GUIGemstones() {
        super("Gemstones", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        border(FILLER_ITEM);
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dGemstones",
                        Material.BOOK,
                        1,
                        "§8Block Classification",
                        "",
                        "§7These blocks are affected by: ",
                        "§8 - §6☘ Gemstone Fortune",
                        "§8 - §6☘ Mining Fortune",
                        "§8 - §e▚ Gemstone Spread",
                        "§8 - §5✧ Pristine"
                );
            }
        });
        set(new GUIItem(10) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dRuby Gemstone",
                        Material.RED_STAINED_GLASS,
                        1,
                        "§8Gemstone",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §26",
                        "§7 Block Strength: §e2,300"
                );
            }
        });
        set(new GUIItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dAmber Gemstone",
                        Material.ORANGE_STAINED_GLASS,
                        1,
                        "§8Gemstone",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §27",
                        "§7 Block Strength: §e3,000"
                );
            }
        });
        set(new GUIItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dSapphire Gemstone",
                        Material.LIGHT_BLUE_STAINED_GLASS,
                        1,
                        "§8Gemstone",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §27",
                        "§7 Block Strength: §e3,000"
                );
            }
        });
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dJade Gemstone",
                        Material.LIME_STAINED_GLASS,
                        1,
                        "§8Gemstone",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §27",
                        "§7 Block Strength: §e3,000"
                );
            }
        });
        set(new GUIItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dAmethyst Gemstone",
                        Material.PURPLE_STAINED_GLASS,
                        1,
                        "§8Gemstone",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §27",
                        "§7 Block Strength: §e3,000"
                );
            }
        });
        set(new GUIItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dOpal Gemstone",
                        Material.WHITE_STAINED_GLASS,
                        1,
                        "§8Gemstone",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §27",
                        "§7 Block Strength: §e3,000"
                );
            }
        });
        set(new GUIItem(16) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dTopaz Gemstone",
                        Material.YELLOW_STAINED_GLASS,
                        1,
                        "§8Gemstone",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §28",
                        "§7 Block Strength: §e3,800"
                );
            }
        });
        set(new GUIItem(19) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dJasper Gemstone",
                        Material.MAGENTA_STAINED_GLASS,
                        1,
                        "§8Gemstone",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §29",
                        "§7 Block Strength: §e4,800"
                );
            }
        });
        set(new GUIItem(21) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dAquamarine Gemstone",
                        Material.BLUE_STAINED_GLASS,
                        1,
                        "§8Gemstone",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §29",
                        "§7 Block Strength: §e5,200"
                );
            }
        });
        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dCitrine Gemstone",
                        Material.BROWN_STAINED_GLASS,
                        1,
                        "§8Gemstone",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §29",
                        "§7 Block Strength: §e5,200"
                );
            }
        });
        set(new GUIItem(23) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§dPeridot Gemstone",
                        Material.GREEN_STAINED_GLASS,
                        1,
                        "§8Gemstone",
                        "",
                        "§7Properties",
                        "§7 Breaking Power: §29",
                        "§7 Block Strength: §e5,200"
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

package net.swofty.gui.inventory.inventories;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.gui.inventory.item.GUIItem;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.StringUtility;

public class GUIEnchantmentTable extends SkyBlockInventoryGUI {
    private Pos enchantmentTable;
    private int bookshelfPower;

    public GUIEnchantmentTable(Instance instance, Pos enchantmentTable) {
        super("Enchantment Table", InventoryType.CHEST_6_ROW);

        this.enchantmentTable = enchantmentTable;
        this.bookshelfPower = getBookshelfPower(instance, enchantmentTable);

        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 48;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack(
                        "§dBookshelf Power", Material.BOOKSHELF, (short) 0, 1,
                        "§7Stronger enchantments require",
                        "§7more bookshelf power which can",
                        "§7be increased by placing",
                        "§7bookshelves nearby.",
                        "§a ",
                        "§7Current Bookshelf Power:",
                        "§d" + bookshelfPower);
            }
        });
        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 50;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack(
                        "§aEnchantments Guide", Material.BOOK, (short) 0, 1,
                        "§7View a complete list of all",
                        "§7enchantments and their",
                        "§7requirements.",
                        "§a ",
                        "§eClick to view!");
            }
        });
        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 28;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack(
                        "§aEnchant Item", Material.ENCHANTING_TABLE, (short) 0, 1,
                        "§7Add and rmeove enchantments from",
                        "§7the time in the slot above!"
                );
            }
        });

        updateFromItem(null);
    }

    public void updateFromItem(SkyBlockItem item) {
        if (item == null) {
            set(new GUIItem() {
                @Override
                public int getSlot() {
                    return 23;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack(
                            "§cEnchant Item", Material.GRAY_DYE, (short) 0, 1,
                            "§7Place an item in the open slot",
                            "§7to enchant it!"
                    );
                }
            });
            set(new GUIClickableItem() {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    ItemStack stack = e.getCursorItem();

                    if (stack.getDisplayName() == null) return;

                    SkyBlockItem item = new SkyBlockItem(stack);
                    updateFromItem(item);
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public int getSlot() {
                    return 19;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStack.builder(Material.AIR);
                }
            });
            return;
        }

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                ItemStack stack = e.getCursorItem();

                if (stack.getDisplayName() == null) {
                    updateFromItem(null);
                    return;
                }

                SkyBlockItem item = new SkyBlockItem(stack);
                updateFromItem(item);
            }

            @Override
            public boolean canPickup() {
                return true;
            }

            @Override
            public int getSlot() {
                return 19;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return item.getItemStackBuilder();
            }
        });
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        if (e == null) return;

        e.getPlayer().getInventory().addItemStack(e.getInventory().getItemStack(19));
    }

    @Override
    public void suddenlyQuit(SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }

    public static Integer getBookshelfPower(Instance instance, Pos pos) {
        int power = 0;

        for (int x = -2; x <= 2; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (StringUtility.getMaterialFromBlock(instance.getBlock(
                            pos.blockX() + x,
                            pos.blockY() + y,
                            pos.blockZ() + z)) == Material.BOOKSHELF) {
                        power++;
                    }
                }
            }
        }
        if (power > 60) {
            return 60;
        }
        return power;
    }
}

package net.swofty.gui.inventory.inventories;

import lombok.SneakyThrows;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.enchantment.SkyBlockEnchantment;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.gui.inventory.item.GUIItem;
import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.attribute.AttributeHandler;
import net.swofty.item.impl.Enchantable;
import net.swofty.item.updater.NonPlayerItemUpdater;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.ItemGroups;
import net.swofty.utility.StringUtility;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GUIEnchantmentTable extends SkyBlockInventoryGUI {
    private static final int[] PAGINATED_SLOTS = new int[]{
            12, 13, 14, 15, 16,
            21, 22, 23, 24, 25,
            30, 31, 32, 33, 34,
    };

    private final int bookshelfPower;

    public GUIEnchantmentTable(Instance instance, Pos enchantmentTable) {
        super("Enchantment Table", InventoryType.CHEST_6_ROW);

        this.bookshelfPower = getBookshelfPower(instance, enchantmentTable);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
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
                        "§7Add and remove enchantments from",
                        "§7the time in the slot above!"
                );
            }
        });

        updateFromItem(null, null);
    }

    @SneakyThrows
    public void updateFromItem(SkyBlockItem item, SkyBlockEnchantment.EnchantmentType selected) {
        setTitle("Enchant Item " + (selected == null ? "" : "-> " + StringUtility.toNormalCase(selected.name())));

        Arrays.stream(PAGINATED_SLOTS).forEach(slot -> set(slot, ItemStackCreator.createNamedItemStack(
                Material.BLACK_STAINED_GLASS_PANE, "§7 "
        )));

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
                    updateFromItem(item, null);
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
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                ItemStack stack = e.getCursorItem();

                if (stack.getDisplayName() == null) {
                    updateFromItem(null, null);
                    return;
                }

                SkyBlockItem item = new SkyBlockItem(stack);
                updateFromItem(item, null);
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
                return PlayerItemUpdater.playerUpdate(player, null, item.getItemStack());
            }
        });

        ItemType type = item.getAttributeHandler().getItemTypeAsType();
        if (item.getItemStack().getAmount() > 1 ||
                type == null ||
                !(type.clazz.newInstance() instanceof Enchantable)) {
            set(new GUIItem() {
                @Override
                public int getSlot() {
                    return 23;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack(
                            "§cInvalid Stack Size!", Material.RED_DYE, (short) 0, 1,
                            "§7You cannot enchant stacked items!"
                    );
                }
            });

            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        List<ItemGroups> itemGroups = ((Enchantable) type.clazz.newInstance()).getItemGroups();
        List<SkyBlockEnchantment.EnchantmentType> enchantments = Arrays.stream(SkyBlockEnchantment.EnchantmentType.values())
                .filter(enchantmentType -> enchantmentType.getGroups().stream().anyMatch(itemGroups::contains))
                .toList();

        if (enchantments.isEmpty()) {
            set(new GUIItem() {
                @Override
                public int getSlot() {
                    return 23;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack(
                            "§cCannot Enchant Item!", Material.RED_DYE, (short) 0, 1,
                            "§7This item cannot be enchanted!"
                    );
                }
            });
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        if (selected == null) {
            enchantments = enchantments.stream().limit(15).toList();
            int i = 0;
            for (SkyBlockEnchantment.EnchantmentType enchantmentType : enchantments) {
                int finalI = i;
                set(new GUIClickableItem() {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        if (bookshelfPower < enchantmentType.getRequiredBookshelfPower()) {
                            player.sendMessage("§cThis enchantment requires " + enchantmentType.getRequiredBookshelfPower() + " Bookshelf Power!");
                            return;
                        }

                        updateFromItem(item, enchantmentType);
                    }

                    @Override
                    public int getSlot() {
                        return PAGINATED_SLOTS[finalI];
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        AttributeHandler attributeHandler = item.getAttributeHandler();

                        List<String> lore = new ArrayList<>();
                        StringUtility.splitByWordAndLength(enchantmentType.getDescription(), 30, " ")
                                .forEach(line -> lore.add("§7" + line));
                        lore.add("§a ");

                        if (attributeHandler.hasEnchantment(enchantmentType)) {
                            lore.add("§a  " + StringUtility.toNormalCase(enchantmentType.name()) + " " +
                                    StringUtility.getAsRomanNumeral(attributeHandler.getEnchantment(enchantmentType).getLevel()));
                        } else {
                            lore.add("§c  " + StringUtility.toNormalCase(enchantmentType.name()) + " §l✖");
                        }

                        lore.add("§a ");
                        if (bookshelfPower < enchantmentType.getRequiredBookshelfPower()) {
                            lore.add("§cRequires " + enchantmentType.getRequiredBookshelfPower() + " Bookshelf Power!");
                        } else {
                            lore.add("§eClick to view!");
                        }

                        return ItemStackCreator.getStack(
                                "§a" + StringUtility.toNormalCase(enchantmentType.name()),
                                Material.ENCHANTED_BOOK, (short) 0, 1,
                                lore
                        );
                    }
                });
                i++;
            }
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        int supportedLevel = selected.getSources().stream()
                .filter(source -> source.sourceType() == SkyBlockEnchantment.EnchantmentType.SourceType.ENCHANTMENT_TABLE)
                .mapToInt(SkyBlockEnchantment.EnchantmentType.EnchantmentSource::maxLevel)
                .max()
                .orElse(0);

        for (int level = 0; level < supportedLevel; level++) {
            int finalLevel = level;
            set(new GUIClickableItem() {

                @Override
                public int getSlot() {
                    return PAGINATED_SLOTS[finalLevel];
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack(
                            "§aLevel " + (finalLevel + 1), Material.ENCHANTED_BOOK, (short) 0, 1,
                            "§7Click to enchant this item with",
                            "§7" + StringUtility.toNormalCase(selected.name()) + " " + StringUtility.getAsRomanNumeral(finalLevel + 1)
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    player.sendMessage(String.valueOf(player.getLevel()));
                    player.sendMessage(String.valueOf(player.getExp()));
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        e.getPlayer().getInventory().addItemStack(e.getInventory().getItemStack(19));
    }

    @Override
    public void suddenlyQuit(SkyBlockPlayer player) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {}

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

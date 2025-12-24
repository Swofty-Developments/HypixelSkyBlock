package net.swofty.type.skyblockgeneric.gui.inventories;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.enchantment.EnchantmentSource;
import net.swofty.type.skyblockgeneric.enchantment.EnchantmentType;
import net.swofty.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import net.swofty.type.skyblockgeneric.enchantment.abstr.ConflictingEnch;
import net.swofty.type.skyblockgeneric.item.ItemAttributeHandler;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.EnchantableComponent;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIEnchantmentTable extends HypixelInventoryGUI {
    private static final int[] PAGINATED_SLOTS_LIST_ENCHANTS = new int[]{
            12, 13, 14, 15, 16,
            21, 22, 23, 24, 25,
            30, 31, 32, 33, 34,
    };
    private static final int[] PAGINATED_SLOTS_LIST_LEVELS = new int[]{
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

        set(new GUIItem(48) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(
                        "§dBookshelf Power", Material.BOOKSHELF, 1,
                        "§7Stronger enchantments require",
                        "§7more bookshelf power which can",
                        "§7be increased by placing",
                        "§7bookshelves nearby.",
                        "§a ",
                        "§7Current Bookshelf Power:",
                        "§d" + bookshelfPower);
            }
        });

        set(new GUIItem(50) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(
                        "§aEnchantments Guide", Material.BOOK, 1,
                        "§7View a complete list of all",
                        "§7enchantments and their",
                        "§7requirements.",
                        "§a ",
                        "§eClick to view!");
            }
        });

        set(new GUIItem(28) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack(
                        "§aEnchant Item", Material.ENCHANTING_TABLE, 1,
                        "§7Add and remove enchantments from",
                        "§7the time in the slot above!"
                );
            }
        });

        updateFromItem(null, null);
    }

    @SneakyThrows
    public void updateFromItem(SkyBlockItem item, EnchantmentType selected) {
        getInventory().setTitle(Component.text(
                "Enchant Item " + (selected == null ? "" : "-> " + StringUtility.toNormalCase(selected.name())))
        );

        Arrays.stream(PAGINATED_SLOTS_LIST_ENCHANTS).forEach(slot -> set(slot, ItemStackCreator.createNamedItemStack(
                Material.BLACK_STAINED_GLASS_PANE, "§7 "
        )));
        set(45, ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "§7 "));

        if (item == null) {
            set(new GUIItem(23) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack(
                            "§cEnchant Item", Material.GRAY_DYE, 1,
                            "§7Place an item in the open slot",
                            "§7to enchant it!"
                    );
                }
            });

            set(new GUIClickableItem(19) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    ItemStack stack = player.getInventory().getCursorItem();

                    if (stack.get(DataComponents.CUSTOM_NAME) == null) return;

                    e.setCancelled(true);
                    SkyBlockItem item = new SkyBlockItem(stack);
                    player.getInventory().setCursorItem(ItemStack.AIR);
                    updateFromItem(item, null);
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    return ItemStack.builder(Material.AIR);
                }
            });

            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        set(new GUIClickableItem(19) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                ItemStack stack = player.getInventory().getCursorItem();

                if (stack == ItemStack.AIR) {
                    e.setCancelled(true);
                    player.getInventory().setCursorItem(PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build());
                    updateFromItem(null, null);
                }
            }

            @Override
            public boolean canPickup() {
                return true;
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return PlayerItemUpdater.playerUpdate(player, item.getItemStack());
            }
        });

        ItemType type = item.getAttributeHandler().getPotentialType();
        if (item.getItemStack().amount() > 1 || type == null || !(item.hasComponent(EnchantableComponent.class))) {
            set(new GUIItem(23) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack(
                            "§cInvalid Item!", Material.RED_DYE, 1,
                            "§7You cannot enchant stacked items!"
                    );
                }
            });

            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        List<EnchantItemGroups> enchantItemGroups = item.getComponent(EnchantableComponent.class).getEnchantItemGroups();
        List<EnchantmentType> enchantments = Arrays.stream(EnchantmentType.values())
                .filter(enchantmentType -> enchantmentType.getEnch().getGroups().stream().anyMatch(enchantItemGroups::contains))
                .filter(enchantmentType -> enchantmentType.getEnchFromTable() != null)
                .toList();

        if (enchantments.isEmpty()) {
            set(new GUIItem(23) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack(
                            "§cCannot Enchant Item!", Material.RED_DYE, 1,
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
            for (EnchantmentType enchantmentType : enchantments) {
                assert enchantmentType.getEnchFromTable() != null;
                int finalI = i;
                set(new GUIClickableItem(PAGINATED_SLOTS_LIST_ENCHANTS[finalI]) {
                    @Override
                    public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        if (bookshelfPower < enchantmentType.getEnchFromTable().getRequiredBookshelfPower()) {
                            player.sendMessage("§cThis enchantment requires " +
                                    enchantmentType.getEnchFromTable().getRequiredBookshelfPower() + " Bookshelf Power!");
                            return;
                        }

                        updateFromItem(item, enchantmentType);
                    }

                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer p) {
                        SkyBlockPlayer player = (SkyBlockPlayer) p;
                        ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();

                        List<String> lore = new ArrayList<>();
                        StringUtility.splitByWordAndLength(enchantmentType.getDescription(1, player), 30)
                                .forEach(line -> lore.add("§7" + line));
                        lore.add("§a ");

                        if (itemAttributeHandler.hasEnchantment(enchantmentType)) {
                            lore.add("§a  " + StringUtility.toNormalCase(enchantmentType.name()) + " " +
                                    StringUtility.getAsRomanNumeral(itemAttributeHandler.getEnchantment(enchantmentType).level())
                                    + " §l✓");
                        } else {
                            lore.add("§c  " + StringUtility.toNormalCase(enchantmentType.name()) + " §l✖");
                        }

                        lore.add("§a ");

                        if (bookshelfPower < enchantmentType.getEnchFromTable().getRequiredBookshelfPower()) {
                            lore.add("§cRequires " + enchantmentType.getEnchFromTable().getRequiredBookshelfPower()
                                    + " Bookshelf Power!");
                        } else {
                            lore.add("§eClick to view!");
                        }

                        return ItemStackCreator.getStack(
                                "§a" + StringUtility.toNormalCase(enchantmentType.name()),
                                Material.ENCHANTED_BOOK, 1,
                                lore
                        );
                    }
                });
                i++;
            }
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        int minLevel = selected.getEnch().getSources((SkyBlockPlayer) getPlayer()).stream().filter(source ->
                        source.getSource().equals(EnchantmentSource.SourceType.ENCHANTMENT_TABLE.toString()))
                .mapToInt(value -> value.minLevel).findAny().orElse(0);
        int maxLevel = selected.getEnch().getSources((SkyBlockPlayer) getPlayer()).stream().filter(source ->
                        source.getSource().equals(EnchantmentSource.SourceType.ENCHANTMENT_TABLE.toString()))
                .mapToInt(value -> value.maxLevel).findAny().orElse(0);

        int hasLevel = 0;
        if (item.getAttributeHandler().hasEnchantment(selected)) {
            hasLevel = item.getAttributeHandler().getEnchantment(selected).level();
        }

        for (int level = minLevel; level <= maxLevel; level++) {
            int finalLevel = level;
            int finalHasLevel = hasLevel;
            set(new GUIClickableItem(PAGINATED_SLOTS_LIST_LEVELS[finalLevel - 1]) {

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    int levelCost = selected.getEnchFromTable().getLevelsFromTableToApply(player).get(finalLevel);
                    List<String> lore = new ArrayList<>();
                    StringUtility.splitByWordAndLength(selected.getDescription(finalLevel, player), 30)
                            .forEach(line -> lore.add("§7" + line));

                    lore.add("§a ");

                    if (selected.getEnch() instanceof ConflictingEnch conflictingEnch) {
                        for (EnchantmentType ench : conflictingEnch.getConflictingEnchantments()) {
                            if (item.getAttributeHandler().hasEnchantment(ench)) {
                                lore.add("§c§lWARNING: This will remove " + StringUtility.toNormalCase(ench.name()) + ".");
                                break;
                            }
                        }
                    }

                    if (finalHasLevel == finalLevel) {
                        lore.add("§cThis enchantment is already present");
                        lore.add("§cand can be removed.");
                        lore.add("§a ");
                    }

                    lore.add("§7Cost");

                    if (finalHasLevel > finalLevel) {
                        if (levelCost > player.getLevel())
                            lore.add("§3" + levelCost + " Exp Levels §c§l✖");
                        else lore.add("§3" + levelCost + " Exp Levels §a§l✓");

                        lore.add("§a ");
                        lore.add("§cHigher level already present!");
                        return ItemStackCreator.getStack(
                                "§9" + selected.getName() + " " + StringUtility.getAsRomanNumeral(finalLevel),
                                Material.GRAY_DYE, 1,
                                lore
                        );
                    }

                    if (levelCost > player.getLevel()) {
                        lore.add("§3" + levelCost + " Exp Levels §c§l✖");
                        lore.add("§a ");
                        lore.add("§cYou have insufficient levels!");
                    } else {
                        lore.add("§3" + levelCost + " Exp Levels §a§l✓");
                        lore.add("§a ");
                        if (finalHasLevel >= finalLevel) {
                            lore.add("§eClick to remove!");
                        } else {
                            lore.add("§eClick to enchant!");
                        }
                    }

                    return ItemStackCreator.getStack(
                            "§9" + selected.getName() + " " + StringUtility.getAsRomanNumeral(finalLevel),
                            Material.ENCHANTED_BOOK, 1,
                            lore
                    );
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    if (e.getClickedItem().material() == Material.GRAY_DYE)
                        return;

                    // TODO if someone know how to get the itemstack name, do it
                    // I can't find it, I keep getting null...
                    // Because it should be, let's say, "§6Heroic Hyperion", and not "Hyperion"
                    String itemName = StringUtility.toNormalCase(type.name());

                    if (player.getLevel() < selected.getEnchFromTable().getLevelsFromTableToApply(player).get(finalLevel)) {
                        player.sendMessage("§cYou have insufficient levels!");
                        return;
                    }

                    item.getAttributeHandler().removeEnchantment(selected);
                    if (finalHasLevel < finalLevel) {
                        item.getAttributeHandler().addEnchantment(
                                new SkyBlockEnchantment(selected, finalLevel)
                        );

                        if (selected.getEnch() instanceof ConflictingEnch conflictingEnch) {
                            for (EnchantmentType enchant : conflictingEnch.getConflictingEnchantments()) {
                                System.out.printf("conflicting enchant: " + enchant.name());
                                if (item.getAttributeHandler().hasEnchantment(enchant)) item.getAttributeHandler().removeEnchantment(enchant);
                            }
                        }

                        player.setLevel(player.getLevel() - selected.getEnchFromTable().getLevelsFromTableToApply(player).get(finalLevel));
                        player.sendMessage("§aYou enchanted your " + itemName + " §awith " +
                                StringUtility.toNormalCase(selected.name()) + " " + StringUtility.getAsRomanNumeral(finalLevel) + "!");
                    } else {
                        int difference = finalHasLevel - finalLevel;

                        if (difference > 0) {
                            item.getAttributeHandler().addEnchantment(
                                    new SkyBlockEnchantment(selected, difference)
                            );
                        }

                        player.setLevel(player.getLevel() - selected.getEnchFromTable().getLevelsFromTableToApply(player).get(finalLevel));
                        player.sendMessage("§cYou removed " + StringUtility.toNormalCase(selected.name()) + " from your " + itemName + "§c!");
                    }

                    updateFromItem(item, selected);
                }
            });
        }

        set(new GUIClickableItem(45) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                updateFromItem(item, null);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.createNamedItemStack(
                        Material.ARROW, "§aGo Back"
                );
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
        ((SkyBlockPlayer) e.getPlayer()).addAndUpdateItem(new SkyBlockItem(e.getInventory().getItemStack(19)));
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        player.addAndUpdateItem(new SkyBlockItem(inventory.getItemStack(19)));
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
        return Math.min(power, 60);
    }
}

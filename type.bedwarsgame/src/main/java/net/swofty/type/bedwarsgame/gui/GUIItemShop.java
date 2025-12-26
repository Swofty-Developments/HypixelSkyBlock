package net.swofty.type.bedwarsgame.gui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.Game;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.shop.ShopManager;
import net.swofty.type.bedwarsgame.shop.UpgradeableItemTier;
import net.swofty.type.bedwarsgame.shop.UpgradeableShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.swofty.type.bedwarsgame.util.ComponentManipulator.noItalic;

public class GUIItemShop extends HypixelInventoryGUI {

    private static final ItemStack QUICK_BUY = ItemStack.builder(Material.NETHER_STAR)
            .customName(Component.text("Quick Buy").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.AQUA))
            .build();

    private static final ItemStack BLOCKS = ItemStack.builder(Material.TERRACOTTA)
            .customName(Component.text("Blocks").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
            .build();

    private static final ItemStack WEAPONS = ItemStack.builder(Material.GOLDEN_SWORD)
            .customName(Component.text("Weapons").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
            .build();

    private static final ItemStack ARMOR = ItemStack.builder(Material.CHAINMAIL_BOOTS)
            .customName(Component.text("Armor").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
            .build();

    private static final ItemStack TOOLS = ItemStack.builder(Material.STONE_PICKAXE)
            .customName(Component.text("Tools").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
            .build();

    private static final ItemStack BOWS = ItemStack.builder(Material.BOW)
            .customName(Component.text("Bows & Arrows").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
            .build();

    private static final ItemStack POTIONS = ItemStack.builder(Material.BREWING_STAND)
            .customName(Component.text("Potions").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
            .build();

    private static final ItemStack UTILITY = ItemStack.builder(Material.TNT)
            .customName(Component.text("Utility").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
            .build();

    private static final ItemStack ROTATING_ITEMS = ItemStack.builder(Material.REDSTONE_TORCH)
            .customName(Component.text("Rotating Items").decorationIfAbsent(ITALIC, TextDecoration.State.FALSE).color(NamedTextColor.GREEN))
            .build();
    private static final List<List<Material>> TIERED_ITEM_GROUPS = List.of(
            List.of(Material.LEATHER_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS)
    );
    private final ShopManager shopService = TypeBedWarsGameLoader.shopManager;
    private int currentPage = 0;
    private final Game game;

    public GUIItemShop(Game game) {
        super("Item Shop", InventoryType.CHEST_6_ROW);
        this.game = game;
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        updateGUI(e.player());
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    private void updateGUI(HypixelPlayer p) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 9, 17);
        set(currentPage + 9, ItemStackCreator.createNamedItemStack(Material.GREEN_STAINED_GLASS_PANE));
        set(new GUIClickableItem(0) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                currentPage = 0;
                updateGUI(player);
                playClickSound(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return convertToClickToView(QUICK_BUY, currentPage, 0);
            }
        });

        set(new GUIClickableItem(1) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                currentPage = 1;
                updateGUI(player);
                playClickSound(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return convertToClickToView(BLOCKS, currentPage, 1);
            }
        });

        set(new GUIClickableItem(2) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                currentPage = 2;
                updateGUI(player);
                playClickSound(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return convertToClickToView(WEAPONS, currentPage, 2);
            }
        });

        set(new GUIClickableItem(3) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                currentPage = 3;
                updateGUI(player);
                playClickSound(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return convertToClickToView(ARMOR, currentPage, 3);
            }
        });

        set(new GUIClickableItem(4) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                currentPage = 4;
                updateGUI(player);
                playClickSound(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return convertToClickToView(TOOLS, currentPage, 4);
            }
        });

        set(new GUIClickableItem(5) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                currentPage = 5;
                updateGUI(player);
                playClickSound(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return convertToClickToView(BOWS, currentPage, 5);
            }
        });

        set(new GUIClickableItem(6) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                currentPage = 6;
                updateGUI(player);
                playClickSound(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return convertToClickToView(POTIONS, currentPage, 6);
            }
        });

        set(new GUIClickableItem(7) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                currentPage = 7;
                updateGUI(player);
                playClickSound(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return convertToClickToView(UTILITY, currentPage, 7);
            }
        });

        set(new GUIClickableItem(8) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                currentPage = 8;
                updateGUI(player);
                playClickSound(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return convertToClickToView(ROTATING_ITEMS, currentPage, 8);
            }
        });

        populateShopItems(this, shopService, game, currentPage, null, this::updateGUI);
        updateItemStacks(getInventory(), getPlayer());
    }

    private ItemStack.Builder convertToClickToView(ItemStack itemStack, int currentPage, int index) {
        ItemStack.Builder builder = itemStack.builder();

        if (currentPage != index) {
            return builder.lore(noItalic(Component.text("Click to view!").color(NamedTextColor.YELLOW)));
        }

        return builder;
    }

    public static void populateShopItems(HypixelInventoryGUI gui, ShopManager shopService, Game game, @Nullable Integer currentPage, @Nullable ShopItem quickBuyEditor, Consumer<HypixelPlayer> update) {
        int[] shopSlots = {
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };

        for (int i = 0; i < shopSlots.length; i++) {
            int slot = shopSlots[i];
            int index = i;

            if (currentPage != null && currentPage == 8) {
                gui.set(new GUIItem(49) {

                    @Override
                    public ItemStack.Builder getItem(HypixelPlayer player) {
                        return ItemStackCreator.getStack("§aWhat are Rotating Items?", Material.PAPER, 1, List.of(
                                "§7Rotating Items are items that are",
                                "§7only available for a limited amount of",
                                "§7time. They may disappear and be",
                                "§7replaced with another temporary",
                                "§7item at any time."
                        ));
                    }
                });
            }

            gui.set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    BedWarsPlayer player = (BedWarsPlayer) p;
                    ShopItem shopItem;
                    if ((currentPage != null && currentPage == 0 ) || quickBuyEditor != null) {
                        shopItem = shopService.getQuickShopItem(player, index);
                    } else if (currentPage != null) {
                        shopItem = shopService.getShopItem(currentPage, index);
                    } else {
                        throw new IllegalStateException("Current page cannot be null when clicking shop items!");
                    }

                    if (quickBuyEditor != null) {
                        shopService.setQuickBuyItem(player, index, quickBuyEditor);
                        player.closeInventory();
                        new GUIItemShop(game).open(player);
                        player.sendMessage("§aAdded " + quickBuyEditor.getName() + " to Quick Buy!");
                        return;
                    }

                    if (shopItem == null) return;

                    Click click = e.getClick();
                    if (click instanceof Click.LeftShift || click instanceof Click.RightShift) {
                        boolean isInQuickBuy = shopService.isItemIDinQuickBuy(player, shopItem.getId());
                        if (isInQuickBuy) {
                            if (currentPage != 0) return;
                            shopService.removeQuickBuyItem(player, shopItem);
                            player.sendMessage(noItalic(Component.text("Removed " + shopItem.getName() + " from Quick Buy!").color(NamedTextColor.RED)));
                        } else {
                            player.closeInventory();
                            new GUIQuickBuyEditor(game, shopItem).open(player);
                        }
                        playClickSound(player);
                        update.accept(player);
                        return;
                    }

                    if (shopItem instanceof UpgradeableShopItem upgradeableShopItem) {
                        int nextLevel = upgradeableShopItem.getNextLevel(player);
                        if (nextLevel >= upgradeableShopItem.getTiers().size()) {
                            player.sendMessage(noItalic(Component.text("You have already purchased the maximum tier of this item!").color(NamedTextColor.RED)));
                            return;
                        }
                        UpgradeableItemTier nextTier = upgradeableShopItem.getNextTier(player);
                        if (!hasPlayerEnoughCurrencyForTier(game, player, nextTier)) {
                            int owned = Arrays.stream(player.getInventory().getItemStacks())
                                    .filter(s -> s.material() == nextTier.currency().getMaterial())
                                    .mapToInt(ItemStack::amount)
                                    .sum();
                            int needed = nextTier.price().apply(game.getBedwarsGameType()) - owned;
                            player.sendMessage(noItalic(Component.text("You don't have enough " + nextTier.currency().getName() + "! Need " + needed + " more!").color(NamedTextColor.RED)));
                            return;
                        }
                        upgradeableShopItem.handlePurchase(player, game.getBedwarsGameType());
                        player.sendMessage(noItalic(Component.text("You purchased " + nextTier.name() + "!").color(NamedTextColor.GREEN)));
                        playBuySound(player);
                        update.accept(player);
                        return;
                    }

                    if (!hasPlayerEnoughCurrency(game, player, shopItem)) {
                        player.sendMessage(noItalic(Component.text("You don't have enough " + shopItem.getCurrency().getName() + "!").color(NamedTextColor.RED)));
                        return;
                    }
                    if (!shopItem.isOwned(player)) {
                        player.sendMessage(noItalic(Component.text("You already have the highest tier available!").color(NamedTextColor.RED)));
                        return;
                    }


                    if (hasBetterItem(player, shopItem.getDisplay().material())) {
                        player.sendMessage("§cYou already have a better item!");
                        return;
                    }
                    shopItem.handlePurchase(player, game.getBedwarsGameType());
                    playBuySound(player);
                    update.accept(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    BedWarsPlayer player = (BedWarsPlayer) p;
                    ShopItem shopItem;
                    if ((currentPage != null && currentPage == 0 ) || quickBuyEditor != null) {
                        shopItem = shopService.getQuickShopItem(player, index);
                    } else if (currentPage != null) {
                        shopItem = shopService.getShopItem(currentPage, index);
                    } else {
                        throw new IllegalStateException("Current page cannot be null when getting shop items!");
                    }

                    if (shopItem == null) {
                        if (quickBuyEditor != null) {
                            return ItemStackCreator.getStack(
                                    "§cEmpty slot!",
                                    Material.RED_STAINED_GLASS_PANE,
                                    1,
                                    List.of("§eClick to set!")
                            );
                        }
                        if (currentPage != 0) return ItemStack.builder(Material.AIR);
                        return ItemStackCreator.getStack(
                                "§cEmpty slot!",
                                Material.RED_STAINED_GLASS_PANE,
                                1,
                                List.of("§7This is a Quick Buy Slot! §bShift Click", "§7any item in the shop to add it here.")
                        );
                    }

                    if (shopItem instanceof UpgradeableShopItem upgradeableShopItem) {
                        int nextLevel = upgradeableShopItem.getNextLevel(player);
                        UpgradeableItemTier nextTier = upgradeableShopItem.getNextTier(player);
                        boolean hasEnough = hasPlayerEnoughCurrencyForTier(game, player, nextTier);

                        List<String> lore = new ArrayList<>();
                        if (quickBuyEditor != null) {
                            lore.add("§eClick to replace!");
                        } else {
                            lore.add(
                                    "§7Cost: " + nextTier.currency().getColor() + nextTier.price().apply(game.getBedwarsGameType()) + " " + nextTier.currency().getName()
                            );
                            lore.add(" ");
                            if (upgradeableShopItem.getDescription() != null && !upgradeableShopItem.getDescription().isEmpty()) {
                                lore.addAll(StringUtility.splitByNewLine(upgradeableShopItem.getDescription(), "§7"));
                                lore.add(" ");
                            }

                            boolean isItemInQuickBuy = shopService.isItemIDinQuickBuy(player, upgradeableShopItem.getId());
                            if (currentPage != 0 && !isItemInQuickBuy) {
                                lore.add("§bShift Click to add to Quick Buy");
                            } else if (currentPage == 0 && isItemInQuickBuy) {
                                lore.add("§bShift Click to remove from Quick Buy");
                            }
                            if (nextLevel >= upgradeableShopItem.getTiers().size()) {
                                lore.add("§cYou have already purchased the maximum tier of this item!");
                            } else if (hasEnough) {
                                lore.add("§eClick to buy!");
                            } else {
                                lore.add("§cYou don't have enough " + nextTier.currency().getName() + "!");
                            }
                        }

                        String name;
                        if (quickBuyEditor != null) {
                            name = "§a" + nextTier.name();
                        } else {
                            name = hasEnough ? "§a" + nextTier.name() : "§c" + nextTier.name();
                        }

                        return ItemStackCreator.getStack(
                                name,
                                nextTier.material(),
                                1,
                                lore
                        );
                    }

                    boolean hasEnough = hasPlayerEnoughCurrency(game, player, shopItem);
                    List<String> lore = new ArrayList<>();
                    if (quickBuyEditor != null) {
                        lore.add("§eClick to replace!");
                    } else {
                        lore.add(
                                "§7Cost: " + shopItem.getCurrency().getColor() + shopItem.getPrice().apply(game.getBedwarsGameType()) + " " + shopItem.getCurrency().getName()
                        );
                        lore.add(" ");
                        if (shopItem.getDescription() != null && !shopItem.getDescription().isEmpty()) {
                            lore.addAll(StringUtility.splitByNewLine(shopItem.getDescription(), "§7"));
                            lore.add(" ");
                        }

                        boolean isItemInQuickBuy = shopService.isItemIDinQuickBuy(player, shopItem.getId());
                        if (currentPage != 0 && !isItemInQuickBuy) {
                            lore.add("§bShift Click to add to Quick Buy");
                        } else if (currentPage == 0 && isItemInQuickBuy) {
                            lore.add("§bShift Click to remove from Quick Buy");
                        }
                        if (!hasEnough) {
                            lore.add("§cYou don't have enough " + shopItem.getCurrency().getName() + "!");
                        } else if (!shopItem.isOwned(player)) {
                            lore.add("§aUNLOCKED");
                        } else if (hasBetterItem(player, shopItem.getDisplay().material())) {
                            lore.add("§cYou already have a better item!");
                        } else {
                            lore.add("§eClick to buy!");
                        }
                    }

                    Component name;
                    if (quickBuyEditor != null) {
                        name = Component.text("§a" + shopItem.getName());
                    } else {
                        name = hasEnough && shopItem.isOwned(player) ? Component.text("§a" + shopItem.getName()) : Component.text("§c" + shopItem.getName());
                    }

                    return ItemStackCreator.updateLore(
                            shopItem.getDisplay().builder().set(DataComponents.CUSTOM_NAME, name),
                            lore
                    );
                }
            });
        }
    }

    private static boolean hasPlayerEnoughCurrency(Game game, HypixelPlayer player, ShopItem shopItem) {
        int requiredAmount = shopItem.getPrice().apply(game.getBedwarsGameType());
        Material currencyMaterial = shopItem.getCurrency().getMaterial();

        int playerAmount = 0;
        for (ItemStack item : player.getInventory().getItemStacks()) {
            if (item.material() == currencyMaterial) {
                playerAmount += item.amount();
            }
        }

        return playerAmount >= requiredAmount;
    }

    private static boolean hasPlayerEnoughCurrencyForTier(Game game, HypixelPlayer player, UpgradeableItemTier tier) {
        int required = tier.price().apply(game.getBedwarsGameType());
        Material cur = tier.currency().getMaterial();
        int have = 0;
        for (ItemStack it : player.getInventory().getItemStacks()) {
            if (it.material() == cur) have += it.amount();
        }
        return have >= required;
    }

    private static boolean hasBetterItem(Player player, Material materialToBuy) {
        for (List<Material> group : TIERED_ITEM_GROUPS) {
            if (!group.contains(materialToBuy)) {
                continue;
            }

            int tierToBuy = group.indexOf(materialToBuy);
            for (ItemStack stack : player.getInventory().getItemStacks()) {
                if (group.contains(stack.material()) && group.indexOf(stack.material()) > tierToBuy) {
                    return true;
                }
            }
            for (ItemStack stack : List.of(
                    player.getEquipment(EquipmentSlot.BOOTS),
                    player.getEquipment(EquipmentSlot.LEGGINGS),
                    player.getEquipment(EquipmentSlot.CHESTPLATE),
                    player.getEquipment(EquipmentSlot.HELMET))) {
                if (group.contains(stack.material()) && group.indexOf(stack.material()) > tierToBuy) {
                    return true;
                }
            }
            return false;
        }

        return false;
    }

    private static void playClickSound(HypixelPlayer player) {
        player.playSound(Sound.sound(Key.key("minecraft:ui.button.click"), Sound.Source.MASTER, 1.0f, 1.0f));
    }

    private static void playBuySound(HypixelPlayer player) {
        player.playSound(Sound.sound(Key.key("minecraft:entity.experience_orb.pickup"), Sound.Source.MASTER, 1.0f, 1.0f));
    }

}

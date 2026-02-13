package net.swofty.type.bedwarsgame.gui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.game.v2.BedWarsGame;
import net.swofty.type.bedwarsgame.shop.ShopItem;
import net.swofty.type.bedwarsgame.shop.ShopManager;
import net.swofty.type.bedwarsgame.shop.UpgradeableItemTier;
import net.swofty.type.bedwarsgame.shop.UpgradeableShopItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.StatefulView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.swofty.type.bedwarsgame.util.ComponentManipulator.noItalic;

public class GUIItemShop implements StatefulView<GUIItemShop.State> {

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
    private final BedWarsGame game;

    public GUIItemShop(BedWarsGame game) {
        this.game = game;
    }

    @Override
    public State initialState() {
        return new State(0);
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("Item Shop", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        layout.allowHotkey(false);

        for (int slot = 9; slot <= 17; slot++) {
            layout.slot(slot, ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        layout.slot(state.currentPage() + 9, ItemStackCreator.createNamedItemStack(Material.GREEN_STAINED_GLASS_PANE));

        addCategoryButton(layout, 0, QUICK_BUY, state.currentPage(), 0);
        addCategoryButton(layout, 1, BLOCKS, state.currentPage(), 1);
        addCategoryButton(layout, 2, WEAPONS, state.currentPage(), 2);
        addCategoryButton(layout, 3, ARMOR, state.currentPage(), 3);
        addCategoryButton(layout, 4, TOOLS, state.currentPage(), 4);
        addCategoryButton(layout, 5, BOWS, state.currentPage(), 5);
        addCategoryButton(layout, 6, POTIONS, state.currentPage(), 6);
        addCategoryButton(layout, 7, UTILITY, state.currentPage(), 7);
        addCategoryButton(layout, 8, ROTATING_ITEMS, state.currentPage(), 8);

        populateShopItems(layout, shopService, game, state.currentPage(), null, c -> c.session(State.class).refresh());
    }

    private void addCategoryButton(ViewLayout<State> layout, int slot, ItemStack icon, int currentPage, int targetPage) {
        layout.slot(slot,
                (s, c) -> convertToClickToView(icon, currentPage, targetPage),
                (click, c) -> {
                    c.session(State.class).update(prev -> prev.withCurrentPage(targetPage));
                    playClickSound(click.player());
                }
        );
    }

    private ItemStack.Builder convertToClickToView(ItemStack itemStack, int currentPage, int index) {
        ItemStack.Builder builder = itemStack.builder();
        if (currentPage != index) {
            return builder.lore(noItalic(Component.text("Click to view!").color(NamedTextColor.YELLOW)));
        }
        return builder;
    }

    public static <S> void populateShopItems(ViewLayout<S> layout,
                                             ShopManager shopService,
                                             BedWarsGame game,
                                             @Nullable Integer currentPage,
                                             @Nullable ShopItem quickBuyEditor,
                                             Consumer<ViewContext> update) {
        int[] shopSlots = {
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };

        if (currentPage != null && currentPage == 8) {
            layout.slot(49, (s, c) -> ItemStackCreator.getStack("§aWhat are Rotating Items?", Material.PAPER, 1, List.of(
                    "§7Rotating Items are items that are",
                    "§7only available for a limited amount of",
                    "§7time. They may disappear and be",
                    "§7replaced with another temporary",
                    "§7item at any time."
            )));
        }

        if (currentPage != null && currentPage == 0) {
            layout.slot(45, (_, _) -> ItemStackCreator.getStack(
                "§aTracker Shop",
                Material.COMPASS,
                1,
                "§7Purchase tracking upgrade for your",
                "§7compass which will track each player",
                "§7on a specific team until you die."
            ), (_, context) -> {
                context.push(new TrackerShopView());
            });
            layout.slot(53, (_, _) -> ItemStackCreator.getStack("§aHotbar Manager", Material.BLAZE_POWDER, 1, "§7Edit preferred slots for your items", "§7per category.", "", "§eClick to edit!"), (_, context) -> {
                context.push(new HotbarManagerView());
            });
        }

        for (int i = 0; i < shopSlots.length; i++) {
            int slot = shopSlots[i];
            int index = i;

            layout.slot(slot,
                    (s, c) -> renderShopItem(c, shopService, game, currentPage, quickBuyEditor, index),
                    (click, c) -> handleShopItemClick(click.player(), click.click(), c, shopService, game, currentPage, quickBuyEditor, index, update)
            );
        }
    }

    private static ItemStack.Builder renderShopItem(ViewContext context,
                                                    ShopManager shopService,
                                                    BedWarsGame game,
                                                    @Nullable Integer currentPage,
                                                    @Nullable ShopItem quickBuyEditor,
                                                    int index) {
        BedWarsPlayer player = (BedWarsPlayer) context.player();
        ShopItem shopItem;
        if ((currentPage != null && currentPage == 0) || quickBuyEditor != null) {
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
                lore.add("§7Cost: " + nextTier.currency().getColor() + nextTier.price().apply(game.getGameType()) + " " + nextTier.currency().getName());
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

            String name = quickBuyEditor != null || hasEnough ? "§a" + nextTier.name() : "§c" + nextTier.name();

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
            lore.add("§7Cost: " + shopItem.getCurrency().getColor() + shopItem.getPrice().apply(game.getGameType()) + " " + shopItem.getCurrency().getName());
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

        Component name = quickBuyEditor != null
                ? Component.text("§a" + shopItem.getName())
                : hasEnough && shopItem.isOwned(player) ? Component.text("§a" + shopItem.getName()) : Component.text("§c" + shopItem.getName());

        return ItemStackCreator.updateLore(
                shopItem.getDisplay().builder().set(DataComponents.CUSTOM_NAME, name),
                lore
        );
    }

    private static void handleShopItemClick(HypixelPlayer p,
                                            Click click,
                                            ViewContext ctx,
                                            ShopManager shopService,
                                            BedWarsGame game,
                                            @Nullable Integer currentPage,
                                            @Nullable ShopItem quickBuyEditor,
                                            int index,
                                            Consumer<ViewContext> update) {
        BedWarsPlayer player = (BedWarsPlayer) p;
        ShopItem shopItem;
        if ((currentPage != null && currentPage == 0) || quickBuyEditor != null) {
            shopItem = shopService.getQuickShopItem(player, index);
        } else if (currentPage != null) {
            shopItem = shopService.getShopItem(currentPage, index);
        } else {
            throw new IllegalStateException("Current page cannot be null when clicking shop items!");
        }

        if (quickBuyEditor != null) {
            shopService.setQuickBuyItem(player, index, quickBuyEditor);
            player.sendMessage("§aAdded " + quickBuyEditor.getName() + " to Quick Buy!");
            ctx.replace(new GUIItemShop(game));
            return;
        }

        if (shopItem == null) return;

        if (click instanceof Click.LeftShift || click instanceof Click.RightShift) {
            boolean isInQuickBuy = shopService.isItemIDinQuickBuy(player, shopItem.getId());
            if (isInQuickBuy) {
                if (currentPage != 0) return;
                shopService.removeQuickBuyItem(player, shopItem);
                player.sendMessage(noItalic(Component.text("Removed " + shopItem.getName() + " from Quick Buy!").color(NamedTextColor.RED)));
            } else {
                ctx.replace(new GUIQuickBuyEditor(game, shopItem));
            }
            playClickSound(player);
            update.accept(ctx);
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
                int needed = nextTier.price().apply(game.getGameType()) - owned;
                player.sendMessage(noItalic(Component.text("You don't have enough " + nextTier.currency().getName() + "! Need " + needed + " more!").color(NamedTextColor.RED)));
                return;
            }

            upgradeableShopItem.handlePurchase(player, game.getGameType());
            player.sendMessage(noItalic(Component.text("You purchased " + nextTier.name() + "!").color(NamedTextColor.GREEN)));
            playBuySound(player);
            update.accept(ctx);
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

        shopItem.handlePurchase(player, game.getGameType());
        playBuySound(player);
        update.accept(ctx);
    }

    private static boolean hasPlayerEnoughCurrency(BedWarsGame game, HypixelPlayer player, ShopItem shopItem) {
        int requiredAmount = shopItem.getPrice().apply(game.getGameType());
        Material currencyMaterial = shopItem.getCurrency().getMaterial();

        int playerAmount = 0;
        for (ItemStack item : player.getInventory().getItemStacks()) {
            if (item.material() == currencyMaterial) {
                playerAmount += item.amount();
            }
        }

        return playerAmount >= requiredAmount;
    }

    private static boolean hasPlayerEnoughCurrencyForTier(BedWarsGame game, HypixelPlayer player, UpgradeableItemTier tier) {
        int required = tier.price().apply(game.getGameType());
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

    public record State(int currentPage) {
        public State withCurrentPage(int page) {
            return new State(page);
        }
    }
}

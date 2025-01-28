package net.swofty.types.generic.gui.inventory.inventories;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.enchantment.EnchantmentSource;
import net.swofty.types.generic.enchantment.EnchantmentType;
import net.swofty.types.generic.enchantment.SkyBlockEnchantment;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.ItemAttributeHandler;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.EnchantableComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIEnchantmentTable extends SkyBlockAbstractInventory {
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
        super(InventoryType.CHEST_6_ROW);
        this.bookshelfPower = getBookshelfPower(instance, enchantmentTable);
        doAction(new SetTitleAction(Component.text("Enchantment Table")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        attachItem(GUIItem.builder(48)
                .item(ItemStackCreator.getStack(
                        "§dBookshelf Power", Material.BOOKSHELF, 1,
                        "§7Stronger enchantments require",
                        "§7more bookshelf power which can",
                        "§7be increased by placing",
                        "§7bookshelves nearby.",
                        "§a ",
                        "§7Current Bookshelf Power:",
                        "§d" + bookshelfPower).build())
                .build());

        attachItem(GUIItem.builder(50)
                .item(ItemStackCreator.getStack(
                        "§aEnchantments Guide", Material.BOOK, 1,
                        "§7View a complete list of all",
                        "§7enchantments and their",
                        "§7requirements.",
                        "§a ",
                        "§eClick to view!").build())
                .build());

        attachItem(GUIItem.builder(28)
                .item(ItemStackCreator.getStack(
                        "§aEnchant Item", Material.ENCHANTING_TABLE, 1,
                        "§7Add and remove enchantments from",
                        "§7the time in the slot above!").build())
                .build());

        updateFromItem(null, null);
    }

    @SneakyThrows
    public void updateFromItem(SkyBlockItem item, EnchantmentType selected) {
        doAction(new SetTitleAction(Component.text(
                "Enchant Item " + (selected == null ? "" : "-> " + StringUtility.toNormalCase(selected.name())))));

        Arrays.stream(PAGINATED_SLOTS_LIST_ENCHANTS).forEach(slot ->
                attachItem(GUIItem.builder(slot)
                        .item(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "§7 ").build())
                        .build())
        );

        attachItem(GUIItem.builder(45)
                .item(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "§7 ").build())
                .build());

        if (item == null) {
            handleEmptyItem();
            return;
        }

        attachItem(GUIItem.builder(19)
                .item(() -> PlayerItemUpdater.playerUpdate(owner, item.getItemStack()).build())
                .onClick((ctx, clickedItem) -> {
                    ItemStack stack = ctx.cursorItem();

                    if (stack.get(ItemComponent.CUSTOM_NAME) == null) {
                        updateFromItem(null, null);
                        return true;
                    }

                    SkyBlockItem newItem = new SkyBlockItem(stack);
                    updateFromItem(newItem, null);
                    return true;
                })
                .build());

        ItemType type = item.getAttributeHandler().getPotentialType();
        if (item.getItemStack().amount() > 1 ||
                type == null ||
                !(item.hasComponent(EnchantableComponent.class))) {
            handleInvalidItem();
            return;
        }

        List<EnchantItemGroups> enchantItemGroups = item.getComponent(EnchantableComponent.class).getEnchantItemGroups();
        List<EnchantmentType> enchantments = Arrays.stream(EnchantmentType.values())
                .filter(enchantmentType -> enchantmentType.getEnch().getGroups().stream().anyMatch(enchantItemGroups::contains))
                .filter(enchantmentType -> enchantmentType.getEnchFromTable() != null)
                .toList();

        if (enchantments.isEmpty()) {
            handleNonEnchantableItem();
            return;
        }

        if (selected == null) {
            handleEnchantmentSelection(item, enchantments);
            return;
        }

        handleEnchantmentLevels(item, selected, type);
    }

    private void handleEmptyItem() {
        attachItem(GUIItem.builder(23)
                .item(ItemStackCreator.getStack(
                        "§cEnchant Item", Material.GRAY_DYE, 1,
                        "§7Place an item in the open slot",
                        "§7to enchant it!").build())
                .build());

        attachItem(GUIItem.builder(19)
                .item(ItemStack.AIR)
                .onClick((ctx, clickedItem) -> {
                    ItemStack stack = ctx.cursorItem();
                    if (stack.get(ItemComponent.CUSTOM_NAME) == null) return true;
                    updateFromItem(new SkyBlockItem(stack), null);
                    return true;
                })
                .build());
    }

    private void handleInvalidItem() {
        attachItem(GUIItem.builder(23)
                .item(ItemStackCreator.getStack(
                        "§cInvalid Item!", Material.RED_DYE, 1,
                        "§7You cannot enchant stacked items!").build())
                .build());
    }

    private void handleNonEnchantableItem() {
        attachItem(GUIItem.builder(23)
                .item(ItemStackCreator.getStack(
                        "§cCannot Enchant Item!", Material.RED_DYE, 1,
                        "§7This item cannot be enchanted!").build())
                .build());
    }

    private void handleEnchantmentSelection(SkyBlockItem item, List<EnchantmentType> enchantments) {
        enchantments = enchantments.stream().limit(15).toList();
        int i = 0;
        for (EnchantmentType enchantmentType : enchantments) {
            int finalI = i;
            attachItem(createEnchantmentDisplayItem(item, enchantmentType, PAGINATED_SLOTS_LIST_ENCHANTS[finalI]));
            i++;
        }
    }

    private void handleEnchantmentLevels(SkyBlockItem item, EnchantmentType selected, ItemType type) {
        int minLevel = selected.getEnch().getSources(owner).stream()
                .filter(source -> source.getSource().equals(EnchantmentSource.SourceType.ENCHANTMENT_TABLE.toString()))
                .mapToInt(value -> value.minLevel).findAny().orElse(0);
        int maxLevel = selected.getEnch().getSources(owner).stream()
                .filter(source -> source.getSource().equals(EnchantmentSource.SourceType.ENCHANTMENT_TABLE.toString()))
                .mapToInt(value -> value.maxLevel).findAny().orElse(0);

        int hasLevel = item.getAttributeHandler().hasEnchantment(selected) ?
                item.getAttributeHandler().getEnchantment(selected).level() : 0;

        for (int level = minLevel; level <= maxLevel; level++) {
            attachItem(createEnchantmentLevelItem(item, selected, type, level, hasLevel));
        }

        attachItem(GUIItem.builder(45)
                .item(ItemStackCreator.createNamedItemStack(Material.ARROW, "§aGo Back").build())
                .onClick((ctx, clickedItem) -> {
                    updateFromItem(item, null);
                    return true;
                })
                .build());
    }

    private GUIItem createEnchantmentDisplayItem(SkyBlockItem item, EnchantmentType enchantmentType, int slot) {
        return GUIItem.builder(slot)
                .item(() -> {
                    ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
                    List<String> lore = new ArrayList<>();
                    StringUtility.splitByWordAndLength(enchantmentType.getDescription(1, owner), 30)
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
                            lore).build();
                })
                .onClick((ctx, clickedItem) -> {
                    if (bookshelfPower < enchantmentType.getEnchFromTable().getRequiredBookshelfPower()) {
                        ctx.player().sendMessage("§cThis enchantment requires " +
                                enchantmentType.getEnchFromTable().getRequiredBookshelfPower() + " Bookshelf Power!");
                        return true;
                    }
                    updateFromItem(item, enchantmentType);
                    return true;
                })
                .build();
    }

    private GUIItem createEnchantmentLevelItem(SkyBlockItem item, EnchantmentType selected,
                                               ItemType type, int level, int hasLevel) {
        return GUIItem.builder(PAGINATED_SLOTS_LIST_LEVELS[level - 1])
                .item(() -> {
                    int levelCost = selected.getEnchFromTable().getLevelsFromTableToApply(owner).get(level);
                    List<String> lore = new ArrayList<>();
                    StringUtility.splitByWordAndLength(selected.getDescription(level, owner), 30)
                            .forEach(line -> lore.add("§7" + line));

                    lore.add("§a ");
                    if (hasLevel == level) {
                        lore.add("§cThis enchantment is already present");
                        lore.add("§cand can be removed.");
                        lore.add("§a ");
                    }

                    lore.add("§7Cost");

                    if (hasLevel > level) {
                        if (levelCost > owner.getLevel())
                            lore.add("§3" + levelCost + " Exp Levels §c§l✖");
                        else
                            lore.add("§3" + levelCost + " Exp Levels §a§l✓");

                        lore.add("§a ");
                        lore.add("§cHigher level already present!");
                        return ItemStackCreator.getStack(
                                "§9" + selected.getName() + " " + StringUtility.getAsRomanNumeral(level),
                                Material.GRAY_DYE, 1,
                                lore).build();
                    }

                    if (levelCost > owner.getLevel()) {
                        lore.add("§3" + levelCost + " Exp Levels §c§l✖");
                        lore.add("§a ");
                        lore.add("§cYou have insufficient levels!");
                    } else {
                        lore.add("§3" + levelCost + " Exp Levels §a§l✓");
                        lore.add("§a ");
                        if (hasLevel >= level) {
                            lore.add("§eClick to remove!");
                        } else {
                            lore.add("§eClick to enchant!");
                        }
                    }

                    return ItemStackCreator.getStack(
                            "§9" + selected.getName() + " " + StringUtility.getAsRomanNumeral(level),
                            Material.ENCHANTED_BOOK, 1,
                            lore).build();
                })
                .onClick((ctx, clickedItem) -> {
                    SkyBlockPlayer player = ctx.player();
                    if (clickedItem.material() == Material.GRAY_DYE)
                        return true;

                    String itemName = StringUtility.toNormalCase(type.name());

                    if (player.getLevel() < selected.getEnchFromTable().getLevelsFromTableToApply(player).get(level)) {
                        player.sendMessage("§cYou have insufficient levels!");
                        return true;
                    }

                    item.getAttributeHandler().removeEnchantment(selected);
                    if (hasLevel < level) {
                        item.getAttributeHandler().addEnchantment(
                                new SkyBlockEnchantment(selected, level)
                        );

                        player.setLevel(player.getLevel() - selected.getEnchFromTable().getLevelsFromTableToApply(player).get(level));
                        player.sendMessage("§aYou enchanted your " + itemName + " §awith " +
                                StringUtility.toNormalCase(selected.name()) + " " + StringUtility.getAsRomanNumeral(level) + "!");
                    } else {
                        int difference = hasLevel - level;

                        if (difference > 0) {
                            item.getAttributeHandler().addEnchantment(
                                    new SkyBlockEnchantment(selected, difference)
                            );
                        }

                        player.setLevel(player.getLevel() - selected.getEnchFromTable().getLevelsFromTableToApply(player).get(level));
                        player.sendMessage("§cYou removed " + StringUtility.toNormalCase(selected.name()) + " from your " + itemName + "§c!");
                    }

                    updateFromItem(item, selected);
                    return true;
                })
                .build();
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
    protected void onSuddenQuit(SkyBlockPlayer player) {
        player.addAndUpdateItem(new SkyBlockItem(getItemStack(19)));
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

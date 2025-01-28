package net.swofty.types.generic.gui.inventory.inventories.sbmenu;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.actions.SetCursorItemAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.StandardItemComponent;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.PlayerStatistics;

import java.util.ArrayList;
import java.util.List;

public class GUISkyBlockProfile extends SkyBlockAbstractInventory {

    public GUISkyBlockProfile() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Your Equipment and Stats")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Back and Close buttons
        attachItem(createBackButton());
        attachItem(createCloseButton());

        // Equipment slots
        attachItem(createHeldItemSlot());
        attachItem(createHelmetSlot());
        attachItem(createChestplateSlot());
        attachItem(createLeggingsSlot());
        attachItem(createBootsSlot());
        attachItem(createPetSlot());

        // Stats displays
        attachItem(createCombatStatsDisplay());
        attachItem(createGatheringStatsDisplay());
        attachItem(createWisdomStatsDisplay());
        attachItem(createMiscStatsDisplay());
    }

    private GUIItem createBackButton() {
        return GUIItem.builder(48)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To SkyBlock Menu").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockMenu());
                    return true;
                })
                .build();
    }

    private GUIItem createCloseButton() {
        return GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build();
    }

    private GUIItem createHeldItemSlot() {
        return GUIItem.builder(2)
                .item(() -> !owner.getItemInMainHand().isAir() ?
                        ItemStackCreator.getFromStack(owner.getItemInMainHand()).build() :
                        ItemStackCreator.getStack("§7Empty Held Item Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build())
                .build();
    }

    private GUIItem createHelmetSlot() {
        return GUIItem.builder(11)
                .item(() -> !owner.getHelmet().isAir() ?
                        ItemStackCreator.getFromStack(owner.getHelmet()).build() :
                        ItemStackCreator.getStack("§7Empty Helmet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build())
                .onClick((ctx, clickedItem) -> {
                    SkyBlockPlayer player = ctx.player();
                    SkyBlockItem item = new SkyBlockItem(ctx.cursorItem());

                    if (!player.getHelmet().isAir() && ctx.cursorItem().isAir()) {
                        player.addAndUpdateItem(player.getHelmet());
                        player.setHelmet(ItemStack.AIR);
                        player.openInventory(new GUISkyBlockProfile());
                        return true;
                    }

                    if (item.hasComponent(StandardItemComponent.class)
                            && item.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.HELMET
                            && player.getHelmet().isAir()) {
                        player.setHelmet(ctx.cursorItem());
                        new SetCursorItemAction(ctx, ItemStack.AIR).execute(this);
                        player.openInventory(new GUISkyBlockProfile());
                        return true;
                    }
                    return false;
                })
                .build();
    }

    private GUIItem createChestplateSlot() {
        return GUIItem.builder(20)
                .item(() -> !owner.getChestplate().isAir() ?
                        ItemStackCreator.getFromStack(owner.getChestplate()).build() :
                        ItemStackCreator.getStack("§7Empty Chestplate Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build())
                .onClick((ctx, clickedItem) -> {
                    SkyBlockPlayer player = ctx.player();
                    SkyBlockItem item = new SkyBlockItem(ctx.cursorItem());

                    if (!player.getChestplate().isAir() && ctx.cursorItem().isAir()) {
                        player.addAndUpdateItem(player.getChestplate());
                        player.setChestplate(ItemStack.AIR);
                        player.openInventory(new GUISkyBlockProfile());
                        return true;
                    }

                    if (item.hasComponent(StandardItemComponent.class)
                            && item.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.CHESTPLATE
                            && player.getChestplate().isAir()) {
                        player.setChestplate(ctx.cursorItem());
                        new SetCursorItemAction(ctx, ItemStack.AIR).execute(this);
                        player.openInventory(new GUISkyBlockProfile());
                        return true;
                    }
                    return false;
                })
                .build();
    }

    private GUIItem createLeggingsSlot() {
        return GUIItem.builder(29)
                .item(() -> !owner.getLeggings().isAir() ?
                        ItemStackCreator.getFromStack(owner.getLeggings()).build() :
                        ItemStackCreator.getStack("§7Empty Leggings Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build())
                .onClick((ctx, clickedItem) -> {
                    SkyBlockPlayer player = ctx.player();
                    SkyBlockItem item = new SkyBlockItem(ctx.cursorItem());

                    if (!player.getLeggings().isAir() && ctx.cursorItem().isAir()) {
                        player.addAndUpdateItem(player.getLeggings());
                        player.setLeggings(ItemStack.AIR);
                        player.openInventory(new GUISkyBlockProfile());
                        return true;
                    }

                    if (item.hasComponent(StandardItemComponent.class)
                            && item.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.LEGGINGS
                            && player.getLeggings().isAir()) {
                        player.setLeggings(ctx.cursorItem());
                        new SetCursorItemAction(ctx, ItemStack.AIR).execute(this);
                        player.openInventory(new GUISkyBlockProfile());
                        return true;
                    }
                    return false;
                })
                .build();
    }

    private GUIItem createBootsSlot() {
        return GUIItem.builder(38)
                .item(() -> !owner.getBoots().isAir() ?
                        ItemStackCreator.getFromStack(owner.getBoots()).build() :
                        ItemStackCreator.getStack("§7Empty Boots Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build())
                .onClick((ctx, clickedItem) -> {
                    SkyBlockPlayer player = ctx.player();
                    SkyBlockItem item = new SkyBlockItem(ctx.cursorItem());

                    if (!player.getBoots().isAir() && ctx.cursorItem().isAir()) {
                        player.addAndUpdateItem(player.getBoots());
                        player.setBoots(ItemStack.AIR);
                        player.openInventory(new GUISkyBlockProfile());
                        return true;
                    }

                    if (item.hasComponent(StandardItemComponent.class)
                            && item.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.BOOTS
                            && player.getBoots().isAir()) {
                        player.setBoots(ctx.cursorItem());
                        new SetCursorItemAction(ctx, ItemStack.AIR).execute(this);
                        player.openInventory(new GUISkyBlockProfile());
                        return true;
                    }
                    return false;
                })
                .build();
    }

    private GUIItem createPetSlot() {
        return GUIItem.builder(47)
                .item(() -> {
                    if (owner.getPetData().getEnabledPet() != null && !owner.getPetData().getEnabledPet().getItemStack().isAir()) {
                        SkyBlockItem pet = owner.getPetData().getEnabledPet();
                        return new NonPlayerItemUpdater(pet).getUpdatedItem().build();
                    }
                    return ItemStackCreator.getStack("§7Empty Pet Slot", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIInventoryPets());
                    return true;
                })
                .build();
    }

    private GUIItem createCombatStatsDisplay() {
        return GUIItem.builder(15)
                .item(() -> {
                    PlayerStatistics statistics = owner.getStatistics();
                    List<String> lore = new ArrayList<>(List.of("§7Gives you a better chance at", "§7fighting strong monsters. "));
                    List<ItemStatistic> stats = new ArrayList<>(List.of(
                            ItemStatistic.HEALTH, ItemStatistic.DEFENSE, ItemStatistic.STRENGTH, ItemStatistic.INTELLIGENCE,
                            ItemStatistic.CRIT_CHANCE, ItemStatistic.CRIT_DAMAGE, ItemStatistic.BONUS_ATTACK_SPEED,
                            ItemStatistic.ABILITY_DAMAGE, ItemStatistic.TRUE_DEFENSE, ItemStatistic.FEROCITY,
                            ItemStatistic.HEALTH_REGEN, ItemStatistic.VITALITY, ItemStatistic.MENDING, ItemStatistic.SWING_RANGE
                    ));

                    statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                        if (!value.equals(statistic.getBaseAdditiveValue()) && stats.contains(statistic)) {
                            lore.add(" " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                    StringUtility.toNormalCase(statistic.name()) + " §f" +
                                    StringUtility.decimalify(value, 2) + statistic.getSuffix());
                        }
                    });

                    lore.add("");
                    lore.add("§eClick for details!");
                    return ItemStackCreator.getStack("§cCombat Stats", Material.DIAMOND_SWORD, 1, lore).build();
                })
                .onClick((ctx, item) -> true)
                .build();
    }

    private GUIItem createGatheringStatsDisplay() {
        return GUIItem.builder(16)
                .item(() -> {
                    PlayerStatistics statistics = owner.getStatistics();
                    List<String> lore = new ArrayList<>(List.of("§7Lets you collect and harvest better", "§7items, or more of them. "));
                    List<ItemStatistic> stats = new ArrayList<>(List.of(
                            ItemStatistic.MINING_SPEED, ItemStatistic.MINING_FORTUNE, ItemStatistic.BREAKING_POWER,
                            ItemStatistic.PRISTINE, ItemStatistic.FORAGING_FORTUNE, ItemStatistic.FARMING_FORTUNE
                    ));

                    statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                        if (!value.equals(statistic.getBaseAdditiveValue()) && stats.contains(statistic)) {
                            lore.add(" " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                    StringUtility.toNormalCase(statistic.name()) + " §f" +
                                    StringUtility.decimalify(value, 2) + statistic.getSuffix());
                        }
                    });

                    lore.add("");
                    lore.add("§eClick for details!");
                    return ItemStackCreator.getStack("§eGathering Stats", Material.IRON_PICKAXE, 1, lore).build();
                })
                .onClick((ctx, item) -> true)
                .build();
    }

    private GUIItem createWisdomStatsDisplay() {
        return GUIItem.builder(24)
                .item(() -> {
                    PlayerStatistics statistics = owner.getStatistics();
                    List<String> lore = new ArrayList<>(List.of("§7Increases the §3XP §7you gain on your", "§7skills "));
                    List<ItemStatistic> stats = new ArrayList<>();

                    statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                        if (!value.equals(statistic.getBaseAdditiveValue()) && stats.contains(statistic)) {
                            lore.add(" " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                    StringUtility.toNormalCase(statistic.name()) + " §f" +
                                    StringUtility.decimalify(value, 2) + statistic.getSuffix());
                        }
                    });

                    lore.add("");
                    lore.add("§eClick for details!");
                    return ItemStackCreator.getStack("§3Wisdom Stats", Material.BOOK, 1, lore).build();
                })
                .onClick((ctx, item) -> true)
                .build();
    }

    private GUIItem createMiscStatsDisplay() {
        return GUIItem.builder(25)
                .item(() -> {
                    PlayerStatistics statistics = owner.getStatistics();
                    List<String> lore = new ArrayList<>(List.of("§7Augments various aspects of your", "§7gameplay! "));
                    List<ItemStatistic> stats = new ArrayList<>(List.of(
                            ItemStatistic.SPEED, ItemStatistic.MAGIC_FIND, ItemStatistic.PET_LUCK,
                            ItemStatistic.SEA_CREATURE_CHANCE, ItemStatistic.FISHING_SPEED,
                            ItemStatistic.COLD_RESISTANCE, ItemStatistic.BONUS_PEST_CHANCE
                    ));

                    statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                        if (!value.equals(statistic.getBaseAdditiveValue()) && stats.contains(statistic)) {
                            lore.add(" " + statistic.getDisplayColor() + statistic.getSymbol() + " " +
                                    StringUtility.toNormalCase(statistic.name()) + " §f" +
                                    StringUtility.decimalify(value, 2) + statistic.getSuffix());
                        }
                    });

                    lore.add("");
                    lore.add("§eClick for details!");
                    return ItemStackCreator.getStack("§dMisc Stats", Material.CLOCK, 1, lore).build();
                })
                .onClick((ctx, item) -> true)
                .build();
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(false);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {}
}
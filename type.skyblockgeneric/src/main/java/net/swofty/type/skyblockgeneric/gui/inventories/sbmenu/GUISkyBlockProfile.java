package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointInventory;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats.GUIGatheringCategoryStats;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.StandardItemComponent;
import net.swofty.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockInventory;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.PlayerStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GUISkyBlockProfile extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.profile.title", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        layout.slot(2, (s, c) -> new NonPlayerItemUpdater(new SkyBlockItem(ItemType.SKYBLOCK_MENU))
            .getUpdatedItem());

        equipmentSlot(layout, 10, List.of("Necklace"), SkyBlockInventory::getNecklace, SkyBlockInventory::setNecklace,
            StandardItemComponent.StandardItemType.NECKLACE);
        equipmentSlot(layout, 19, List.of("Cloak"), SkyBlockInventory::getCloak, SkyBlockInventory::setCloak,
            StandardItemComponent.StandardItemType.CLOAK);
        equipmentSlot(layout, 28, List.of("Belt"), SkyBlockInventory::getBelt, SkyBlockInventory::setBelt,
            StandardItemComponent.StandardItemType.BELT);
        equipmentSlot(layout, 37, List.of("Gloves", "Bracelet"), SkyBlockInventory::getGloves, SkyBlockInventory::setGloves,
            StandardItemComponent.StandardItemType.GLOVES, StandardItemComponent.StandardItemType.BRACELET);

        // Helmet
        layout.slot(11, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (!player.getHelmet().isAir()) {
                return ItemStackCreator.getFromStack(player.getHelmet());
            } else {
                return TranslatableItemStackCreator.getStack("gui_sbmenu.profile.empty_helmet", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            }
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockItem cursorItem = new SkyBlockItem(player.getInventory().getCursorItem());
            if (!player.getHelmet().isAir() && player.getInventory().getCursorItem().isAir()) {
                player.addAndUpdateItem(player.getHelmet());
                player.setHelmet(ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            } else if (cursorItem.hasComponent(StandardItemComponent.class)
                && cursorItem.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.HELMET
                && player.getHelmet().isAir()) {
                player.setHelmet(player.getInventory().getCursorItem());
                c.inventory().setCursorItem(player, ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            }
        });

        // Chestplate
        layout.slot(20, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (!player.getChestplate().isAir()) {
                return ItemStackCreator.getFromStack(player.getChestplate());
            } else {
                return TranslatableItemStackCreator.getStack("gui_sbmenu.profile.empty_chestplate", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            }
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockItem cursorItem = new SkyBlockItem(player.getInventory().getCursorItem());
            if (!player.getChestplate().isAir() && player.getInventory().getCursorItem().isAir()) {
                player.addAndUpdateItem(player.getChestplate());
                player.setChestplate(ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            } else if (cursorItem.hasComponent(StandardItemComponent.class)
                && cursorItem.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.CHESTPLATE
                && player.getChestplate().isAir()) {
                player.setChestplate(player.getInventory().getCursorItem());
                c.inventory().setCursorItem(player, ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            }
        });

        // Leggings
        layout.slot(29, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (!player.getLeggings().isAir()) {
                return ItemStackCreator.getFromStack(player.getLeggings());
            } else {
                return TranslatableItemStackCreator.getStack("gui_sbmenu.profile.empty_leggings", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            }
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockItem cursorItem = new SkyBlockItem(player.getInventory().getCursorItem());
            if (!player.getLeggings().isAir() && player.getInventory().getCursorItem().isAir()) {
                player.addAndUpdateItem(player.getLeggings());
                player.setLeggings(ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            } else if (cursorItem.hasComponent(StandardItemComponent.class)
                && cursorItem.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.LEGGINGS
                && player.getLeggings().isAir()) {
                player.setLeggings(player.getInventory().getCursorItem());
                c.inventory().setCursorItem(player, ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            }
        });

        // Boots
        layout.slot(38, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (!player.getBoots().isAir()) {
                return ItemStackCreator.getFromStack(player.getBoots());
            } else {
                return TranslatableItemStackCreator.getStack("gui_sbmenu.profile.empty_boots", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            }
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockItem cursorItem = new SkyBlockItem(player.getInventory().getCursorItem());
            if (!player.getBoots().isAir() && player.getInventory().getCursorItem().isAir()) {
                player.addAndUpdateItem(player.getBoots());
                player.setBoots(ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            } else if (cursorItem.hasComponent(StandardItemComponent.class)
                && cursorItem.getComponent(StandardItemComponent.class).getType() == StandardItemComponent.StandardItemType.BOOTS
                && player.getBoots().isAir()) {
                player.setBoots(player.getInventory().getCursorItem());
                c.inventory().setCursorItem(player, ItemStack.AIR);
                c.player().openView(new GUISkyBlockProfile());
            }
        });

        // Pet
        layout.slot(47, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (player.getPetData().getEnabledPet() != null && !player.getPetData().getEnabledPet().getItemStack().isAir()) {
                SkyBlockItem pet = player.getPetData().getEnabledPet();
                return new NonPlayerItemUpdater(pet).getUpdatedItem();
            } else {
                return TranslatableItemStackCreator.getStack("gui_sbmenu.profile.empty_pet", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
            }
        }, (click, c) -> {
            //c.player().openView(new GUIPets())
        });

        // Combat Stats
        layout.slot(14, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            PlayerStatistics statistics = player.getStatistics();
            List<Component> lore = new ArrayList<>();
            lore.add(I18n.t("gui_sbmenu.profile.combat_stats.lore.1"));
            lore.add(I18n.t("gui_sbmenu.profile.combat_stats.lore.2"));
            lore.add(Component.space());

            List<ItemStatistic> stats = List.of(ItemStatistic.HEALTH, ItemStatistic.DEFENSE, ItemStatistic.STRENGTH, ItemStatistic.INTELLIGENCE,
                ItemStatistic.CRITICAL_CHANCE, ItemStatistic.CRITICAL_DAMAGE, ItemStatistic.BONUS_ATTACK_SPEED, ItemStatistic.ABILITY_DAMAGE, ItemStatistic.TRUE_DEFENSE,
                ItemStatistic.FEROCITY, ItemStatistic.HEALTH_REGENERATION, ItemStatistic.VITALITY, ItemStatistic.MENDING, ItemStatistic.SWING_RANGE);

            for (ItemStatistic statistic : stats) {
                double value = statistics.allStatistics().getOverall(statistic);
                lore.add(Component.text(" " + statistic.getFullDisplayName() + " §f"
                    + StringUtility.decimalify(value, 2) + statistic.getSuffix()));
            }

            lore.add(Component.space());
            lore.add(I18n.t("gui_common.details"));

            return TranslatableItemStackCreator.getStack("gui_sbmenu.profile.combat_stats", Material.STONE_SWORD, 1,
                lore);
        }, (click, c) -> {
            c.player().openView(new GUIGatheringCategoryStats(GUIGatheringCategoryStats.Category.COMBAT));
        });

        gatheringCategorySlot(layout, 15, GUIGatheringCategoryStats.Category.MINING);
        gatheringCategorySlot(layout, 16, GUIGatheringCategoryStats.Category.FARMING);
        gatheringCategorySlot(layout, 23, GUIGatheringCategoryStats.Category.FORAGING);
        gatheringCategorySlot(layout, 24, GUIGatheringCategoryStats.Category.FISHING);
        gatheringCategorySlot(layout, 32, GUIGatheringCategoryStats.Category.HUNTING);

        // Wisdom Stats
        layout.slot(34, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            PlayerStatistics statistics = player.getStatistics();
            List<Component> lore = new ArrayList<>();
            lore.add(I18n.t("gui_sbmenu.profile.wisdom_stats.lore.1"));
            lore.add(I18n.t("gui_sbmenu.profile.wisdom_stats.lore.2"));
            lore.add(Component.space());

            List<ItemStatistic> stats = List.of(ItemStatistic.COMBAT_WISDOM, ItemStatistic.FARMING_WISDOM, ItemStatistic.FISHING_WISDOM,
                ItemStatistic.MINING_WISDOM, ItemStatistic.FORAGING_WISDOM, ItemStatistic.ENCHANTING_WISDOM,
                ItemStatistic.ALCHEMY_WISDOM, ItemStatistic.CARPENTRY_WISDOM, ItemStatistic.RUNE_CRAFTING_WISDOM,
                ItemStatistic.TAMING_WISDOM, ItemStatistic.SOCIAL_WISDOM, ItemStatistic.HUNTING_WISDOM);

            for (ItemStatistic statistic : stats) {
                double value = statistics.allStatistics().getOverall(statistic);
                lore.add(Component.text(" " + statistic.getFullDisplayName() + " §f"
                    + StringUtility.decimalify(value, 2) + statistic.getSuffix()));
            }

            lore.add(Component.space());
            lore.add(I18n.t("gui_common.details"));

            return TranslatableItemStackCreator.getStack("gui_sbmenu.profile.wisdom_stats", Material.BOOK, 1,
                lore);
        }, (_, c) -> {
            c.player().openView(new GUIGatheringCategoryStats(GUIGatheringCategoryStats.Category.WISDOM));
        });

        // Misc Stats
        layout.slot(25, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            PlayerStatistics statistics = player.getStatistics();
            List<Component> lore = new ArrayList<>();
            lore.add(I18n.t("gui_sbmenu.profile.misc_stats.lore.1"));
            lore.add(I18n.t("gui_sbmenu.profile.misc_stats.lore.2"));
            lore.add(Component.space());

            List<ItemStatistic> stats = List.of(ItemStatistic.SPEED, ItemStatistic.MAGIC_FIND, ItemStatistic.PET_LUCK,
                ItemStatistic.HEAT_RESISTANCE, ItemStatistic.COLD_RESISTANCE, ItemStatistic.RESPIRATION,
                ItemStatistic.PRESSURE_RESISTANCE, ItemStatistic.FEAR, ItemStatistic.TRACKING);

            for (ItemStatistic statistic : stats) {
                double value = statistics.allStatistics().getOverall(statistic);
                lore.add(Component.text(" " + statistic.getFullDisplayName() + " §f"
                    + StringUtility.decimalify(value, 2) + statistic.getSuffix()));
            }

            lore.add(Component.space());
            lore.add(I18n.t("gui_common.details"));

            return TranslatableItemStackCreator.getStack("gui_sbmenu.profile.misc_stats", Material.CLOCK, 1, lore);
        }, (_, c) -> {
            c.player().openView(new GUIGatheringCategoryStats(GUIGatheringCategoryStats.Category.MISC));
        });
    }

    private static void equipmentSlot(
        ViewLayout<DefaultState> layout,
        int slot,
        List<String> displayName,
        Function<SkyBlockInventory, UnderstandableSkyBlockItem> getter,
        BiConsumer<SkyBlockInventory, UnderstandableSkyBlockItem> setter,
        StandardItemComponent.StandardItemType... allowedTypes
    ) {
        layout.slot(slot, (_, c) -> {
            SkyBlockInventory inventory = getStoredInventory((SkyBlockPlayer) c.player());
            SkyBlockItem item = new SkyBlockItem(getter.apply(inventory));
            if (!item.isAir()) return new NonPlayerItemUpdater(item).getUpdatedItem();

            List<String> lore = new ArrayList<>();
            lore.add("§8 > " + displayName.getFirst());
            if (displayName.size() > 1) {
                for (int i = 1; i < displayName.size(); i++) {
                    lore.add("§8 > " + displayName.get(i));
                }
            }

            return ItemStackCreator.getStack(
                "§7Empty Equipment Slot",
                Material.LIGHT_GRAY_STAINED_GLASS_PANE,
                1,
                lore

            );
        }, (_, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockInventory inventory = getStoredInventory(player);
            SkyBlockItem equipped = new SkyBlockItem(getter.apply(inventory));
            ItemStack cursor = player.getInventory().getCursorItem();

            if (!equipped.isAir() && cursor.isAir()) {
                player.addAndUpdateItem(equipped.getItemStack());
                setter.accept(inventory, new SkyBlockItem(Material.AIR).toUnderstandable());
                player.openView(new GUISkyBlockProfile());
                return;
            }

            SkyBlockItem cursorItem = new SkyBlockItem(cursor);
            if (!equipped.isAir() || !cursorItem.hasComponent(StandardItemComponent.class)) return;
            StandardItemComponent.StandardItemType cursorType =
                cursorItem.getComponent(StandardItemComponent.class).getType();
            if (Arrays.stream(allowedTypes).noneMatch(type -> type == cursorType)) return;

            setter.accept(inventory, cursorItem.toUnderstandable());
            c.inventory().setCursorItem(player, ItemStack.AIR);
            player.openView(new GUISkyBlockProfile());
        });
    }

    private static void gatheringCategorySlot(
        ViewLayout<DefaultState> layout,
        int slot,
        GUIGatheringCategoryStats.Category category
    ) {
        layout.slot(slot, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            return category.createProfileSummary(player);
        }, (click, c) -> c.player().openView(new GUIGatheringCategoryStats(category)));
    }

    private static SkyBlockInventory getStoredInventory(SkyBlockPlayer player) {
        return player.getSkyblockDataHandler()
            .get(SkyBlockDataHandler.Data.INVENTORY, DatapointInventory.class)
            .getValue();
    }
}

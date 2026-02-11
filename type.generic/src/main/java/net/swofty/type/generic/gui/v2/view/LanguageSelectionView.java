package net.swofty.type.generic.gui.v2.view;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.minestom.server.component.DataComponents;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.language.LanguageMessage;
import net.swofty.type.generic.language.PlayerLanguage;
import net.swofty.type.generic.language.PlayerLanguageService;

public final class LanguageSelectionView {
    private static final ItemStack.Builder FILLER = ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE)
            .set(DataComponents.CUSTOM_NAME, net.kyori.adventure.text.Component.space())
            .set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.EMPTY);

    private LanguageSelectionView() {
    }

    public static final class Page1 extends StatelessView {
        @Override
        public ViewConfiguration<DefaultState> configuration() {
        public ViewConfiguration<Void> configuration() {
            return new ViewConfiguration<>("Select Language", InventoryType.CHEST_4_ROW);
        }

        @Override
        public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
            Components.fill(layout);
        public void layout(ViewLayout<Void> layout, Void state, ViewContext ctx) {
            layout.filler(FILLER);
            addLanguageSlot(layout, 10, PlayerLanguage.ENGLISH, Material.RED_CONCRETE);
            addLanguageSlot(layout, 11, PlayerLanguage.JAPANESE, Material.GREEN_CONCRETE);
            addLanguageSlot(layout, 12, PlayerLanguage.KOREAN, Material.BLUE_CONCRETE);
            addLanguageSlot(layout, 13, PlayerLanguage.CHINESE_SIMPLIFIED, Material.WHITE_CONCRETE);

            setDecor(layout, 14, Material.RED_WOOL);
            setDecor(layout, 15, Material.BLUE_WOOL);
            setDecor(layout, 16, Material.RED_TERRACOTTA);
            setDecor(layout, 19, Material.BLUE_TERRACOTTA);
            setDecor(layout, 20, Material.BLACK_CONCRETE);
            setDecor(layout, 21, Material.GREEN_WOOL);
            setDecor(layout, 22, Material.RED_WOOL);
            setDecor(layout, 23, Material.WHITE_WOOL);
            setDecor(layout, 24, Material.BLACK_WOOL);
            setDecor(layout, 25, Material.RED_WOOL);

            Components.close(layout, 31);
            setCloseButton(layout, 31, ctx);
            setBookNoAction(layout, 33);
            setAutoDetectButton(layout, 34, ctx);
            layout.slot(35,
                    ItemStackCreator.getStack("§aNext Page", Material.ARROW, 1,
                            "§7View more language options.",
                            "",
                            "§eClick to continue"),
                    (click, clickCtx) -> clickCtx.replace(new Page2()));
        }
    }

    public static final class Page2 extends StatelessView {
        @Override
        public ViewConfiguration<DefaultState> configuration() {
        public ViewConfiguration<Void> configuration() {
            return new ViewConfiguration<>("Select Language", InventoryType.CHEST_4_ROW);
        }

        @Override
        public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
            Components.fill(layout);
        public void layout(ViewLayout<Void> layout, Void state, ViewContext ctx) {
            layout.filler(FILLER);
            setDecor(layout, 10, Material.CYAN_CONCRETE);
            setDecor(layout, 11, Material.ORANGE_CONCRETE);
            setDecor(layout, 12, Material.PURPLE_CONCRETE);
            setDecor(layout, 13, Material.YELLOW_CONCRETE);
            setDecor(layout, 14, Material.LIME_CONCRETE);
            setDecor(layout, 15, Material.PINK_CONCRETE);
            setDecor(layout, 16, Material.LIGHT_BLUE_CONCRETE);

            Components.close(layout, 31);
            setCloseButton(layout, 31, ctx);
            setBookNoAction(layout, 33);
            setAutoDetectButton(layout, 34, ctx);
            layout.slot(35,
                    ItemStackCreator.getStack("§aPrevious Page", Material.ARROW, 1,
                            "§7Return to the main language page.",
                            "",
                            "§eClick to go back"),
                    (click, clickCtx) -> clickCtx.replace(new Page1()));
        }
    }

    private static void addLanguageSlot(ViewLayout<DefaultState> layout, int slot, PlayerLanguage language, Material material) {
    private static void addLanguageSlot(ViewLayout<Void> layout, int slot, PlayerLanguage language, Material material) {
        layout.slot(slot,
                (state, ctx) -> {
                    boolean selected = ctx.player().getLanguage() == language;
                    ItemStack.Builder base = ItemStackCreator.getStack(
                            (selected ? "§a" : "§f") + language.getDisplayName(),
                            material,
                            1,
                            "§7Code: §f" + language.getId(),
                            "",
                            selected ? "§aSELECTED" : "§eClick to select"
                    );
                    return selected ? ItemStackCreator.enchant(base) : base;
                },
                (click, ctx) -> {
                    PlayerLanguageService.applyLanguage(ctx.player(), language);
                    ctx.player().sendMessage(LanguageMessage.LANGUAGE_UPDATED.format(
                            language,
                            language.getDisplayName(),
                            language.getId()));
                    ctx.player().closeInventory();
                });
    }

    private static void setDecor(ViewLayout<DefaultState> layout, int slot, Material material) {
        layout.slot(slot, ItemStackCreator.createNamedItemStack(material, "§f"));
    }

    private static void setBookNoAction(ViewLayout<DefaultState> layout, int slot) {
    private static void setDecor(ViewLayout<Void> layout, int slot, Material material) {
        layout.slot(slot, ItemStackCreator.createNamedItemStack(material, "§f"));
    }

    private static void setCloseButton(ViewLayout<Void> layout, int slot, ViewContext ctx) {
        layout.slot(slot, ItemStackCreator.getStack("§cClose", Material.BARRIER, 1), (click, clickCtx) -> ctx.player().closeInventory());
    }

    private static void setBookNoAction(ViewLayout<Void> layout, int slot) {
        layout.slot(slot, ItemStackCreator.getStack("§eLanguages", Material.BOOK, 1,
                "§7Browse available languages."));
    }

    private static void setAutoDetectButton(ViewLayout<DefaultState> layout, int slot, ViewContext ctx) {
    private static void setAutoDetectButton(ViewLayout<Void> layout, int slot, ViewContext ctx) {
        layout.slot(slot,
                ItemStackCreator.getStack("§aAuto Detect Language", Material.MAGMA_CREAM, 1,
                        "§7Automatically detect your",
                        "§7client language and apply it.",
                        "",
                        "§eClick to detect"),
                (click, clickCtx) -> {
                    PlayerLanguage detected = PlayerLanguageService.detectClientLanguage(ctx.player());
                    PlayerLanguageService.applyLanguage(ctx.player(), detected);
                    ctx.player().sendMessage(LanguageMessage.LANGUAGE_UPDATED.format(
                            detected,
                            detected.getDisplayName(),
                            detected.getId()));
                    ctx.player().closeInventory();
                });
    }
}

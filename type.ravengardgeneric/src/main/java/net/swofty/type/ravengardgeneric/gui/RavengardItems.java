package net.swofty.type.ravengardgeneric.gui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.ShadowColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.TooltipDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class RavengardItems {
    private static final Material BUTTON_MATERIAL = Material.LEATHER_CHESTPLATE;
    private static final ShadowColor LABEL_SHADOW = ShadowColor.shadowColor(4194303);
    private static final ShadowColor ICON_SHADOW = ShadowColor.shadowColor(0);

    private static final Set<DataComponent<?>> HIDDEN = Set.of(
            DataComponents.PAINTING_VARIANT,
            DataComponents.FIREWORKS,
            DataComponents.ATTRIBUTE_MODIFIERS,
            DataComponents.ENCHANTMENTS,
            DataComponents.STORED_ENCHANTMENTS,
            DataComponents.TRIM,
            DataComponents.CHARGED_PROJECTILES,
            DataComponents.JUKEBOX_PLAYABLE,
            DataComponents.MAP_ID,
            DataComponents.UNBREAKABLE,
            DataComponents.WRITTEN_BOOK_CONTENT,
            DataComponents.BANNER_PATTERNS,
            DataComponents.POTION_CONTENTS,
            DataComponents.DYED_COLOR);

    private RavengardItems() {
    }

    public static Builder button(RavengardSprite button) {
        return new Builder(button);
    }

    public static Component label(RavengardSprite button, String text, int offset) {
        return label(button, text, offset, RavengardFont.DEFAULT);
    }

    public static Component label(RavengardSprite button, String text, int offset, Key font) {
        return Component.text(button.icon() + RavengardFont.space(-1))
                .color(TextColor.color(offset))
                .shadowColor(ICON_SHADOW)
                .decoration(TextDecoration.ITALIC, false)
                .append(Component.text(text)
                        .color(NamedTextColor.WHITE)
                        .shadowColor(LABEL_SHADOW)
                        .font(font));
    }

    public static final class Builder {
        private final RavengardSprite button;
        private final List<Component> lore = new ArrayList<>();
        private String text = "";
        private String tooltipStyle;
        private int amount = 1;
        private int originSlot;
        private Key font = RavengardFont.DEFAULT;
        private int hoverColorOverride = -1;

        private Builder(RavengardSprite button) {
            this.button = button;
        }

        /**
         * Renders the whole tooltip in the given font. Hypixel puts unreleased buttons in
         * {@link RavengardFont#ILLAGERALT} so their placeholder text reads as runes.
         */
        public Builder font(Key value) {
            this.font = value;
            return this;
        }

        /**
         * The slot the button's sprite starts at. For a button spanning several slots this stays
         * the top-left one on every copy, so the whole button shares a single hover glow.
         */
        public Builder origin(int slot) {
            this.originSlot = slot;
            return this;
        }

        /**
         * Overrides the computed offset with a captured one. Hypixel authors these per button per
         * menu -- they usually line up with the slot grid, but not always (the select-class statues
         * sit at a fixed height regardless of which row their click slot is on), so a captured
         * value always wins over {@link RavengardSprite#hoverColor(int)}.
         */
        public Builder hoverColor(int value) {
            this.hoverColorOverride = value;
            return this;
        }

        private int offset() {
            return hoverColorOverride >= 0 ? hoverColorOverride : button.hoverColor(originSlot);
        }

        public RavengardSprite sprite() {
            return button;
        }

        public Builder label(String value) {
            this.text = value;
            return this;
        }

        public Builder amount(int value) {
            this.amount = value;
            return this;
        }

        public Builder tooltipStyle(String value) {
            this.tooltipStyle = value;
            return this;
        }

        public Builder lore(String... lines) {
            for (String line : lines) {
                lore.add(Component.text(line).decoration(TextDecoration.ITALIC, false).font(font));
            }
            return this;
        }

        public Builder lore(Component... lines) {
            lore.addAll(List.of(lines));
            return this;
        }

        public Builder blankLine() {
            lore.add(Component.empty());
            return this;
        }

        public ItemStack build() {
            return toBuilder().build();
        }

        public ItemStack.Builder toBuilder() {
            ItemStack.Builder builder = ItemStack.builder(BUTTON_MATERIAL)
                    .amount(amount)
                    .set(DataComponents.ITEM_MODEL, button.itemModel())
                    .set(DataComponents.CUSTOM_NAME, RavengardItems.label(button, text, offset(), font))
                    // item.vsh forwards the dye colour as the button face's screen offset, the same
                    // encoding the hover glow reads off custom_name. Both must carry it: custom_name
                    // places the lit sprite, dyed_color places the unlit one.
                    .set(DataComponents.DYED_COLOR, TextColor.color(offset()))
                    .set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, HIDDEN));

            if (!lore.isEmpty()) {
                builder.set(DataComponents.LORE, List.copyOf(lore));
            }
            if (tooltipStyle != null) {
                builder.set(DataComponents.TOOLTIP_STYLE, tooltipStyle);
            }
            return builder;
        }
    }
}

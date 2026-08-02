package net.swofty.type.ravengardgeneric.item;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.item.component.TooltipDisplay;
import net.swofty.type.ravengardgeneric.classes.RavengardClass;
import net.swofty.type.ravengardgeneric.data.monogdb.RavengardTrackedItemsDatabase;
import net.swofty.type.ravengardgeneric.item.components.PlaceholderSlotComponent;
import net.swofty.type.ravengardgeneric.item.components.StandardItemComponent;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Builds the real stack for an item type, generating the captured tooltip: a rarity and type tag
 * line, the defense statistic behind its icon, and a tag line of every class that can use it.
 */
public final class RavengardItem {
    private static final TextColor DEFENSE_COLOR = TextColor.color(0x5FEC7B);
    private static final int DEFENSE_ICON = 0xE231;

    /** Class tag order as the captured Old Boots tooltip lists them. */
    private static final RavengardClass[] CLASS_TAG_ORDER = {
            RavengardClass.KNIGHT, RavengardClass.HUNTER,
            RavengardClass.ASSASSIN, RavengardClass.WARRIOR};

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

    private RavengardItem() {
    }

    public static ItemStack of(String id) {
        return of(id, null);
    }

    public static ItemStack of(String id, RavengardPlayer owner) {
        RavengardItemType type = RavengardItemRegistry.get(id);
        return type == null ? ItemStack.AIR : of(type, owner);
    }

    /** A display copy for menus: identical tooltip, but no tracked uuid is minted. */
    public static ItemStack.Builder displayBuilder(RavengardItemType type) {
        return build(type, null, false);
    }

    public static ItemStack of(RavengardItemType type, RavengardPlayer owner) {
        return build(type, owner, true).build();
    }

    public static List<Component> loreOf(RavengardItemType type) {
        return type.component(PlaceholderSlotComponent.class) != null ? placeholderLore(type) : lore(type);
    }

    private static ItemStack.Builder build(RavengardItemType type, RavengardPlayer owner, boolean tracked) {
        ItemStack.Builder builder = ItemStack.builder(type.getMaterial());

        if (type.getItemModel() != null) {
            builder.set(DataComponents.ITEM_MODEL, type.getItemModel());
        }

        boolean placeholder = type.component(PlaceholderSlotComponent.class) != null;
        builder.set(DataComponents.CUSTOM_NAME, Component.text(displayName(type))
                .color(placeholder ? NamedTextColor.GRAY : NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false));

        if (!placeholder) {
            builder.set(DataComponents.UNBREAKABLE, net.minestom.server.utils.Unit.INSTANCE);
            builder.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, HIDDEN));
            builder.set(DataComponents.TOOLTIP_STYLE, type.getRarity().tooltipStyle());
            if (tracked) {
                builder.set(DataComponents.CUSTOM_DATA, new CustomData(customData(type, owner)));
            }
        }

        for (RavengardItemComponent component : type.getComponents()) {
            builder = component.apply(builder, type);
        }

        List<Component> lore = placeholder ? placeholderLore(type) : lore(type);
        if (!lore.isEmpty()) {
            builder.set(DataComponents.LORE, lore);
        }
        return builder;
    }

    /**
     * Every non-placeholder item is unique: it carries the rarity-prefixed id plus a freshly
     * minted uuid, recorded to the tracker so the item has a history. The distinct uuid is also
     * what stops two of them stacking.
     */
    private static CompoundBinaryTag customData(RavengardItemType type, RavengardPlayer owner) {
        UUID itemUuid = UUID.randomUUID();
        String trackedId = type.getRarity().name() + "_" + type.getId();
        RavengardTrackedItemsDatabase.record(itemUuid, trackedId,
                owner == null ? null : owner.getUuid(),
                owner == null ? null : owner.getSelectedProfile());
        return CompoundBinaryTag.builder()
                .putString("id", trackedId)
                .putString("uuid", itemUuid.toString())
                .build();
    }

    private static String displayName(RavengardItemType type) {
        if (type.getDisplayName() != null) {
            return type.getDisplayName();
        }
        String[] words = type.getId().toLowerCase().split("_");
        StringBuilder name = new StringBuilder();
        for (String word : words) {
            if (!name.isEmpty()) {
                name.append(' ');
            }
            name.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return name.toString();
    }

    private static List<Component> lore(RavengardItemType type) {
        List<Component> lore = new ArrayList<>();

        StringBuilder tags = new StringBuilder();
        tags.appendCodePoint(type.getRarity().getTagGlyph());
        StandardItemComponent standard = type.component(StandardItemComponent.class);
        boolean consumable = standard != null && "CONSUMABLE".equals(standard.getStandardItemType());
        if (standard != null && standard.tagGlyph() != 0) {
            tags.append(' ').appendCodePoint(standard.tagGlyph());
        }
        lore.add(white(tags.toString()));
        lore.add(Component.empty());

        boolean stats = false;
        stats |= stat(lore, 0xE22F, type.statistic("damage"), "Damage");
        stats |= stat(lore, 0xE233, type.statistic("attack_speed"), "Attack Speed");
        stats |= stat(lore, DEFENSE_ICON, type.statistic("defense"), "Defense");
        if (stats) {
            lore.add(Component.empty());
        }

        if (!type.getDescription().isEmpty()) {
            type.getDescription().forEach(line ->
                    lore.add(Component.text(line).color(NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false)));
            lore.add(Component.empty());
        }
        if (type.getEffect() != null) {
            lore.add(white(highlightEffect(type.getEffect())));
            lore.add(Component.empty());
        }

        double value = type.statistic("value");
        if (value > 0) {
            lore.add(crowns((int) value, " Crowns"));
            lore.add(Component.empty());
        }

        if (!consumable) {
            StringBuilder classes = new StringBuilder();
            for (RavengardClass tag : CLASS_TAG_ORDER) {
                if (type.usableBy(tag)) {
                    classes.appendCodePoint(tag.getTagGlyph()).append(' ');
                }
            }
            lore.add(white(classes.toString()));
        }
        while (!lore.isEmpty() && Component.empty().equals(lore.getLast())) {
            lore.removeLast();
        }
        return lore;
    }

    private static boolean stat(List<Component> lore, int icon, double amount, String label) {
        if (amount <= 0) {
            return false;
        }
        String value = amount == Math.floor(amount) ? String.valueOf((int) amount) : String.valueOf(amount);
        lore.add(Component.text(new String(Character.toChars(icon)) + value)
                .color(DEFENSE_COLOR)
                .decoration(TextDecoration.ITALIC, false)
                .append(Component.text(" "))
                .append(Component.text(label).color(NamedTextColor.WHITE)));
        return true;
    }

    /** The crown glyph in white with the amount and trailing text in the gold the captures use. */
    public static Component crowns(int amount, String suffix) {
        return Component.text("\uD83D\uDC51")
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false)
                .append(Component.text(amount + suffix).color(TextColor.color(0xFFCE47)));
    }

    private static String highlightEffect(String effect) {
        return "\u00a77" + effect
                .replaceAll("([+-]?\\d+(?:\\.\\d+)? ?HP(?: over)?)", "\u00a7c$1\u00a77")
                .replaceAll("(\\d+ seconds?)", "\u00a7e$1\u00a77");
    }

    private static List<Component> placeholderLore(RavengardItemType type) {
        PlaceholderSlotComponent slot = type.component(PlaceholderSlotComponent.class);
        if (slot == null) {
            return List.of();
        }
        return List.of(Component.empty(), white(slot.getLoreOne()), white(slot.getLoreTwo()));
    }

    private static Component white(String text) {
        return Component.text(text)
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false);
    }
}

package net.swofty.type.ravengardgeneric.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.swofty.type.ravengardgeneric.item.components.PlaceholderSlotComponent;

import java.util.ArrayList;
import java.util.List;

/** Builds the real stack for an item type, generating lore from its statistics. */
public final class RavengardItem {
    private static final TextColor DEFENSE_COLOR = TextColor.color(0x5FEC7B);

    private RavengardItem() {
    }

    public static ItemStack of(String id) {
        RavengardItemType type = RavengardItemRegistry.get(id);
        return type == null ? ItemStack.AIR : of(type);
    }

    public static ItemStack of(RavengardItemType type) {
        ItemStack.Builder builder = ItemStack.builder(type.getMaterial());

        if (type.getItemModel() != null) {
            builder.set(DataComponents.ITEM_MODEL, type.getItemModel());
        }
        boolean placeholder = type.component(PlaceholderSlotComponent.class) != null;
        builder.set(DataComponents.CUSTOM_NAME, Component.text(displayName(type))
                .color(placeholder ? NamedTextColor.GRAY : NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false));

        for (RavengardItemComponent component : type.getComponents()) {
            builder = component.apply(builder, type);
        }

        List<Component> lore = lore(type);
        if (!lore.isEmpty()) {
            builder.set(DataComponents.LORE, lore);
        }
        return builder.build();
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

        PlaceholderSlotComponent placeholder = type.component(PlaceholderSlotComponent.class);
        if (placeholder != null) {
            lore.add(Component.empty());
            lore.add(plain(placeholder.getLoreOne()));
            lore.add(plain(placeholder.getLoreTwo()));
            return lore;
        }

        double defense = type.statistic("defense");
        if (defense > 0) {
            lore.add(plain(" "));
            lore.add(Component.empty());
            lore.add(Component.text(String.valueOf((int) defense))
                    .color(DEFENSE_COLOR)
                    .decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(" "))
                    .append(Component.text("Defense").color(NamedTextColor.WHITE)));
            lore.add(Component.empty());
            lore.add(plain(" "));
        }
        return lore;
    }

    private static Component plain(String text) {
        return Component.text(text)
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.ITALIC, false);
    }
}

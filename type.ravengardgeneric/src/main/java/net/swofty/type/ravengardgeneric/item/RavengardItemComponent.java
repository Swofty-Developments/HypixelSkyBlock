package net.swofty.type.ravengardgeneric.item;

import net.minestom.server.item.ItemStack;

import java.util.Map;

/**
 * A behaviour attached to an item by its config. Components apply themselves to the stack as it is
 * built, so a new one can be added without touching the parser.
 */
public interface RavengardItemComponent {

    String id();

    default void configure(Map<String, Object> config) {
    }

    default ItemStack.Builder apply(ItemStack.Builder builder, RavengardItemType type) {
        return builder;
    }
}

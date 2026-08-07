package net.swofty.type.ravengardgeneric.item.attribute;

import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class RavengardItemAttribute<T> {
    private static final List<RavengardItemAttribute<?>> attributes = new ArrayList<>();

    public abstract String getKey();

    public abstract Tag<T> getTag();

    public abstract T getDefaultValue();

    public T from(ItemStack stack) {
        T value = stack.getTag(getTag());
        return value == null ? getDefaultValue() : value;
    }

    public ItemStack.Builder apply(ItemStack.Builder builder, T value) {
        builder.setTag(getTag(), value);
        return builder;
    }

    public static void registerItemAttributes() {
        ItemAttribute.loopThroughPackage(
                "net.swofty.type.ravengardgeneric.item.attribute.attributes",
                RavengardItemAttribute.class
        ).forEach(attributes::add);
    }

    public static Collection<RavengardItemAttribute<?>> getPossibleAttributes() {
        return new ArrayList<>(attributes);
    }
}

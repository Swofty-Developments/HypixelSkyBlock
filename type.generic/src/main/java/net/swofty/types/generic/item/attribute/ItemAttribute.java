package net.swofty.types.generic.item.attribute;

import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ItemAttribute<T> {
    private static final ArrayList<ItemAttribute> attributes = new ArrayList<>();

    public T value;

    public ItemAttribute() {
        value = getDefaultValue(null);
    }

    public abstract String getKey();

    public abstract T getDefaultValue(@Nullable Class<? extends CustomSkyBlockItem> itemClass);

    public abstract T loadFromString(String string);

    public abstract String saveIntoString();

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static void registerItemAttributes() {
        SkyBlockGenericLoader.loopThroughPackage(
                "net.swofty.types.generic.item.attribute.attributes", ItemAttribute.class
        ).forEach(attributes::add);
    }

    public static Collection<ItemAttribute> getPossibleAttributes() {
        return (Collection<ItemAttribute>) attributes.stream().map(attributeClass -> {
            try {
                return attributeClass.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}

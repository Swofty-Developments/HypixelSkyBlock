package net.swofty.commons.skyblock.item.attribute;

import net.swofty.commons.skyblock.SkyBlock;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ItemAttribute<T> {
    private static final ArrayList<ItemAttribute> attributes = new ArrayList<>();

    public T value;

    protected ItemAttribute() {
        value = getDefaultValue();
    }

    public abstract String getKey();

    public abstract T getDefaultValue();

    public abstract T loadFromString(String string);

    public abstract String saveIntoString();

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static void registerItemAttributes() {
        SkyBlock.loopThroughPackage(
                "net.swofty.commons.skyblock.item.attribute.attributes", ItemAttribute.class
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

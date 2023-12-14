package net.swofty.item.attribute;

import net.swofty.event.SkyBlockEvent;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ItemAttribute<T> {
    private static ArrayList<ItemAttribute> attributes = new ArrayList<>();

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
        Reflections eventReflection = new Reflections("net.swofty.item.attribute.attributes");
        for (Class<? extends ItemAttribute> l : eventReflection.getSubTypesOf(ItemAttribute.class)) {
            try {
                ItemAttribute itemAttribute = l.newInstance();
                attributes.add(itemAttribute);
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
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

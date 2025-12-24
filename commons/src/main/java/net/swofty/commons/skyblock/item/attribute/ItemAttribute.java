package net.swofty.commons.skyblock.item.attribute;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Setter
@Getter
public abstract class ItemAttribute<T> {
    private static final ArrayList<ItemAttribute> attributes = new ArrayList<>();

    public T value;

    public ItemAttribute() {
        value = getDefaultValue(null);
    }

    public abstract String getKey();

    public abstract T getDefaultValue(@Nullable ItemStatistics defaultStatistics);

    public abstract T loadFromString(String string);

    public abstract String saveIntoString();

    public static void registerItemAttributes() {
        loopThroughPackage(
                "net.swofty.commons.skyblock.item.attribute.attributes", ItemAttribute.class
        ).forEach(attributes::add);
    }

    public static Collection<ItemAttribute> getPossibleAttributes() {
        // Create a new list to store the results
        List<ItemAttribute> result = new ArrayList<>();

        // Iterate safely using a traditional for loop
        for (ItemAttribute attribute : attributes) {
            try {
                result.add(attribute.getClass().getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    public static <T> Stream<T> loopThroughPackage(String packageName, Class<T> clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);

        return subTypes.stream()
                .map(subClass -> {
                    try {
                        return clazz.cast(subClass.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull);
    }
}

package net.swofty.collection;

import lombok.Getter;
import net.swofty.collection.collections.FarmingCollection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum CollectionCategories {
    FARMING(FarmingCollection.class),
    ;

    private final Class<? extends CollectionCategory> clazz;

    CollectionCategories(Class<? extends CollectionCategory> clazz) {
        this.clazz = clazz;
    }

    public static ArrayList<CollectionCategory> getCategories() {
        return new ArrayList<>(Arrays.stream(values()).map(CollectionCategories::getClazz).map(clazz -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));
    }
}

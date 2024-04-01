package net.swofty.types.generic.collection;

import lombok.Getter;
import net.swofty.types.generic.collection.collections.*;
import net.swofty.types.generic.item.ItemType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public enum CollectionCategories {
    FARMING(FarmingCollection.class),
    MINING(MiningCollection.class),
    COMBAT(CombatCollection.class),
    FORAGING(ForagingCollection.class),
    FISHING(FishingCollection.class),
    BOSS(BossCollection.class),
    ;

    private final Class<? extends CollectionCategory> clazz;

    CollectionCategories(Class<? extends CollectionCategory> clazz) {
        this.clazz = clazz;
    }

    public static @Nullable CollectionCategory getCategory(ItemType type) {
        return getCategories().stream().filter(category -> Arrays.stream(category.getCollections()).anyMatch(collection -> collection.type() == type)).findFirst().orElse(null);
    }

    public static ArrayList<CollectionCategory> getCategories() {
        return new ArrayList<>(Arrays.stream(values()).map(CollectionCategories::getClazz).map(clazz -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));
    }
}

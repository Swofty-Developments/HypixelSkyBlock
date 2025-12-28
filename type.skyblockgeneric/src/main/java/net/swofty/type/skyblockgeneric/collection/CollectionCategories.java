package net.swofty.type.skyblockgeneric.collection;

import lombok.Getter;
import net.swofty.commons.skyblock.item.ItemType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public enum CollectionCategories {
    FARMING("Farming"),
    MINING("Mining"),
    COMBAT("Combat"),
    FORAGING("Foraging"),
    FISHING("Fishing"),
    BOSS("Boss"),
    ;

    private final String file;
    private CollectionCategory category;

    CollectionCategories(String file) {
        this.file = file;
    }

    public CollectionCategory getCategory() {
        if (category == null) {
            category = CollectionLoader.loadFromFile(file);
        }
        return category;
    }

    public static @Nullable CollectionCategory getCategory(ItemType type) {
        return getCategories().stream().filter(category -> Arrays.stream(category.getCollections()).anyMatch(collection ->
                collection.type() == type)).findFirst().orElse(null);
    }

    public static ArrayList<CollectionCategory> getCategories() {
        return Arrays.stream(values()).map(CollectionCategories::getCategory).collect(Collectors.toCollection(ArrayList::new));
    }
}

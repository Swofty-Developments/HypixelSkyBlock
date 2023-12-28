package net.swofty.item.impl;

import net.swofty.item.ItemType;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.recipes.ShapelessRecipe;
import net.swofty.user.statistics.ItemStatistics;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface Enchanted extends CustomSkyBlockItem, Craftable {
    @Override
    default ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    ItemType getCraftingMaterial();

    @Override
    default SkyBlockRecipe<?> getRecipe() {
        List<ItemType> matchTypes = Arrays.stream(ItemType.values())
                .filter(itemType -> itemType.clazz != null)
                .filter(itemType -> itemType.clazz.equals(this.getClass()))
                .toList();

        if (matchTypes.isEmpty()) {
            throw new RuntimeException("No matching ItemType found");
        } else {
            return new ShapelessRecipe(new SkyBlockItem(matchTypes.get(0)))
                    .add(this.getCraftingMaterial(), 64)
                    .add(this.getCraftingMaterial(), 64)
                    .add(this.getCraftingMaterial(), 64)
                    .add(this.getCraftingMaterial(), 64)
                    .add(this.getCraftingMaterial(), 32);
        }
    }
}

package net.swofty.types.generic.item.impl;

import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.List;

public interface Enchanted extends CustomSkyBlockItem , DisableAnimationImpl{
    @Override
    default ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    default SkyBlockRecipe<?> getStandardEnchantedRecipe(SkyBlockRecipe.RecipeType type, ItemType craftingMaterial) {
        return SkyBlockRecipe.getStandardEnchantedRecipe(this.getClass(), type, craftingMaterial);
    }

    @Override
    default List<PlayerItemAnimationEvent.ItemAnimationType> getDisabledAnimations(){
        return List.of(PlayerItemAnimationEvent.ItemAnimationType.EAT);
    }
}

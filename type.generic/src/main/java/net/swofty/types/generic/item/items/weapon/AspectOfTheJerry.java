package net.swofty.types.generic.item.items.weapon;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.sound.SoundEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AspectOfTheJerry implements CustomSkyBlockItem, CustomSkyBlockAbility, StandardItem, DefaultCraftable {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.MOVE_JERRY, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.STICK, 1));
        List<String> pattern = List.of(
                "A",
                "A",
                "B");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.ASPECT_OF_THE_JERRY), ingredientMap, pattern);
    }

    @Override
    public String getAbilityName() {
        return "Parley";
    }

    @Override
    public String getAbilityDescription() {
        return "§7Channel your inner Jerry";
    }

    @Override
    public void onAbilityUse(SkyBlockPlayer player, SkyBlockItem sItem) {
        player.playSound(Sound.sound(SoundEvent.ENTITY_VILLAGER_YES, Sound.Source.RECORD, 1f, 1f));
    }

    @Override
    public int getManaCost() {
        return 0;
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 5;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.RIGHT_CLICK;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 1D)
                .build();
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }
}

package net.swofty.types.generic.item.items.weapon;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.sound.SoundEvent;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AspectOfTheJerry implements CustomSkyBlockItem, CustomSkyBlockAbility, StandardItem, DefaultCraftable {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.MOVE_JERRY, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.STICK, 1));
        List<String> pattern = List.of(
                "A",
                "A",
                "B");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemTypeLinker.ASPECT_OF_THE_JERRY), ingredientMap, pattern);
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

    @Override
    public List<Ability> getAbilities() {
        return List.of(
                new Ability() {
                    @Override
                    public @NotNull String getName() {
                        return "Parley";
                    }

                    @Override
                    public @NotNull String getDescription() {
                        return "§7Channel your inner Jerry";
                    }

                    @Override
                    public @NotNull AbilityActivation getAbilityActivation() {
                        return AbilityActivation.RIGHT_CLICK;
                    }

                    @Override
                    public int getCooldownTicks() {
                        return 100;
                    }

                    @Override
                    public @NotNull AbilityCost getAbilityCost() {
                        return new NoAbilityCost();
                    }

                    @Override
                    public void onUse(@NotNull SkyBlockPlayer player, @NotNull SkyBlockItem sItem) {
                        player.playSound(Sound.sound(SoundEvent.ENTITY_VILLAGER_YES, Sound.Source.RECORD, 1f, 1f));
                    }
                }
        );
    }
}

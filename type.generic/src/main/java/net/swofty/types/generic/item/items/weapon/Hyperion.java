package net.swofty.types.generic.item.items.weapon;

import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.museum.MuseumableItemCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hyperion implements CustomSkyBlockItem, CustomSkyBlockAbility, StandardItem,
        DefaultCraftable, GemstoneItem, NotFinishedYet, Museumable, TrackedUniqueItem {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 100D)
                .withBase(ItemStatistic.HEALTH, 20D)
                .withBase(ItemStatistic.DEFENSE, 30D)
                .withBase(ItemStatistic.SPEED, 50D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList("This item literally comes", "out of your mum and", "says §aHELLO §7lmao."));
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.COMBAT, new SkyBlockItem(ItemTypeLinker.HYPERION), (player -> {
            if (player.getLevel() > 10) {
                return new SkyBlockRecipe.CraftingResult(true, null);
            } else {
                return new SkyBlockRecipe.CraftingResult(false, new String[]{"§cLevel Issue", "§7You must be at least §eLevel 10 §7to craft this item!"});
            }
        })).add(ItemType.DIRT, 10)
                .add(ItemTypeLinker.IRON_PICKAXE, 1);
    }

    @Override
    public List<GemstoneItemSlot> getGemstoneSlots() {
        return List.of(
                new GemstoneItemSlot(Gemstone.Slots.SAPPHIRE, 250000),
                new GemstoneItemSlot(Gemstone.Slots.COMBAT, 250000)
        );
    }
    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }

    @Override
    public MuseumableItemCategory getMuseumCategory() {
        return MuseumableItemCategory.WEAPONS;
    }

    @Override
    public List<Ability> getAbilities() {
        return List.of(
                new Ability() {
                    @Override
                    public @NotNull String getName() {
                        return "Wither Impact";
                    }

                    @Override
                    public @NotNull String getDescription() {
                        return "§7Teleports §a10 Blocks §7ahead of you. Then implode dealing §c10000 §7damage to nearby enemies. Also applies the wither shield scroll ability reducing mobdamage taken and granting an absorption shield for §e5 §7seconds.";
                    }

                    @Override
                    public @NotNull AbilityActivation getAbilityActivation() {
                        return AbilityActivation.RIGHT_CLICK;
                    }

                    @Override
                    public int getCooldownTicks() {
                        return 50;
                    }

                    @Override
                    public @NotNull AbilityCost getAbilityCost() {
                        return new AbilityManaCost(25);
                    }

                    @Override
                    public void onUse(@NotNull SkyBlockPlayer player, @NotNull SkyBlockItem sItem) {
                        player.sendMessage("Hey");
                    }
                }
        );
    }
}

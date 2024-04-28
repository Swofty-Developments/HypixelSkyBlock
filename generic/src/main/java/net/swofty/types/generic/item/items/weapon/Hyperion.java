package net.swofty.types.generic.item.items.weapon;

import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.museum.MuseumableItemCategory;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

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
    public String getAbilityName() {
        return "Wither Impact";
    }

    @Override
    public String getAbilityDescription() {
        return "§7Teleports §a10 Blocks §7ahead of you. Then implode dealing §c10000 §7damage to nearby enemies. Also applies the wither shield scroll ability reducing mobdamage taken and granting an absorption shield for §e5 §7seconds.";
    }

    @Override
    public void onAbilityUse(SkyBlockPlayer player, SkyBlockItem sItem) {
        player.sendMessage("Hey");
    }

    @Override
    public int getManaCost() {
        return 25;
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 60;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.RIGHT_CLICK;
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        return new ShapelessRecipe(SkyBlockRecipe.RecipeType.COMBAT, new SkyBlockItem(ItemType.HYPERION), (player -> {
            if (player.getLevel() > 10) {
                return new SkyBlockRecipe.CraftingResult(true, null);
            } else {
                return new SkyBlockRecipe.CraftingResult(false, new String[]{"§cLevel Issue", "§7You must be at least §eLevel 10 §7to craft this item!"});
            }
        })).add(ItemType.DIRT, 10)
                .add(ItemType.IRON_PICKAXE, 1);
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
}

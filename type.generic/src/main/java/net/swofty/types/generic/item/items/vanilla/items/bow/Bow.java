package net.swofty.types.generic.item.items.vanilla.items.bow;

import net.minestom.server.coordinate.Vec;
import net.swofty.types.generic.entity.ArrowEntityImpl;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.BowImpl;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.DefaultCraftable;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bow implements CustomSkyBlockItem, BowImpl, DefaultCraftable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 30D)
                .build();
    }

    @Override
    public boolean shouldBeArrow() {
        return true;
    }

    @Override
    public void onBowShoot(SkyBlockPlayer player, SkyBlockItem item) {
        SkyBlockItem arrow = player.getAndConsumeArrow();
        if (arrow == null) return;

        ArrowEntityImpl arrowEntity = new ArrowEntityImpl(player, item);
        Vec arrowVelocity = calculateArrowVelocity(
                player.getPosition().pitch(),
                player.getPosition().yaw());
        arrowEntity.setVelocity(arrowVelocity);
        arrowEntity.setInstance(player.getInstance(), calculateArrowSpawnPosition(player));
    }

    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.STRING, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.STICK, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " AB",
                "A B",
                " AB");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.BOW), ingredientMap, pattern);
    }
}

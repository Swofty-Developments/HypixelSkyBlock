package net.swofty.types.generic.item.items.combat.slayer.enderman.craftable;

import net.minestom.server.coordinate.Vec;
import net.swofty.types.generic.entity.ArrowEntityImpl;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.*;

public class JujuShortbow implements CustomSkyBlockItem, Craftable, BowImpl {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.ENCHANTED_EYE_OF_ENDER, 32));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.ENCHANTED_STRING, 64));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.NULL_OVOID, 32));
        ingredientMap.put('D', new MaterialQuantifiable(ItemType.ENCHANTED_QUARTZ_BLOCK, 32));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " AB",
                "C B",
                " DB");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.SLAYER, new SkyBlockItem(ItemType.JUJU_SHORTBOW), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 310D)
                .with(ItemStatistic.STRENGTH, 40D)
                .with(ItemStatistic.CRIT_CHANCE, 10D)
                .with(ItemStatistic.CRIT_DAMAGE, 110D)
                //.with(ItemStatistic.SHOT_COOLDOWN, 0.5s)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Hits §c3 §7mobs on impact.",
                "§7Can damage endermen.",
                "",
                "§5Shortbow: Instantly shoots!"));
    }

    @Override
    public boolean shouldBeArrow() {
        return false;
    }

    @Override
    public void onBowShoot(SkyBlockPlayer player, SkyBlockItem item) {
        SkyBlockItem arrow = player.getAndConsumeArrow();
        if (arrow == null) return;

        ArrowEntityImpl arrowEntity = new ArrowEntityImpl(player);
        Vec arrowVelocity = calculateArrowVelocity(
                player.getPosition().pitch(),
                player.getPosition().yaw());
        arrowEntity.setVelocity(arrowVelocity);
        arrowEntity.setInstance(player.getInstance(), player.getPosition().add(0, 1, 0));
    }
}

package net.swofty.types.generic.item.items.combat.slayer.zombie.craftable;

import net.minestom.server.entity.EntityType;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.PlayerDamageMobValueUpdateEvent;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;

import java.util.*;
import net.swofty.commons.item.ItemType;

public class RevenantFalchion extends SkyBlockValueEvent implements CustomSkyBlockItem, DefaultCraftable, StandardItem, NotFinishedYet {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemTypeLinker.CRYSTALLIZED_HEART, 1));
        ingredientMap.put('B', new MaterialQuantifiable(ItemTypeLinker.UNDEAD_CATALYST, 1));
        ingredientMap.put('C', new MaterialQuantifiable(ItemTypeLinker.UNDEAD_SWORD, 1));
        ingredientMap.put(' ', new MaterialQuantifiable(ItemType.AIR, 1));
        List<String> pattern = List.of(
                " A ",
                " B ",
                " C ");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.REVENANT_HORROR, new SkyBlockItem(ItemTypeLinker.REAPER_FALCHION), ingredientMap, pattern);
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 90D)
                .withBase(ItemStatistic.STRENGTH, 50D)
                .withBase(ItemStatistic.INTELLIGENCE, 100D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "§7Deals §a+150% §7damage to",
                "§7§7Zombies§7."));
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }

    @Override
    public Class<? extends ValueUpdateEvent> getValueEvent() {
        return PlayerDamageMobValueUpdateEvent.class;
    }

    @Override
    public void run(ValueUpdateEvent tempEvent) {
        PlayerDamageMobValueUpdateEvent event = (PlayerDamageMobValueUpdateEvent) tempEvent;
        SkyBlockPlayer player = event.getPlayer();
        SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());
        if(item.isNA() || item.isAir()) return;
        if(item.getAttributeHandler().getPotentialClassLinker() != ItemTypeLinker.REVENANT_FALCHION) return;
        if (event.getMob().getEntityType() == EntityType.ZOMBIE || event.getMob().getEntityType() == EntityType.ZOMBIFIED_PIGLIN || event.getMob().getEntityType() == EntityType.SKELETON || event.getMob().getEntityType() == EntityType.WITHER_SKELETON) {
            event.setValue((((float) event.getValue()) *2.5));
        }
    }
}

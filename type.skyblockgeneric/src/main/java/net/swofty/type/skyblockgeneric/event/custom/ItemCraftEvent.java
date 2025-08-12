package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.generic.data.datapoints.DatapointMinionData;
import net.swofty.type.generic.item.SkyBlockItem;
import net.swofty.type.generic.item.components.MinionComponent;
import net.swofty.type.generic.item.crafting.SkyBlockRecipe;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class ItemCraftEvent implements PlayerInstanceEvent {
    private final HypixelPlayer player;
    private final SkyBlockItem craftedItem;
    private final SkyBlockRecipe<?> recipe;

    public ItemCraftEvent(HypixelPlayer player, SkyBlockItem craftedItem, SkyBlockRecipe<?> recipe) {
        this.player = player;
        this.craftedItem = craftedItem;
        this.recipe = recipe;

        if (craftedItem.hasComponent(MinionComponent.class)) {
            DatapointMinionData.ProfileMinionData playerData = player.getSkyBlockData().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.MINION_DATA, DatapointMinionData.class).getValue();
            String name = craftedItem.getAttributeHandler().getMinionType().name();
            Integer tier = craftedItem.getAttributeHandler().getMinionData().tier();
            AbstractMap.SimpleEntry<String, List<Integer>> minion = new AbstractMap.SimpleEntry<>(name, List.of(tier));
            boolean minionTypeInList = false;

            if (playerData.craftedMinions().isEmpty()) {
                playerData.craftedMinions().add(minion);
                return;
            }

            for (int i = 0; i < playerData.craftedMinions().size(); i++) {
                if (Objects.equals(playerData.craftedMinions().get(i).getKey(), name)) {
                    for (Integer integer : playerData.craftedMinions().get(i).getValue()) {
                        if (Objects.equals(integer, tier)) return;
                    }
                    List<Integer> tiers = new ArrayList<>(playerData.craftedMinions().get(i).getValue());
                    tiers.add(tier);
                    minion.setValue(tiers);
                    playerData.craftedMinions().remove(playerData.craftedMinions().get(i));
                    playerData.craftedMinions().add(minion);
                    minionTypeInList = true;
                }
            }

            if (!minionTypeInList) {
                playerData.craftedMinions().add(minion);
            }
        }
    }
}

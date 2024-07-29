package net.swofty.types.generic.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMinionData;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.item.impl.SkyBlockRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.*;

@Getter
public class ItemCraftEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    private final SkyBlockItem craftedItem;
    private final SkyBlockRecipe<?> recipe;

    public ItemCraftEvent(SkyBlockPlayer player, SkyBlockItem craftedItem, SkyBlockRecipe<?> recipe) {
        this.player = player;
        this.craftedItem = craftedItem;
        this.recipe = recipe;

        if (craftedItem.getGenericInstance() instanceof Minion) {
            DatapointMinionData.ProfileMinionData playerData = player.getDataHandler().get(DataHandler.Data.MINION_DATA, DatapointMinionData.class).getValue();
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
                    System.out.println(playerData.craftedMinions());
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

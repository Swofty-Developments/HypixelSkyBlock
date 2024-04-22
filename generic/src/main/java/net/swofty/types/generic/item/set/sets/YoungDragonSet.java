package net.swofty.types.generic.item.set.sets;

import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.item.set.impl.SetEvents;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.user.statistics.TemporaryConditionalStatistic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class YoungDragonSet implements ArmorSet, SetEvents {

    private static final List<UUID> wearing = new ArrayList<>();

    @Override
    public String getName() {
        return "Young Blood";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "§7Gain §a+70% §7Walk Speed while you are",
                "§7above §a50% §7HP",
                "§8+100 Walk Speed Cap");
    }

    @Override
    public void setPutOn(SkyBlockPlayer player) {
        wearing.add(player.getUuid());
        double maxHealth = player.getMaxHealth();

        player.getStatistics().boostStatistic(TemporaryConditionalStatistic.builder()
                .withExpiry((newPlayer) -> wearing.contains(newPlayer.getUuid()))
                .withStatistics((updatedPlayer) -> {
                    ItemStatistics.Builder toReturn = ItemStatistics.builder();
                    if(updatedPlayer.getHealth() > maxHealth / 2) {
                        toReturn.withMultiplicativePercentage(ItemStatistic.SPEED, 70D);
                    }
                    return toReturn.build();
                }).build());
    }

    @Override
    public void setTakeOff(SkyBlockPlayer player) {
        wearing.remove(player.getUuid());
    }
}

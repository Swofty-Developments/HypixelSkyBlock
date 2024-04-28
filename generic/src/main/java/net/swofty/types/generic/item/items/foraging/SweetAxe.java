package net.swofty.types.generic.item.items.foraging;

import net.minestom.server.event.Event;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.NotFinishedYet;
import net.swofty.types.generic.item.impl.Reforgable;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@EventParameters(description = "drops apple",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class SweetAxe extends SkyBlockEvent implements CustomSkyBlockItem, Reforgable, NotFinishedYet {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 25D)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList(
                "20% chance of dropping an Apple",
                "when chopping logs."
        ));
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.AXES;
    }

    @Override
    public Class<? extends Event> getEvent() {
        return CustomBlockBreakEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        CustomBlockBreakEvent event = (CustomBlockBreakEvent) tempEvent;
        SkyBlockPlayer player = event.getPlayer();
        if (!(event.getMaterial().block() == Material.OAK_LOG.block() || event.getMaterial().block() == Material.DARK_OAK_LOG.block() || event.getMaterial().block() == Material.SPRUCE_LOG.block())) return;
        Random r = new Random();
        int percentage = r.nextInt(101);
        if (percentage == 20) {
            player.addAndUpdateItem(ItemStack.of(Material.APPLE, 1));
        }
    }
}

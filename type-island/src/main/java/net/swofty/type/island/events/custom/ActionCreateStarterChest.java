package net.swofty.type.island.events.custom;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.chest.ChestBuilder;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.IslandFirstCreatedEvent;
import net.swofty.types.generic.utility.MathUtility;

@EventParameters(description = "Handles creating starter chest on the players Island",
        node = EventNodes.CUSTOM,
        requireDataLoaded = false)
public class ActionCreateStarterChest extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return IslandFirstCreatedEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        IslandFirstCreatedEvent event = (IslandFirstCreatedEvent) tempEvent;
        MathUtility.delay(()->{
            new ChestBuilder(event.getIsland().getIslandInstance(), new Pos(3 , 93 , 30))
                    .setItem(0 , ItemStack.of(Material.DIRT , 5))
                    .setItem(1 , ItemStack.of(Material.GRASS_BLOCK , 7))
                    .setItem(2 , ItemStack.of(Material.COBBLESTONE , 8))
                    .setItem(3 , ItemStack.of(Material.WATER_BUCKET))
                    .setItem(4 , ItemStack.of(Material.LAVA_BUCKET))
                    .setItem(5 , ItemStack.of(Material.BONE_MEAL , 3))
                    .build();
        } , 60);

    }
}

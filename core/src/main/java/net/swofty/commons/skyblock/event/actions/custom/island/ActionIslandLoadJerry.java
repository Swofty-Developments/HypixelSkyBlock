package net.swofty.commons.skyblock.event.actions.custom.island;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.entity.hologram.ServerHolograms;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.custom.IslandFetchedFromDatabaseEvent;
import net.swofty.commons.skyblock.utility.MathUtility;
import org.bson.Document;

@EventParameters(description = "Handles loading Jerry on the players Island",
        node = EventNodes.CUSTOM,
        validLocations = EventParameters.Location.EITHER,
        requireDataLoaded = false)
public class ActionIslandLoadJerry extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return IslandFetchedFromDatabaseEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        IslandFetchedFromDatabaseEvent event = (IslandFetchedFromDatabaseEvent) tempEvent;

        Document document = event.getIsland().getDatabase().getDocument();

        if (document != null && document.getDouble("jerry_position_x") != null) {
            event.getIsland().setJerryPosition(
                    new Pos(
                            document.getDouble("jerry_position_x"),
                            document.getDouble("jerry_position_y"),
                            document.getDouble("jerry_position_z"),
                            MathUtility.fromDouble(document.getDouble("jerry_position_yaw")),
                            MathUtility.fromDouble(document.getDouble("jerry_position_pitch"))
                    )
            );
        }

        Entity jerry = new Entity(EntityType.VILLAGER);
        jerry.setInstance(
                event.getIsland().getIslandInstance(),
                event.getIsland().getJerryPosition()
        );

        ServerHolograms.addExternalHologram(ServerHolograms.ExternalHologram.builder()
                .text(new String[]{"§6§lNEW UPDATE", "Jerry", "§e§lCLICK"})
                .instance(event.getIsland().getIslandInstance())
                .pos(event.getIsland().getJerryPosition().add(0, 1, 0))
                .build());

        event.getMembersOnline().forEach(player -> {
            jerry.addViewer(player);
            jerry.updateNewViewer(player);
        });
    }
}

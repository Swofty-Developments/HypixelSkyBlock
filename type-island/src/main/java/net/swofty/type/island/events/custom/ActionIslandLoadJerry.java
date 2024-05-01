package net.swofty.type.island.events.custom;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.swofty.types.generic.entity.hologram.ServerHolograms;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.utility.JerryInformation;
import net.swofty.types.generic.utility.MathUtility;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.IslandFetchedFromDatabaseEvent;
import org.bson.Document;

public class ActionIslandLoadJerry implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(IslandFetchedFromDatabaseEvent event) {
        Document document = event.getIsland().getDatabase().getDocument();
        JerryInformation jerryInformation = event.getIsland().getJerryInformation();

        if (jerryInformation == null) {
            jerryInformation = new JerryInformation(null, null, null);
        }

        if (document != null && document.getDouble("jerry_position_x") != null) {
            jerryInformation.setJerryPosition(
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
                jerryInformation.getJerryPosition()
        );

        jerryInformation.setJerry(jerry);

        ServerHolograms.ExternalHologram hologram = ServerHolograms.ExternalHologram.builder()
                .text(new String[]{"§6§lNEW UPDATE", "Jerry", "§e§lCLICK"})
                .instance(event.getIsland().getIslandInstance())
                .pos(jerryInformation.getJerryPosition().add(0, 1, 0))
                .build();

        ServerHolograms.addExternalHologram(hologram);

        jerryInformation.setHologram(hologram);

        event.getIsland().setJerryInformation(jerryInformation);

        event.getMembersOnline().forEach(player -> {
            jerry.addViewer(player);
            jerry.updateNewViewer(player);
        });
    }
}

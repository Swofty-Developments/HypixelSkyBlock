package net.swofty.type.island.events.custom;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.swofty.type.generic.entity.hologram.ServerHolograms;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.skyblockgeneric.event.custom.IslandFetchedFromDatabaseEvent;
import net.swofty.type.skyblockgeneric.utility.JerryInformation;
import org.bson.Document;

public class ActionIslandLoadJerry implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
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
        } else {
            jerryInformation.setJerryPosition(
                    new Pos(2.5, 100, 24.5, 145, 0)
            );
        }

        Entity jerry = new Entity(EntityType.VILLAGER);
        jerry.setAutoViewable(true);
        jerry.setInstance(
                event.getIsland().getIslandInstance(),
                jerryInformation.getJerryPosition()
        );
        jerry.setNoGravity(true);

        jerryInformation.setJerry(jerry);

        ServerHolograms.ExternalHologram hologram = ServerHolograms.ExternalHologram.builder()
                .text(new String[]{"§6§lNEW UPDATE", "Jerry", "§e§lCLICK"})
                .instance(event.getIsland().getIslandInstance())
                .pos(jerryInformation.getJerryPosition().add(0, 1, 0))
                .build();

        ServerHolograms.addExternalHologram(hologram);

        jerryInformation.setHologram(hologram);

        event.getIsland().setJerryInformation(jerryInformation);
    }
}

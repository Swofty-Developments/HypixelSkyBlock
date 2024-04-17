package net.swofty.types.generic.item.items.miscellaneous;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.hologram.ServerHolograms;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Interactable;
import net.swofty.types.generic.user.SkyBlockIsland;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.JerryInformation;

import java.util.ArrayList;
import java.util.List;

public class MoveJerry implements CustomSkyBlockItem, Interactable {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public void onLeftInteract(SkyBlockPlayer player, SkyBlockItem item) {
        if (!SkyBlockConst.isIslandServer()) {
            player.sendMessage("§cYou can't move Jerry here! He doesn't belong here!");
            return;
        }

        Point position = player.getTargetBlockPosition(5);
        if (position == null) {
            return;
        }

        // Move up one block and center jerry
        position = position.add(0.5, 1, 0.5);

        SkyBlockIsland island = player.getSkyBlockIsland();
        JerryInformation jerryInformation = island.getJerryInformation();

        ServerHolograms.ExternalHologram hologram = jerryInformation.getHologram();
        ServerHolograms.removeExternalHologram(hologram);

        jerryInformation.setJerryPosition(new Pos(position).withLookAt(player.getPosition()));
        jerryInformation.getJerry().teleport(jerryInformation.getJerryPosition());
        jerryInformation.getJerry().lookAt(player.getPosition().add(0, 1.4, 0));

        hologram = ServerHolograms.ExternalHologram.builder()
                .text(hologram.getText())
                .instance(hologram.getInstance())
                .pos(jerryInformation.getJerryPosition().add(0, 1, 0))
                .build();

        ServerHolograms.addExternalHologram(hologram);
        jerryInformation.setHologram(hologram);
    }

    @Override
    public List<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(List.of(
                "§7Place this where you would like",
                "§7Jerry to move to!"
        ));
    }
}

package net.swofty.type.skyblockgeneric.utility;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.swofty.type.generic.entity.hologram.ServerHolograms;

import java.util.ArrayList;

public class WarpPortal {

    @Getter
    private static ArrayList<WarpPortalData> warpPortals = new ArrayList<>();

    public static void create(Instance instance, BlockVec vector, Component text, Pos pos) {
        ServerHolograms.addExternalHologram(ServerHolograms.ExternalHologram.builder().pos(vector.asPos().add(0.5, 2, 0.5)).instance(instance).text(new String[]{
            "§5✦ §dWarp To §b" + LegacyComponentSerializer.legacySection().serialize(text)
        }).build());
        warpPortals.add(new WarpPortalData(vector, text, pos));
    }

    public record WarpPortalData(BlockVec vector, Component text, Pos pos) {
    }

}

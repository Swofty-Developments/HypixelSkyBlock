package net.swofty.types.generic.item.items.farming;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.Material;
import net.swofty.types.generic.entity.ServerOrbImpl;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Placeable;
import net.swofty.types.generic.item.impl.ServerOrb;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class FarmingOrb implements CustomSkyBlockItem, SkullHead, Placeable, ServerOrb {
    @Override
    public ItemStatistics getStatistics() {
            return ItemStatistics.EMPTY;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "557b354ce9bf8db0a7ed2afc79e27f28c3ccaaaf2c460a51993cac2b0564ef60";
    }

    @Override
    public void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item) {
        new ServerOrbImpl(Material.WHEAT_SEEDS, "557b354ce9bf8db0a7ed2afc79e27f28c3ccaaaf2c460a51993cac2b0564ef60").setInstance(
                player.getInstance(), event.getBlockPosition().add(0, 1, 0)
        );
    }

    @Override
    public Material getOrbSpawnMaterial() {
        return Material.WHEAT_SEEDS;
    }
}

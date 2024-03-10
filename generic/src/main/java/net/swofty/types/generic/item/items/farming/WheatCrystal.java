package net.swofty.types.generic.item.items.farming;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.Material;
import net.swofty.types.generic.entity.ServerCrystalImpl;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Placeable;
import net.swofty.types.generic.item.impl.ServerOrb;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

public class WheatCrystal implements CustomSkyBlockItem, SkullHead, Placeable, ServerOrb {
    @Override
    public ItemStatistics getStatistics() {
            return ItemStatistics.EMPTY;
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "9265f96f54b78885c46e7d2f86b1c1dbfe643c6060fc7fcc9834c3e3fd595135";
    }

    @Override
    public void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item) {
        new ServerCrystalImpl(Material.WHEAT_SEEDS, "9265f96f54b78885c46e7d2f86b1c1dbfe643c6060fc7fcc9834c3e3fd595135").setInstance(
                player.getInstance(), event.getBlockPosition().add(0, 1, 0)
        );
    }

    @Override
    public Material getOrbSpawnMaterial() {
        return Material.WHEAT_SEEDS;
    }
}

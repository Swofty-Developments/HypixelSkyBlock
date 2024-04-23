package net.swofty.types.generic.item.items.farming;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointRank;
import net.swofty.types.generic.data.mongodb.CrystalDatabase;
import net.swofty.types.generic.entity.ServerCrystalImpl;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.PlaceEvent;
import net.swofty.types.generic.item.impl.ServerOrb;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class CarrotCrystal implements CustomSkyBlockItem, SkullHead, ServerOrb, PlaceEvent {
    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.empty();
    }

    @Override
    public String getSkullTexture(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        return "9265f96f54b78885c46e7d2f86b1c1dbfe643c6060fc7fcc9834c3e3fd595135";
    }

    @Override
    public void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item) {
        event.setCancelled(true);

        if (SkyBlockConst.isIslandServer()) {
            player.sendMessage("§cYou cannot place this item on your island.");
            return;
        } else if (!player.getDataHandler().get(DataHandler.Data.RANK, DatapointRank.class).getValue().isStaff())
            return;

        new ServerCrystalImpl(getOrbSpawnMaterial(),
                "9265f96f54b78885c46e7d2f86b1c1dbfe643c6060fc7fcc9834c3e3fd595135",
                getBlocksToPlaceOn()
        ).setInstance(player.getInstance(), event.getBlockPosition().add(0, 1, 0));

        CrystalDatabase.addCrystal("9265f96f54b78885c46e7d2f86b1c1dbfe643c6060fc7fcc9834c3e3fd595135",
                new Pos(event.getBlockPosition().add(0, 1, 0)), ItemType.CARROT_CRYSTAL);
    }

    @Override
    public Function<ServerCrystalImpl, Block> getOrbSpawnMaterial() {
        return (entity) -> Material.CARROT.block();
    }

    @Override
    public List<Material> getBlocksToPlaceOn() {
        return List.of(Material.FARMLAND);
    }
}


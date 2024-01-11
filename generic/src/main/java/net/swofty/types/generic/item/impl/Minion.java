package net.swofty.types.generic.item.impl;

import com.mongodb.lang.Nullable;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMinionData;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Minion extends CustomSkyBlockItem, SkullHead, Placeable {
    MinionRegistry getMinionRegistry();

    @Override
    default void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item) {
        if (!SkyBlockConst.isIslandServer()) {
            player.sendMessage("§cYou can only place minions on your island!");
            event.setCancelled(true);
            return;
        }

        DatapointMinionData.ProfileMinionData playerData = player.getDataHandler().get(
                DataHandler.Data.MINION_DATA, DatapointMinionData.class
        ).getValue();

        int slots = playerData.getSlots();

        IslandMinionData minionData = player.getSkyBlockIsland().getMinionData();

        if (minionData.getMinions().size() == slots) {
            player.sendMessage("§cYou have reached the maximum amount of minions you can place!");
            event.setCancelled(true);
            return;
        }

        IslandMinionData.IslandMinion minion = minionData.initializeMinion(Pos.fromPoint(event.getBlockPosition()),
                getMinionRegistry(),
                item.getAttributeHandler().getMinionData());
        minionData.spawn(minion);

        event.setBlock(Block.AIR);

        player.sendMessage(
                "§bYou placed a minion! (" + minionData.getMinions().size() + "/" + playerData.getSlots() + ")"
        );
    }

    @Override
    default ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    default String getSkullTexture(SkyBlockPlayer player, SkyBlockItem item) {
        return getMinionRegistry().asSkyBlockMinion().getTexture();
    }


    @Override
    default String getAbsoluteName(SkyBlockPlayer player, SkyBlockItem item) {
        return "§9" + getMinionRegistry().getDisplay() + " " +
                StringUtility.getAsRomanNumeral(item.getAttributeHandler().getMinionData().tier());
    }

    @Override
    default List<String> getAbsoluteLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<String> lore = new ArrayList<>(Arrays.asList(
                "§7Place this minion and it will start",
                "§7generating and mining " + getMinionRegistry().name().toLowerCase() + "!",
                "§7Requires an open area to place",
                getMinionRegistry().name().toLowerCase() + ". Minions also work",
                "§7you are offline!",
                ""
        ));

        SkyBlockMinion minion = item.getAttributeHandler().getMinionType().asSkyBlockMinion();
        ItemAttributeMinionData.MinionData data = item.getAttributeHandler().getMinionData();
        SkyBlockMinion.MinionTier tier = minion.getTiers().get(data.tier() - 1);

        lore.add("§7Time Between Actions: §a" + tier.timeBetweenActions() + "s");
        lore.add("§7Max Storage: §e" + tier.storage());
        lore.add("§7Resources Generated: §b" + data.generatedResources());

        lore.add(" ");
        lore.add("§9§lRARE");

        return lore;
    }
}

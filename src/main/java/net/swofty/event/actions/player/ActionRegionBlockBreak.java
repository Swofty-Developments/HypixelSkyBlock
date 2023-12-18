package net.swofty.event.actions.player;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.region.SkyBlockMiningConfiguration;
import net.swofty.region.RegionType;
import net.swofty.region.SkyBlockRegion;
import net.swofty.user.SkyBlockPlayer;

@EventParameters(description = "Protects the hub from being broken",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionRegionBlockBreak extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerBlockBreakEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerBlockBreakEvent playerBreakEvent = (PlayerBlockBreakEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerBreakEvent.getPlayer();

        if (player.isBypassBuild()) {
            return;
        }

        playerBreakEvent.setCancelled(true);
        SkyBlockRegion region = SkyBlockRegion.getRegionOfPosition(playerBreakEvent.getBlockPosition());

        if (region == null) {
            return;
        }

        RegionType type = region.getType();

        Block block = playerBreakEvent.getBlock();
        Material material = Material.fromNamespaceId(block.name().toString());
        SkyBlockMiningConfiguration mining = type.getMiningHandler();

        if (mining == null || material == null || !mining.getMineableBlocks().contains(material)) {
            return;
        }

        mining.addToQueue(player, Pos.fromPoint(playerBreakEvent.getBlockPosition()), (SharedInstance) player.getInstance());
    }
}


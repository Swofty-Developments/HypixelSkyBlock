package net.swofty.commons.skyblock.event.actions.player.region;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.event.custom.CustomBlockBreakEvent;
import net.swofty.commons.skyblock.entity.DroppedItemEntityImpl;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.region.RegionType;
import net.swofty.commons.skyblock.region.SkyBlockMiningConfiguration;
import net.swofty.commons.skyblock.region.SkyBlockRegion;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

@EventParameters(description = "Protects the hub from being broken",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.HUB,
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
        Material material = Material.fromNamespaceId(block.name());
        SkyBlockMiningConfiguration mining = type.getMiningHandler();

        if (mining == null || material == null || !mining.getMineableBlocks().contains(material)) {
            return;
        }

        mining.addToQueue(player, Pos.fromPoint(playerBreakEvent.getBlockPosition()), (SharedInstance) player.getInstance());
        SkyBlockEvent.callSkyBlockEvent(new CustomBlockBreakEvent(
                player, block, playerBreakEvent.getBlockPosition()
        ));

        /**
         * Handle block dropping
         */
        DroppedItemEntityImpl droppedItem = new DroppedItemEntityImpl(new SkyBlockItem(material), player);
        Pos pos = Pos.fromPoint(playerBreakEvent.getBlockPosition());
        // Move the dropped item to the center of the block
        pos = pos.add(0.5, 0.5, 0.5);
        // Move block closer to player by 0.5 blocks
        Pos playerPos = player.getPosition().add(0, 0.5, 0);
        double x = playerPos.x() - pos.x();
        double y = playerPos.y() - pos.y();
        double z = playerPos.z() - pos.z();
        double distance = Math.sqrt(x * x + y * y + z * z);
        double multiplier = 1.2 / distance;
        pos = pos.add(x * multiplier, y * multiplier * 3, z * multiplier);

        droppedItem.setInstance(player.getInstance(), pos);
        droppedItem.spawn();
    }
}


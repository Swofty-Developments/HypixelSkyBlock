package net.swofty.types.generic.event.actions.player.region;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.item.ItemDropChanger;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.region.SkyBlockMiningConfiguration;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;

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

        if (player.isBypassBuild() || SkyBlockConst.getTypeLoader().getType() == ServerType.ISLAND) {
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

        SkyBlockItem item;
        if (ItemDropChanger.get(material) != null) {
            item = ItemDropChanger.get(material).getItemSupplier().get();
        } else {
            item = new SkyBlockItem(material);
        }

        SkyBlockEvent.callSkyBlockEvent(new CustomBlockBreakEvent(
                player, item.getMaterial(), playerBreakEvent.getBlockPosition()
        ));

        /**
         * Handle block dropping
         */
        DroppedItemEntityImpl droppedItem = new DroppedItemEntityImpl(item, player);
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
    }
}


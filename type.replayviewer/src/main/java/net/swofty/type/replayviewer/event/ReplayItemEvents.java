package net.swofty.type.replayviewer.event;

import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.item.ReplayItemHandler;

public class ReplayItemEvents implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void onItemFinishUse(PlayerFinishItemUseEvent event) {
        ReplayItemHandler handler = TypeReplayViewerLoader.getItemHandler();
        if (handler != null) {
            handler.onItemFinishUse(event);
        }
    }

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void onItemUseOnBlock(PlayerUseItemOnBlockEvent event) {
        ReplayItemHandler handler = TypeReplayViewerLoader.getItemHandler();
        if (handler != null) {
            handler.onItemUseOnBlock(event);
        }
    }

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void onItemUse(PlayerUseItemEvent event) {
        ReplayItemHandler handler = TypeReplayViewerLoader.getItemHandler();
        if (handler != null) {
            handler.onItemUse(event);
        }
    }

    @HypixelEvent(node = EventNodes.ITEM, requireDataLoaded = true)
    public void onItemDrop(ItemDropEvent event) {
        ReplayItemHandler handler = TypeReplayViewerLoader.getItemHandler();
        if (handler != null) {
            handler.onItemDrop(event);
        }
    }

}

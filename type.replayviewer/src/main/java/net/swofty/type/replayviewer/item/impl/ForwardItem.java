package net.swofty.type.replayviewer.item.impl;

import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.data.datapoints.DatapointReplaySettings;
import net.swofty.type.generic.data.handlers.ReplayDataHandler;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.item.ReplayItem;

import java.util.List;

public class ForwardItem extends ReplayItem {

    public ForwardItem() {
        super("forward");
    }

    @Override
    public ItemStack getBlandItem() {
        return null;
    }

    @Override
    public ItemStack getItemStack(HypixelPlayer... p) {
        HypixelPlayer player = p[0];
        short duration;
        ReplayDataHandler handler = ReplayDataHandler.getUser(player);
        if (handler == null) {
            duration = 30;
            player.sendMessage("§cSomething went wrong while trying to access player data.");
        } else {
            DatapointReplaySettings.ReplaySettings settings = handler.get(ReplayDataHandler.Data.REPLAY_SETTINGS, DatapointReplaySettings.class)
                .getValue();
            duration = settings.getSkipIntervals();
        }
        return ItemStackCreator.getStackHead("§a" + duration + "s Forward", "db2f30502a8fe4c80e883d23b47389b03a7818d9bbad2ba4dc10d653d3eb52b2", 1, List.of(
            "§7Left click to change the duration."
        )).build();
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        ((CancellableEvent) event).setCancelled(true);
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        short duration;
        ReplayDataHandler handler = ReplayDataHandler.getUser(player);
        if (handler == null) {
            duration = 30;
            player.sendMessage("§cSomething went wrong while trying to access player data.");
        } else {
            DatapointReplaySettings.ReplaySettings settings = handler.get(ReplayDataHandler.Data.REPLAY_SETTINGS, DatapointReplaySettings.class)
                .getValue();
            duration = settings.getSkipIntervals();
        }
        TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
            (session) -> session.skipForward(duration),
            () -> player.sendMessage("§cError: no active replay session.")
        );
    }

    @Override
    public void onItemDigging(PlayerStartDiggingEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
            (session) -> {
                ReplayDataHandler handler = ReplayDataHandler.getUser(player);
                if (handler == null) {
                    player.sendMessage("§cSomething went wrong while trying to access player data.");
                    return;
                }
                DatapointReplaySettings.ReplaySettings settings = handler.get(ReplayDataHandler.Data.REPLAY_SETTINGS, DatapointReplaySettings.class)
                    .getValue();
                short newDuration = session.cycleSkip(settings.getSkipIntervals());
                settings.setSkipIntervals(newDuration);
                handler.get(ReplayDataHandler.Data.REPLAY_SETTINGS, DatapointReplaySettings.class).setValue(settings);
                TypeReplayViewerLoader.populateInventory(player);
            },
            () -> player.sendMessage("§cError: no active replay session.")
        );
    }

}

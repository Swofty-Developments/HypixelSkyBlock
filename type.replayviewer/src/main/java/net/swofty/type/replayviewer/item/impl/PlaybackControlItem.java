package net.swofty.type.replayviewer.item.impl;

import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.item.ReplayItem;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlaybackControlItem extends ReplayItem {

    public PlaybackControlItem() {
        super("playback");
    }

    @Override
    public ItemStack getBlandItem() {
        return null;
    }

    @Override
    public ItemStack getItemStack(HypixelPlayer... p) {
        HypixelPlayer player = p[0];
        AtomicBoolean isPlaying = new AtomicBoolean(true);
        TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
            session -> isPlaying.set(session.isPlaying()),
            () -> player.sendMessage("§cNo active replay session.")
        );
        if (isPlaying.get()) {
            return ItemStackCreator.createNamedItemStack(Material.MAGENTA_DYE, "§aClick to Pause").build();
        }
        return ItemStackCreator.getStack("§aClick to Resume", Material.GRAY_DYE, 1, List.of(
            "§7The replay is currently paused."
        )).build();
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        ((CancellableEvent) event).setCancelled(true);
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
            session -> {
                if (session.isPlaying()) {
                    session.pause();
                } else {
                    session.play();
                }
                TypeReplayViewerLoader.populateInventory(player);
            },
            () -> player.sendMessage("§cNo active replay session.")
        );
    }
}

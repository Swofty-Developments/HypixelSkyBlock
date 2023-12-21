package net.swofty.event.actions.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.item.Material;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointRank;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.gui.inventory.inventories.GUIEnchantmentTable;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

@EventParameters(description = "Handles clicking on the enchantment table",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionEnchantmentTableClick extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerBlockInteractEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerBlockInteractEvent interactEvent = (PlayerBlockInteractEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) interactEvent.getPlayer();

        if (Material.fromNamespaceId(interactEvent.getBlock().namespace()) != Material.ENCHANTING_TABLE) {
            return;
        }

        new GUIEnchantmentTable(player.getInstance(), Pos.fromPoint(interactEvent.getBlockPosition())).open(player);
    }
}


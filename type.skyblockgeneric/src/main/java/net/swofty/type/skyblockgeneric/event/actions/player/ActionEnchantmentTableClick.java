package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.item.Material;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.gui.inventories.GUIEnchantmentTable;
import net.swofty.type.generic.user.HypixelPlayer;

public class ActionEnchantmentTableClick implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerBlockInteractEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (Material.fromKey(event.getBlock().key()) != Material.ENCHANTING_TABLE) {
            return;
        }

        event.setBlockingItemUse(true);

        new GUIEnchantmentTable(player.getInstance(), Pos.fromPoint(event.getBlockPosition())).open(player);
    }
}


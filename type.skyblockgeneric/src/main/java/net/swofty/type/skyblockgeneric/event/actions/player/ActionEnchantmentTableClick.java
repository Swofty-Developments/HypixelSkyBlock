package net.swofty.type.skyblockgeneric.event.actions.player;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.item.Material;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.gui.inventories.GUIEnchantmentTable;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionEnchantmentTableClick implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(PlayerBlockInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (Material.fromKey(event.getBlock().key()) != Material.ENCHANTING_TABLE) {
            return;
        }

        event.setBlockingItemUse(true);
        new GUIEnchantmentTable(player.getInstance(), Pos.fromPoint(event.getBlockPosition())).open(player);
    }
}


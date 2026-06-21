package net.swofty.type.skyblockgeneric.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClickItem implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(InventoryPreClickEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        ItemStack itemStack = event.getClickedItem();
        if (!SkyBlockItem.isSkyBlockItem(itemStack)) return;

        SkyBlockItem item = new SkyBlockItem(itemStack);

        if (item.getItemType() == ItemType.SKYBLOCK_MENU) {
            event.setCancelled(true);
            player.openView(new GUISkyBlockMenu());
        }
    }
}

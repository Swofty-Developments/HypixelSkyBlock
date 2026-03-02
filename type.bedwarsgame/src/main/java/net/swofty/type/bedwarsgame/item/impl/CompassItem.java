package net.swofty.type.bedwarsgame.item.impl;

import net.minestom.server.component.DataComponents;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.gui.GUITrackerAndCommunication;
import net.swofty.type.bedwarsgame.item.SimpleInteractableItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;

public class CompassItem extends SimpleInteractableItem {

    public CompassItem() {
        super("compass");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStack.builder(Material.COMPASS).set(DataComponents.CUSTOM_NAME, I18n.t("bedwars.item.compass")).build();
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.openView(new GUITrackerAndCommunication());
    }
}

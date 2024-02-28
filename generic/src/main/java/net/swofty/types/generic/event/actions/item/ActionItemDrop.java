package net.swofty.types.generic.event.actions.item;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.item.ItemDropEvent;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointBoolean;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.entity.DroppedItemEntityImpl;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

@EventParameters(description = "Handles item ability use for right clicks",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionItemDrop extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return ItemDropEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        ItemDropEvent event = (ItemDropEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (new SkyBlockItem(event.getItemStack()).getAttributeHandler().getItemType().toLowerCase().contains("menu")) {
            event.setCancelled(true);
            return;
        }

        if (SkyBlockInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
            event.setCancelled(true);
            return;
        }

        boolean hideMessage = player.getToggles().get(DatapointToggles.Toggles.ToggleType.DISABLE_DROP_MESSAGES);

        if (!hideMessage) {
            player.sendMessage(Component.text("§e⚠ §aYour drops can't be seen by other players in §bSkyBlock§a!")
                    .hoverEvent(Component.text("§eClick here to disable the alert!"))
                    .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/toggledropalert"))
            );
            player.sendMessage(Component.text("§aOnly you can pickup your dropped items!")
                    .hoverEvent(Component.text("§eClick here to disable the alert!"))
                    .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/toggledropalert"))
            );
            player.sendMessage(Component.text("§eClick here to disable this alert forever!")
                    .hoverEvent(Component.text("§eClick here to disable the alert!"))
                    .clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand("/toggledropalert")));
        }

        DroppedItemEntityImpl droppedItem = new DroppedItemEntityImpl(new SkyBlockItem(
                event.getItemStack()),
                player);
        Pos pos = Pos.fromPoint(player.getPosition().add(0, 1, 0));

        droppedItem.setVelocity(player.getPosition().direction()
                .mul(5)
                .add(0, 1.5, 0)
        );

        droppedItem.setInstance(player.getInstance(), pos);
        droppedItem.spawn();
    }
}

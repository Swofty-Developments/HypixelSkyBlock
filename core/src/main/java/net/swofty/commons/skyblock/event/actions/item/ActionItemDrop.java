package net.swofty.commons.skyblock.event.actions.item;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.item.ItemDropEvent;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.data.DataHandler;
import net.swofty.commons.skyblock.data.datapoints.DatapointBoolean;
import net.swofty.commons.skyblock.entity.DroppedItemEntityImpl;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.item.SkyBlockItem;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

@EventParameters(description = "Handles item ability use for right clicks",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
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
        boolean hideMessage = player.getDataHandler().get(
                DataHandler.Data.DISABLE_DROP_MESSAGE, DatapointBoolean.class
        ).getValue();

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
                .mul(7)
                .add(0, 1.5, 0)
        );

        droppedItem.setInstance(player.getInstance(), pos);
        droppedItem.spawn();
    }
}

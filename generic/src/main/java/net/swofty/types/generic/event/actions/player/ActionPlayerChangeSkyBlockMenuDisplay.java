package net.swofty.types.generic.event.actions.player;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.Event;
import net.minestom.server.event.inventory.PlayerInventoryItemChangeEvent;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.event.player.PlayerItemAnimationEvent;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.data.datapoints.DatapointQuiver;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.QuiverDisplayOnHold;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.List;

@EventParameters(description = "Switches the display of the quiver on hold",
        node = EventNodes.PLAYER,
        requireDataLoaded = false,
        isAsync = true
)
public class ActionPlayerChangeSkyBlockMenuDisplay extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerChangeHeldSlotEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        final PlayerChangeHeldSlotEvent event = (PlayerChangeHeldSlotEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        SkyBlockItem switchedTo = new SkyBlockItem(player.getItemInMainHand());
        if (switchedTo.isNA() || switchedTo.getGenericInstance() == null) {
            setMainMenu(player);
            return;
        }

        // Check if item shows quiver
        if (switchedTo.getGenericInstance() instanceof QuiverDisplayOnHold) {
            ItemStack.Builder builder = ItemStack.builder(Material.ARROW);
            builder = ItemStackCreator.enchant(builder);
            DatapointQuiver.PlayerQuiver quiver = player.getQuiver();

            if (!player.hasCustomCollectionAward(CustomCollectionAward.QUIVER)) return;

            if (player.getQuiver().isEmpty()) {
                builder = builder.displayName(Component.text("§8Empty Quiver")).lore(List.of(
                        Component.text("§7This item is in your inventory"),
                        Component.text("§7because you are currently holding a"),
                        Component.text("§7Bow"),
                        Component.text(" "),
                        Component.text("§cQuiver is empty"),
                        Component.text(" "),
                        Component.text("§7Switch away from your Bow to see"),
                        Component.text("§7the item that was here before.")
                ));
            } else {
                SkyBlockItem item = quiver.getFirstItemInQuiver();
                builder = builder.displayName(Component.text(
                        "§8Quiver " + StringUtility.stripColor(item.getDisplayName()))
                ).lore(List.of(
                        Component.text("§7This item is in your inventory"),
                        Component.text("§7because you are currently holding a"),
                        Component.text("§7Bow"),
                        Component.text(" "),
                        Component.text("§7Quiver contains:"),
                        Component.text("§7Active Arrow: " + item.getDisplayName()
                                + " §7(§e" + quiver.getAmountOfArrows(item.getAttributeHandler().getItemTypeAsType()) + "§7)"),
                        Component.text(" "),
                        Component.text("§7Switch away from your Bow to see"),
                        Component.text("§7the item that was here before.")
                )).amount(Math.max(64, quiver.getAmountOfArrows(item.getAttributeHandler().getItemTypeAsType())));
            }

            player.getInventory().setItemStack(8, builder.build());
            player.getInventory().update();
            return;
        }

        setMainMenu(player);
    }

    public void setMainMenu(SkyBlockPlayer player) {
        player.getInventory().setItemStack(8,
                new NonPlayerItemUpdater(new SkyBlockItem(ItemType.SKYBLOCK_MENU).getItemStack())
                        .getUpdatedItem().build());
    }
}

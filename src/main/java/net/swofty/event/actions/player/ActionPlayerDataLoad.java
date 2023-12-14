package net.swofty.event.actions.player;

import lombok.SneakyThrows;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointInventory;
import net.swofty.data.mongodb.UserDatabase;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.item.updater.PlayerItemOrigin;
import net.swofty.item.updater.PlayerItemUpdater;
import net.swofty.user.SkyBlockInventory;
import net.swofty.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.UUID;

@EventParameters(description = "Load player data on join",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionPlayerDataLoad extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerLoginEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerLoginEvent playerLoginEvent = (PlayerLoginEvent) event;

        // Ensure we use player here
        final Player player = playerLoginEvent.getPlayer();
        UUID uuid = player.getUuid();

        UserDatabase userDatabase = new UserDatabase(uuid.toString());
        if (userDatabase.exists()) {
            Document document = userDatabase.getDocument();
            DataHandler dataHandler = DataHandler.fromDocument(document);
            DataHandler.userCache.put(uuid, dataHandler);

            SkyBlockInventory skyBlockInventory = ((SkyBlockPlayer) player).getDataHandler()
                    .get(DataHandler.Data.INVENTORY, DatapointInventory.class).getValue();

            player.setHelmet(skyBlockInventory.getHelmet().getItemStack());
            player.setChestplate(skyBlockInventory.getChestplate().getItemStack());
            player.setLeggings(skyBlockInventory.getLeggings().getItemStack());
            player.setBoots(skyBlockInventory.getBoots().getItemStack());

            skyBlockInventory.getItems().forEach((integer, itemStack) -> {
                PlayerItemOrigin origin = PlayerItemOrigin.INVENTORY_SLOT;
                origin.setData(integer);

                player.getInventory().setItemStack(integer, itemStack.getItemStack());

                ItemStack loadedItem = PlayerItemUpdater.playerUpdate(((SkyBlockPlayer) player), origin, itemStack.getItemStack());
                origin.setStack(((SkyBlockPlayer) player), loadedItem);
            });
        } else {
            DataHandler dataHandler = DataHandler.initUserWithDefaultData(uuid);
            DataHandler.userCache.put(uuid, dataHandler);
        }
    }
}

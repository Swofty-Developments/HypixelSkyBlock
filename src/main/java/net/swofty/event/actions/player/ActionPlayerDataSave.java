package net.swofty.event.actions.player;

import lombok.SneakyThrows;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.item.ItemStack;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointInventory;
import net.swofty.data.mongodb.UserDatabase;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockInventory;
import net.swofty.user.SkyBlockPlayer;

import java.util.UUID;

@EventParameters(description = "Saves player data on quit",
        node = EventNodes.PLAYER,
        requireDataLoaded = false)
public class ActionPlayerDataSave extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerDisconnectEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerDisconnectEvent playerDisconnectEvent = (PlayerDisconnectEvent) event;

        final SkyBlockPlayer player = (SkyBlockPlayer) playerDisconnectEvent.getPlayer();
        UUID uuid = player.getUuid();

        /*
        Handle inventories separately to other datapoints
         */
        SkyBlockInventory skyBlockInventory = new SkyBlockInventory();

        ItemStack helmet = player.getHelmet();
        if (SkyBlockItem.isSkyBlockItem(helmet)) {
            skyBlockInventory.setHelmet(new SkyBlockItem(helmet));
        }
        ItemStack chestplate = player.getChestplate();
        if (SkyBlockItem.isSkyBlockItem(chestplate)) {
            skyBlockInventory.setChestplate(new SkyBlockItem(chestplate));
        }
        ItemStack leggings = player.getLeggings();
        if (SkyBlockItem.isSkyBlockItem(leggings)) {
            skyBlockInventory.setLeggings(new SkyBlockItem(leggings));
        }
        ItemStack boots = player.getBoots();
        if (SkyBlockItem.isSkyBlockItem(boots)) {
            skyBlockInventory.setBoots(new SkyBlockItem(boots));
        }

        for (int i = 0; i <= 36; i++) {
            ItemStack stack = player.getInventory().getItemStack(i);
            if (SkyBlockItem.isSkyBlockItem(stack)) {
                skyBlockInventory.getItems().put(i, new SkyBlockItem(stack));
            }
        }

        player.getDataHandler().get(DataHandler.Data.INVENTORY, DatapointInventory.class).setValue(skyBlockInventory);

        /*
        Save the data into the DB
         */
        UserDatabase userDatabase = new UserDatabase(uuid.toString());
        if (userDatabase.exists()) {
            UserDatabase.collection.replaceOne(userDatabase.getDocument(), player.getDataHandler().toDocument());
            DataHandler.userCache.remove(uuid);
        } else {
            UserDatabase.collection.insertOne(player.getDataHandler().toDocument());
            DataHandler.userCache.remove(uuid);
        }
    }
}

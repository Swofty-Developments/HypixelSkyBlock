package net.swofty.gui.inventory.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.data.DataHandler;
import net.swofty.data.datapoints.DatapointString;
import net.swofty.data.mongodb.ProfilesDatabase;
import net.swofty.data.mongodb.UserDatabase;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.UserProfiles;
import org.bson.Document;

import java.util.UUID;

public class GUIProfileCreate extends SkyBlockInventoryGUI {
    public GUIProfileCreate() {
        super("Create a Profile", InventoryType.CHEST_3_ROW);
    }

    @SneakyThrows
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        String profileName = UserProfiles.getRandomName();

        set(new GUIClickableItem() {

            @Override
            public int getSlot() {
                return 11;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aCreate New Profile", Material.GREEN_TERRACOTTA, (short) 0, 1,
                        "§7You are creating a new SkyBlock",
                        "§7profile.",
                        "",
                        "§7Profile name: §e" + profileName,
                        "",
                        "§7You won't lose any progress.",
                        "§7You can switch between profiles.",
                        "",
                        "§bYou are creating a SOLO profile!",
                        "§bUse /coop to play with friends!",
                        "§eClick to confirm new profile!");
            }

            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                UserProfiles profiles = player.getProfiles();
                UserProfiles toSet = new UserProfiles();
                toSet.setProfiles(profiles.getProfiles());

                UUID profileId = UUID.randomUUID();

                DataHandler handler = DataHandler.initUserWithDefaultData(player.getUuid());
                handler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(profileName);
                Document document = handler.toDocument(profileId);

                ProfilesDatabase.collection.insertOne(document);

                // TODO: Proxy support
                player.kick("§cYou must relog for this change to take effect");

                MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                    toSet.addProfile(profileId);
                    toSet.setCurrentlySelected(profileId);

                    UserDatabase database = new UserDatabase(player.getUuid());
                    database.saveProfiles(toSet);
                }, TaskSchedule.tick(2), TaskSchedule.stop());
            }
        });

        set(new GUIClickableItem() {

            @Override
            public int getSlot() {
                return 15;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.createNamedItemStack(Material.RED_TERRACOTTA, "§cCancel");
            }

            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIProfileSelectMode().open(player);
            }
        });

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}

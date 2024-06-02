package net.swofty.types.generic.gui.inventory.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointString;
import net.swofty.types.generic.data.datapoints.DatapointUUID;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.data.mongodb.UserDatabase;
import net.swofty.types.generic.event.actions.player.data.ActionPlayerDataSave;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.user.PlayerProfiles;
import net.swofty.types.generic.user.SkyBlockPlayer;
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
        String profileName = PlayerProfiles.getRandomName();

        set(new GUIClickableItem(11) {
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
                PlayerProfiles profiles = player.getProfiles();
                UUID profileId = UUID.randomUUID();

                DataHandler handler = DataHandler.initUserWithDefaultData(player.getUuid());
                handler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(profileName);
                handler.get(DataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(profileId);
                Document document = handler.toDocument(profileId);

                profiles.addProfile(profileId);
                ProfilesDatabase.collection.insertOne(document);

                player.getHookManager().registerHook(ActionPlayerDataSave.class, (nil) -> {
                profiles.setCurrentlySelected(profileId);

                    UserDatabase database = new UserDatabase(player.getUuid());
                    database.saveProfiles(profiles);
                }, false);

                player.sendTo(ServerType.ISLAND, true);
            }
        });

        set(new GUIClickableItem(15) {
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

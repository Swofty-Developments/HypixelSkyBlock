package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUID;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.actions.data.ActionPlayerDataSave;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.commons.SkyBlockPlayerProfiles;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.UUID;

public class GUIProfileCreate extends HypixelInventoryGUI {
    public GUIProfileCreate() {
        super("Create a Profile", InventoryType.CHEST_3_ROW);
    }

    @SneakyThrows
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        String profileName = SkyBlockPlayerProfiles.getRandomName();

        set(new GUIClickableItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§aCreate New Profile", Material.GREEN_TERRACOTTA, 1,
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
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                SkyBlockPlayerProfiles profiles = player.getProfiles();
                UUID profileId = UUID.randomUUID();

                SkyBlockDataHandler handler = SkyBlockDataHandler.initUserWithDefaultData(player.getUuid());
                handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(profileName);
                handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(profileId);
                Document document = handler.toProfileDocument(profileId);

                profiles.addProfile(profileId);
                ProfilesDatabase.collection.insertOne(document);

                player.getHookManager().registerHook(ActionPlayerDataSave.class, (nil) -> {
                profiles.setCurrentlySelected(profileId);

                    UserDatabase database = new UserDatabase(player.getUuid());
                    database.saveProfiles(profiles);
                }, false);

                player.sendTo(ServerType.SKYBLOCK_ISLAND, true);
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.createNamedItemStack(Material.RED_TERRACOTTA, "§cCancel");
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
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
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}

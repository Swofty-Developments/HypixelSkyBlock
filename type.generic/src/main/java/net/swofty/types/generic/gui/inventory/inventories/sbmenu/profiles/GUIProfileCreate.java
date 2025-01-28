package net.swofty.types.generic.gui.inventory.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
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
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.PlayerProfiles;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.UUID;

public class GUIProfileCreate extends SkyBlockAbstractInventory {
    private final String profileName;

    public GUIProfileCreate() {
        super(InventoryType.CHEST_3_ROW);
        this.profileName = PlayerProfiles.getRandomName();
        doAction(new SetTitleAction(Component.text("Create a Profile")));
    }

    @SneakyThrows
    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Create profile button
        attachItem(GUIItem.builder(11)
                .item(ItemStackCreator.getStack("§aCreate New Profile", Material.GREEN_TERRACOTTA, 1,
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
                        "§eClick to confirm new profile!").build())
                .onClick((ctx, item) -> {
                    handleProfileCreation(ctx.player());
                    return true;
                })
                .build());

        // Cancel button
        attachItem(GUIItem.builder(15)
                .item(ItemStackCreator.createNamedItemStack(Material.RED_TERRACOTTA, "§cCancel").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIProfileSelectMode());
                    return true;
                })
                .build());
    }

    private void handleProfileCreation(SkyBlockPlayer player) {
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

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {}
}
package net.swofty.types.generic.gui.inventory.inventories.sbmenu.profiles;

import com.mongodb.client.model.Filters;
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
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.data.mongodb.ProfilesDatabase;
import net.swofty.types.generic.data.mongodb.UserDatabase;
import net.swofty.types.generic.event.actions.player.data.ActionPlayerDataSave;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.PlayerProfiles;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.UUID;

public class GUIProfileSelect extends SkyBlockAbstractInventory {
    private final UUID profileUuid;

    public GUIProfileSelect(UUID profileUuid) {
        super(InventoryType.CHEST_4_ROW);
        this.profileUuid = profileUuid;
        doAction(new SetTitleAction(Component.text("Profile Management")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Back button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Profile Management").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIProfileManagement());
                    return true;
                })
                .build());

        // Switch profile button
        attachItem(GUIItem.builder(11)
                .item(() -> createSwitchProfileItem(player))
                .onClick((ctx, item) -> {
                    handleProfileSwitch(ctx.player());
                    return true;
                })
                .build());

        // Delete profile button
        attachItem(GUIItem.builder(15)
                .item(ItemStackCreator.getStack("§cDelete profile", Material.RED_STAINED_GLASS, 1,
                        "§7Clear this profile slot by",
                        "§7deleting the profile forever.",
                        "",
                        "§cWarning!",
                        "§fYou cannot revert this actions!",
                        "",
                        "§eClick to continue!").build())
                .onClick((ctx, item) -> {
                    handleProfileDeletion(ctx.player());
                    return true;
                })
                .build());
    }

    @SneakyThrows
    private ItemStack createSwitchProfileItem(SkyBlockPlayer player) {
        return ItemStackCreator.getStack("§aSwitch to Profile", Material.GRASS_BLOCK, 1,
                "§7Teleports you to your island on",
                "§7another profile and loads your",
                "§7inventory, skills, collections",
                "§7and more...",
                "",
                "§7Current: §e" + player.getDataHandler().get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue(),
                "§7Switching to: §a" + DataHandler.fromDocument(new ProfilesDatabase(profileUuid.toString()).getDocument())
                        .get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue(),
                "",
                "§eClick to switch").build();
    }

    private void handleProfileSwitch(SkyBlockPlayer player) {
        PlayerProfiles profiles = player.getProfiles();
        PlayerProfiles toSet = new PlayerProfiles();
        toSet.setProfiles(profiles.getProfiles());

        player.getHookManager().registerHook(ActionPlayerDataSave.class, (nil) -> {
            UserDatabase database = new UserDatabase(player.getUuid());
            toSet.setCurrentlySelected(profileUuid);
            database.saveProfiles(toSet);
        }, false);

        player.sendTo(ServerType.ISLAND, true);
    }

    @SneakyThrows
    private void handleProfileDeletion(SkyBlockPlayer player) {
        if (CoopDatabase.getFromMemberProfile(profileUuid) != null) {
            player.sendMessage("§cYou cannot delete a profile that is in a coop!");
            player.sendMessage("§eInstead run §a/coopleave §eto leave your coop.");
            return;
        }

        PlayerProfiles profiles = player.getProfiles();
        profiles.removeProfile(profileUuid);

        DataHandler handler = DataHandler.fromDocument(new ProfilesDatabase(profileUuid.toString()).getDocument());

        player.sendMessage(
                "§aDone! Your §e"
                        + handler.get(DataHandler.Data.PROFILE_NAME, DatapointString.class).getValue()
                        + " §aprofile was deleted!");

        ProfilesDatabase.collection.deleteOne(Filters.eq("_id", profileUuid.toString()));

        player.openInventory(new GUIProfileManagement());
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
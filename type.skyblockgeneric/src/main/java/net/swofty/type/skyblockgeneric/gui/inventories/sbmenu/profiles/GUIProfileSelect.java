package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import com.mongodb.client.model.Filters;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.actions.data.ActionPlayerDataSave;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

public class GUIProfileSelect extends StatelessView {
    private final UUID profileUuid;

    public GUIProfileSelect(UUID profileUuid) {
        this.profileUuid = profileUuid;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Profile Management", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 31, ctx);

        // Switch to Profile
        layout.slot(11, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            String currentProfile = player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
            String switchingTo;
            try {
                switchingTo = SkyBlockDataHandler.createFromProfileOnly(new ProfilesDatabase(profileUuid.toString()).getDocument())
                        .get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
            } catch (Exception e) {
                switchingTo = "Unknown";
            }

            return ItemStackCreator.getStack("§aSwitch to Profile", Material.GRASS_BLOCK, 1,
                    "§7Teleports you to your island on",
                    "§7another profile and loads your",
                    "§7inventory, skills, collections",
                    "§7and more...",
                    "",
                    "§7Current: §e" + currentProfile,
                    "§7Switching to: §a" + switchingTo,
                    "", "§eClick to switch");
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockPlayerProfiles profiles = player.getProfiles();
            SkyBlockPlayerProfiles toSet = new SkyBlockPlayerProfiles();
            toSet.setProfiles(profiles.getProfiles());

            player.getHookManager().registerHook(ActionPlayerDataSave.class, (nil) -> {
                UserDatabase database = new UserDatabase(player.getUuid());
                toSet.setCurrentlySelected(profileUuid);
                database.saveProfiles(toSet);
            }, false);

            player.sendTo(ServerType.SKYBLOCK_ISLAND, true);
        });

        // Delete Profile
        layout.slot(15, (s, c) -> ItemStackCreator.getStack("§cDelete profile", Material.RED_STAINED_GLASS, 1,
                        "§7Clear this profile slot by",
                        "§7deleting the profile forever.",
                        "",
                        "§cWarning!",
                        "§fYou cannot revert this actions!",
                        "",
                        "§eClick to continue!"),
                (click, c) -> {
                    SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                    if (CoopDatabase.getFromMemberProfile(profileUuid) != null) {
                        player.sendMessage("§cYou cannot delete a profile that is in a coop!");
                        player.sendMessage("§eInstead run §a/coopleave §eto leave your coop.");
                        return;
                    }

                    SkyBlockPlayerProfiles profiles = player.getProfiles();
                    profiles.removeProfile(profileUuid);

                    try {
                        SkyBlockDataHandler handler = SkyBlockDataHandler.createFromProfileOnly(new ProfilesDatabase(profileUuid.toString()).getDocument());
                        player.sendMessage("§aDone! Your §e"
                                + handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue()
                                + " §aprofile was deleted!");
                    } catch (Exception e) {
                        player.sendMessage("§aDone! Your profile was deleted!");
                    }

                    ProfilesDatabase.collection.deleteOne(Filters.eq("_id", profileUuid.toString()));
                    player.openView(new GUIProfileManagement());
                });
    }
}

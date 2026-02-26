package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
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
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUID;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.UUID;

public class GUIProfileCreate extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Create a Profile", InventoryType.CHEST_3_ROW);
    }

    @SneakyThrows
    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);

        String profileName = SkyBlockPlayerProfiles.getRandomName();

        // Create New Profile
        layout.slot(11, (s, c) -> ItemStackCreator.getStack("§aCreate New Profile", Material.GREEN_TERRACOTTA, 1,
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
                        "§eClick to confirm new profile!"),
                (click, c) -> {
                    SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                    SkyBlockPlayerProfiles profiles = player.getProfiles();
                    UUID profileId = UUID.randomUUID();

                    // Create new SkyBlock data handler with the profile ID
                    SkyBlockDataHandler handler = SkyBlockDataHandler.initUserWithDefaultData(player.getUuid(), profileId);
                    handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(profileName);
                    handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(profileId);

                    // Convert to document for saving
                    Document document = handler.toProfileDocument();

                    profiles.addProfile(profileId);
                    ProfilesDatabase.collection.insertOne(document);

                    player.getHookManager().registerHook(ActionPlayerDataSave.class, (nil) -> {
                        profiles.setCurrentlySelected(profileId);
                        UserDatabase database = new UserDatabase(player.getUuid());
                        database.saveProfiles(profiles);
                    }, false);

                    player.sendTo(ServerType.SKYBLOCK_ISLAND, true);
                });

        // Cancel
        layout.slot(15, (s, c) -> ItemStackCreator.createNamedItemStack(Material.RED_TERRACOTTA, "§cCancel"),
                (click, c) -> c.player().openView(new GUIProfileSelectMode()));
    }
}
package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUID;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.UUID;

public class GUIProfileCreate extends StatelessView {
    private final net.swofty.type.skyblockgeneric.user.ProfileMode mode;

    public GUIProfileCreate(net.swofty.type.skyblockgeneric.user.ProfileMode mode) {
        this.mode = mode;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(mode == net.swofty.type.skyblockgeneric.user.ProfileMode.IRONMAN
                ? "Create an Ironman Profile" : "Create a Profile", InventoryType.CHEST_3_ROW);
    }

    @SneakyThrows
    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);

        String profileName = SkyBlockPlayerProfiles.getRandomName();

        layout.slot(11, (s, c) -> ItemStackCreator.getStack("§aCreate New Profile", Material.GREEN_TERRACOTTA, 1,
                "§7You are creating a new SkyBlock", "§7profile.", "", "§7Profile name: §e" + profileName,
                "§7Mode: " + mode.getDisplayName(), "", "§7You won't lose any progress.",
                "§7You can switch between profiles.", "", "§bYou are creating a SOLO profile!",
                "§bUse /coopadd <name> to play with friends!", "§eClick to confirm new profile!"),
                (click, c) -> {
                    SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                    SkyBlockPlayerProfiles profiles = player.getProfiles();
                    UUID profileId = UUID.randomUUID();

                    // Create new SkyBlock data handler with the profile ID
                    SkyBlockDataHandler handler = SkyBlockDataHandler.initUserWithDefaultData(player.getUuid(), profileId);
                    handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).setValue(profileName);
                    handler.get(SkyBlockDataHandler.Data.PROFILE_MODE, DatapointString.class).setValue(mode.name());
                    handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(profileId);

                    // Convert to document for saving
                    Document document = handler.toProfileDocument();

                    profiles.addProfile(profileId);
                    ProfilesDatabase.collection.insertOne(document);

                    // Persist the selection before transfer preparation takes its account snapshot.
                    profiles.setCurrentlySelected(profileId);
                    UserDatabase database = new UserDatabase(player.getUuid());
                    database.saveProfiles(profiles);

                    player.sendTo(ServerType.SKYBLOCK_ISLAND, true);
                });

        layout.slot(15, (s, c) -> ItemStackCreator.createNamedItemStack(Material.RED_TERRACOTTA, I18n.string("gui_sbmenu.profiles.create.cancel", c.player().getLocale())),
                (click, c) -> c.player().openView(mode == net.swofty.type.skyblockgeneric.user.ProfileMode.IRONMAN
                        ? new GUIProfileSelectSpecialMode() : new GUIProfileSelectMode()));
    }
}

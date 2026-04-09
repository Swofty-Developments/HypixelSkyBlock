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
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUID;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

public class GUIProfileCreate extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.profiles.create.title", InventoryType.CHEST_3_ROW);
    }

    @SneakyThrows
    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);

        String profileName = SkyBlockPlayerProfiles.getRandomName();

        layout.slot(11, (s, c) -> TranslatableItemStackCreator.getStack("gui_sbmenu.profiles.create.confirm", Material.GREEN_TERRACOTTA, 1,
                        "gui_sbmenu.profiles.create.confirm.lore", Map.of("profile_name", profileName)),
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

        layout.slot(15, (s, c) -> ItemStackCreator.createNamedItemStack(Material.RED_TERRACOTTA, I18n.string("gui_sbmenu.profiles.create.cancel", c.player().getLocale())),
                (click, c) -> c.player().openView(new GUIProfileSelectMode()));
    }
}
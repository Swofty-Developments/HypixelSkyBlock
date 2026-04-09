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
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.UUID;

public class GUIProfileSelect extends StatelessView {
    private final UUID profileUuid;

    public GUIProfileSelect(UUID profileUuid) {
        this.profileUuid = profileUuid;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.profiles.select.title", InventoryType.CHEST_4_ROW);
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

            return TranslatableItemStackCreator.getStack("gui_sbmenu.profiles.select.switch", Material.GRASS_BLOCK, 1,
                    "gui_sbmenu.profiles.select.switch.lore", Map.of("current", currentProfile, "switching_to", switchingTo));
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
        layout.slot(15, (s, c) -> TranslatableItemStackCreator.getStack("gui_sbmenu.profiles.select.delete", Material.RED_STAINED_GLASS, 1,
                        "gui_sbmenu.profiles.select.delete.lore"),
                (click, c) -> {
                    SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                    java.util.Locale l = player.getLocale();
                    if (CoopDatabase.getFromMemberProfile(profileUuid) != null) {
                        player.sendMessage(I18n.t("gui_sbmenu.profiles.select.msg.cannot_delete_coop"));
                        player.sendMessage(I18n.t("gui_sbmenu.profiles.select.msg.coop_leave"));
                        return;
                    }

                    SkyBlockPlayerProfiles profiles = player.getProfiles();
                    profiles.removeProfile(profileUuid);

                    try {
                        SkyBlockDataHandler handler = SkyBlockDataHandler.createFromProfileOnly(new ProfilesDatabase(profileUuid.toString()).getDocument());
                        player.sendMessage(I18n.t("gui_sbmenu.profiles.select.msg.deleted", Map.of("profile_name",
                                handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue())));
                    } catch (Exception e) {
                        player.sendMessage(I18n.t("gui_sbmenu.profiles.select.msg.deleted_generic"));
                    }

                    ProfilesDatabase.collection.deleteOne(Filters.eq("_id", profileUuid.toString()));
                    player.openView(new GUIProfileManagement());
                });
    }
}

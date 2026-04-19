package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.type.generic.data.datapoints.DatapointBoolean;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
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
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class GUIProfileManagement extends StatelessView {
    private static final int[] PROFILE_SLOTS = {11, 12, 13, 14, 15};

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.profiles.management.title", InventoryType.CHEST_4_ROW);
    }

    @SneakyThrows
    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 31);
        Components.back(layout, 30, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        SkyBlockPlayerProfiles profiles = player.getProfiles();
        List<UUID> profileIds = profiles.getProfiles();

        for (int profileCount = 0; profileCount <= 4; profileCount++) {
            int slot = PROFILE_SLOTS[profileCount];

            if (profileIds.size() <= profileCount) {
                // Empty profile slot
                layout.slot(slot, (s, c) -> TranslatableItemStackCreator.getStack("gui_sbmenu.profiles.empty_slot", Material.OAK_BUTTON, 1,
                                "gui_sbmenu.profiles.empty_slot.lore"),
                        (click, c) -> c.player().openView(new GUIProfileSelectMode()));
                continue;
            }

            UUID profileId = profileIds.get(profileCount);
            boolean selected = profileId.equals(profiles.getCurrentlySelected());
            SkyBlockDataHandler dataHandler;

            if (selected) {
                dataHandler = SkyBlockDataHandler.getUser(player.getUuid());
            } else {
                try {
                    ProfilesDatabase profileDb = new ProfilesDatabase(profileId.toString());
                    dataHandler = SkyBlockDataHandler.createFromProfileOnly(profileDb.getDocument());
                } catch (NullPointerException profileNotYetSaved) {
                    dataHandler = SkyBlockDataHandler.initUserWithDefaultData(player.getUuid(), profileId);
                }
            }

            SkyBlockDataHandler finalDataHandler = dataHandler;

            if (selected) {
                layout.slot(slot, (s, c) -> {
                    SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                    Locale l = p.getLocale();
                    List<String> lore = new ArrayList<>(Arrays.asList(I18n.string("gui_sbmenu.profiles.selected.subtitle", l), " "));
                    updateLore(p.getUuid(), finalDataHandler, lore);
                    lore.add(" ");
                    lore.add(I18n.string("gui_sbmenu.profiles.selected.playing", l));

                    String profileName = finalDataHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
                    return ItemStackCreator.getStack(I18n.string("gui_sbmenu.profiles.selected", l, Component.text(profileName)), Material.EMERALD_BLOCK, 1, lore);
                }, (click, c) -> {
                    SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                    Locale l = p.getLocale();
                    String profileName = finalDataHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
                    p.sendMessage(I18n.t("gui_sbmenu.profiles.msg.playing_on", Component.text(profileName)));
                    p.sendMessage(I18n.t("gui_sbmenu.profiles.msg.switch_first"));
                });
            } else {
                layout.slot(slot, (s, c) -> {
                    SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                    Locale l = p.getLocale();
                    List<String> lore = new ArrayList<>(Arrays.asList(I18n.string("gui_sbmenu.profiles.unselected.subtitle", l), " "));
                    updateLore(p.getUuid(), finalDataHandler, lore);
                    lore.add(" ");
                    lore.add(I18n.string("gui_sbmenu.profiles.unselected.click", l));

                    String profileName = finalDataHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
                    return ItemStackCreator.getStack(I18n.string("gui_sbmenu.profiles.unselected", l, Component.text(profileName)), Material.GRASS_BLOCK, 1, lore);
                }, (click, c) -> c.player().openView(new GUIProfileSelect(profileId)));
            }
        }
    }

    public static List<String> updateLore(UUID playerUuid, SkyBlockDataHandler handler, List<String> lore) {
        if (handler.get(SkyBlockDataHandler.Data.IS_COOP, DatapointBoolean.class).getValue()) {
            lore.add(I18n.string("gui_sbmenu.profiles.coop_label"));

            CoopDatabase.Coop coop = CoopDatabase.getFromMember(playerUuid);
            coop.members().forEach(member -> lore.add("§7Member " + SkyBlockPlayer.getDisplayName(member)));
            coop.memberInvites().forEach(invite -> lore.add("§7Invited " + SkyBlockPlayer.getDisplayName(invite)));
            lore.add(" ");
        }

        SkyBlockRecipe.getMissionDisplay(lore, playerUuid);
        lore.add(" ");

        lore.add(I18n.string("gui_sbmenu.profiles.no_skills"));
        lore.add(" ");

        Double coins = handler.get(SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue();
        if (coins > 0) {
            lore.add(I18n.string("gui_sbmenu.profiles.purse_coins", Component.text(String.valueOf(coins))));
        }

        Long createdTime = handler.get(SkyBlockDataHandler.Data.CREATED, DatapointLong.class).getValue();
        String age = StringUtility.profileAge(System.currentTimeMillis() - createdTime);
        lore.add(I18n.string("gui_sbmenu.profiles.age", Component.text(age)));

        return lore;
    }
}

package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.data.datapoints.DatapointBoolean;
import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.datapoints.DatapointLong;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GUIProfileManagement extends StatelessView {
    private static final int[] PROFILE_SLOTS = {11, 12, 13, 14, 15};

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Profile Management", InventoryType.CHEST_4_ROW);
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
                layout.slot(slot, (s, c) -> ItemStackCreator.getStack("§eEmpty Profile Slot", Material.OAK_BUTTON, 1,
                                "§8Available",
                                " ",
                                "§7Use this slot if you want to",
                                "§7start a new SkyBlock adventure.",
                                " ",
                                "§7Each profile has its own:",
                                "§8- §7Personal island",
                                "§8- §7Inventory",
                                "§8- §7Ender Chest",
                                "§8- §7Bank & Purse",
                                "§8- §7Quests",
                                "§8- §7Collections",
                                " ",
                                "§4§lWARNING: §cCreation of profiles",
                                "§cwhich boost other profiles will",
                                "§cbe considered abusive and",
                                "§cpunished.",
                                " ",
                                "§eClick to create solo profile!"),
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
                    List<String> lore = new ArrayList<>(Arrays.asList("§8Selected slot", " "));
                    updateLore(p.getUuid(), finalDataHandler, lore);
                    lore.add(" ");
                    lore.add("§aYou are playing on this profile!");

                    String profileName = finalDataHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
                    return ItemStackCreator.getStack("§eProfile: §a" + profileName, Material.EMERALD_BLOCK, 1, lore);
                }, (click, c) -> {
                    SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                    String profileName = finalDataHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
                    p.sendMessage("§aYep! §e" + profileName + "§a is the profile you are playing on!");
                    p.sendMessage("§cIf you want to delete this profile, switch to another one first!");
                });
            } else {
                layout.slot(slot, (s, c) -> {
                    SkyBlockPlayer p = (SkyBlockPlayer) c.player();
                    List<String> lore = new ArrayList<>(Arrays.asList("§8Slot in use", " "));
                    updateLore(p.getUuid(), finalDataHandler, lore);
                    lore.add(" ");
                    lore.add("§eClick to manage!");

                    String profileName = finalDataHandler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
                    return ItemStackCreator.getStack("§eProfile: §a" + profileName, Material.GRASS_BLOCK, 1, lore);
                }, (click, c) -> c.player().openView(new GUIProfileSelect(profileId)));
            }
        }
    }

    public static List<String> updateLore(UUID playerUuid, SkyBlockDataHandler handler, List<String> lore) {
        if (handler.get(SkyBlockDataHandler.Data.IS_COOP, DatapointBoolean.class).getValue()) {
            lore.add("§bCo-op:");

            CoopDatabase.Coop coop = CoopDatabase.getFromMember(playerUuid);
            coop.members().forEach(member -> lore.add("§7Member " + SkyBlockPlayer.getDisplayName(member)));
            coop.memberInvites().forEach(invite -> lore.add("§7Invited " + SkyBlockPlayer.getDisplayName(invite)));
            lore.add(" ");
        }

        SkyBlockRecipe.getMissionDisplay(lore, playerUuid);
        lore.add(" ");

        lore.add("§cNo Skills Yet!");
        lore.add(" ");

        Double coins = handler.get(SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue();
        if (coins > 0) {
            lore.add("§7Purse Coins: §6" + coins);
        }

        Long createdTime = handler.get(SkyBlockDataHandler.Data.CREATED, DatapointLong.class).getValue();
        String age = StringUtility.profileAge(System.currentTimeMillis() - createdTime);
        lore.add("§7Age: §9" + age);

        return lore;
    }
}
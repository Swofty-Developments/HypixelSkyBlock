package net.swofty.type.skyblockgeneric.user.flow;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.swofty.commons.data.SwoftyData;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.packer.packs.TestingTexture;
import net.swofty.type.skyblockgeneric.data.CoopLinks;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.datapoints.DatapointBoolean;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.datapoints.DatapointStringList;
import net.swofty.type.generic.data.mongodb.ProfilesDatabase;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.user.categories.CustomGroups;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUID;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegion;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.island.SkyBlockIsland;
import net.swofty.type.skyblockgeneric.warps.TravelScrollIslands;
import org.bson.Document;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SkyBlockPlayerDataFlow {

    public static void load(SkyBlockPlayer player) {
        UUID playerUuid = player.getUuid();
        SkyBlockPlayerProfiles profiles = loadProfiles(player);
        UUID profileId = profiles.getCurrentlySelected();

        SkyBlockDataHandler handler = SkyBlockDataHandler.initUserWithDefaultData(playerUuid, profileId);
        SwoftyData.profile().link(profileId, CoopLinks.COOP, resolveCoopId(profileId));
        handler.loadFromApi();

        DatapointUUID islandDatapoint = handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class);
        UUID islandUuid = islandDatapoint.getValue();
        if (islandUuid == null) {
            islandUuid = profileId;
            islandDatapoint.setValue(islandUuid);
        }

        DatapointString profileNameDatapoint = handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class);
        if (Objects.equals(profileNameDatapoint.getValue(), "null")) {
            profileNameDatapoint.setValue(SkyBlockPlayerProfiles.getRandomName());
        }

        SkyBlockDataHandler.skyBlockCache.put(playerUuid, handler);
        player.setSkyBlockIsland(SkyBlockIsland.getOrCreate(islandUuid, profileId));
    }

    public static void postSpawn(SkyBlockPlayer player) {
        SkyBlockDataHandler handler = player.getSkyblockDataHandler();
        handler.runOnLoad(player);

        scheduleRegionRefresh(player);
        sendProfileIntro(player);
    }

    public static void save(SkyBlockPlayer player) {
        UUID playerUuid = player.getUuid();
        SkyBlockDataHandler handler = SkyBlockDataHandler.skyBlockCache.get(playerUuid);

        if (handler == null) return;

        handler.runOnSave(player);
        handler.saveToApi();
        SwoftyData.profile().unload(handler.getCurrentProfileId());
        SkyBlockDataHandler.skyBlockCache.remove(playerUuid);
    }

    private static UUID resolveCoopId(UUID profileId) {
        CoopDatabase.Coop coop = CoopDatabase.getFromMemberProfile(profileId);
        return coop != null ? coop.coopUUID() : profileId;
    }

    private static SkyBlockPlayerProfiles loadProfiles(SkyBlockPlayer player) {
        UserDatabase userDatabase = new UserDatabase(player.getUuid());
        SkyBlockPlayerProfiles profiles = userDatabase.getProfiles();

        if (profiles == null) {
            UUID profileId = UUID.randomUUID();
            profiles = new SkyBlockPlayerProfiles(player.getUuid());
            profiles.setCurrentlySelected(profileId);
            profiles.addProfile(profileId);
            userDatabase.saveProfiles(profiles);
            return profiles;
        }

        if (profiles.getCurrentlySelected() == null) {
            UUID profileId = UUID.randomUUID();
            profiles.setCurrentlySelected(profileId);
            profiles.addProfile(profileId);
            userDatabase.saveProfiles(profiles);
        }

        return profiles;
    }

    private static void scheduleRegionRefresh(SkyBlockPlayer player) {
        MathUtility.delay(() -> {
            SkyBlockRegion playerRegion = player.getRegion();
            if (playerRegion != null && player.isOnline()) {
                HypixelEventHandler.callCustomEvent(new PlayerRegionChangeEvent(
                        player,
                        null,
                        playerRegion.getType()
                ));
            }
        }, 50);
    }

    private static void sendProfileIntro(SkyBlockPlayer player) {
        Thread.startVirtualThread(() -> {
            player.sendMessage("");
            player.showTitle(Title.title(
                    Component.text(TestingTexture.FULL_SCREEN_BLACK.toString()),
                    Component.empty(),
                    Title.Times.times(Duration.ZERO, Duration.ofMillis(300), Duration.ofSeconds(1))
            ));

            Rank rank = player.getRank();
            if (rank.isStaff()) {
                CustomGroups.staffMembers.add(player);
            }

            player.sendMessage("§7 ");
            player.sendMessage("§aYou are playing on profile: §e" + player.getSkyblockDataHandler().get(
                    SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
            player.sendMessage("§8Profile ID: " + player.getProfiles().getCurrentlySelected());

            UUID islandUuid = player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue();
            if (!islandUuid.equals(player.getProfiles().getCurrentlySelected())) {
                player.sendMessage("§8Island ID: " + islandUuid);
            }
            player.sendMessage(" ");

            player.health = player.getMaxHealth();
            player.sendPacket(new UpdateHealthPacket((player.health / player.getMaxHealth()) * 20, 20, 20));

            MinecraftServer.getBossBarManager().removeAllBossBars(player);
            MathUtility.delay(() -> {
                if (!player.isOnline()) return;
                player.getPetData().updatePetEntityImpl(player);
            }, 20);

            TravelScrollIslands island = TravelScrollIslands.getFromType(HypixelConst.getTypeLoader().getType());
            if (island != null) {
                List<String> visitedIslands = player.getSkyblockDataHandler()
                        .get(SkyBlockDataHandler.Data.VISITED_ISLANDS, DatapointStringList.class)
                        .getValue();
                if (!visitedIslands.contains(island.getInternalName())) {
                    visitedIslands.add(island.getInternalName());
                    player.getSkyblockDataHandler()
                            .get(SkyBlockDataHandler.Data.VISITED_ISLANDS, DatapointStringList.class)
                            .setValue(visitedIslands);
                }
            }
        });
    }
}

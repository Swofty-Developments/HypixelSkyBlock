package net.swofty.type.skyblockgeneric.user.flow;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.packer.packs.ravengard.TestingTexture;
import net.swofty.proxyapi.PlayerTransferDataCache;
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

        ProfilesDatabase profileDb = new ProfilesDatabase(profileId.toString());
        SkyBlockDataHandler handler;
        boolean shouldPersistProfile = false;

        String transferredProfile = PlayerTransferDataCache.takeProfileDocument(playerUuid);
        if (transferredProfile != null) {
            handler = SkyBlockDataHandler.createFromProfile(playerUuid, profileId, Document.parse(transferredProfile));
        } else if (profileDb.exists()) {
            Document profileDocument = profileDb.getDocument();
            handler = SkyBlockDataHandler.createFromProfile(playerUuid, profileId, profileDocument);
        } else {
            handler = SkyBlockDataHandler.initUserWithDefaultData(playerUuid, profileId);
            shouldPersistProfile = true;
        }

        DatapointUUID islandDatapoint = handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class);
        UUID islandUuid = islandDatapoint.getValue();
        if (islandUuid == null) {
            islandUuid = profileId;
            islandDatapoint.setValue(islandUuid);
            shouldPersistProfile = true;
        }

        DatapointString profileNameDatapoint = handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class);
        if (Objects.equals(profileNameDatapoint.getValue(), "null")) {
            profileNameDatapoint.setValue(SkyBlockPlayerProfiles.getRandomName());
            shouldPersistProfile = true;
        }

        SkyBlockDataHandler.skyBlockCache.put(playerUuid, handler);

        if (shouldPersistProfile) {
            profileDb.saveDocument(handler.toProfileDocument());
        }

        player.setSkyBlockIsland(SkyBlockIsland.getOrCreate(islandUuid, profileId));
    }

    public static void postSpawn(SkyBlockPlayer player) {
        SkyBlockPlayerProfiles profiles = player.getProfiles();
        SkyBlockDataHandler handler = player.getSkyblockDataHandler();
        handler.runOnLoad(player);

        syncCoopValues(player, profiles, handler);
        scheduleRegionRefresh(player);
        sendProfileIntro(player);
    }

    public static void save(SkyBlockPlayer player) {
        UUID playerUuid = player.getUuid();
        SkyBlockDataHandler handler = SkyBlockDataHandler.skyBlockCache.get(playerUuid);

        if (handler == null) return;

        handler.runOnSave(player);

        UUID profileId = handler.getCurrentProfileId();
        ProfilesDatabase profileDb = new ProfilesDatabase(profileId.toString());
        Document newDoc = handler.toProfileDocument();

        if (profileDb.exists()) {
            ProfilesDatabase.collection.replaceOne(com.mongodb.client.model.Filters.eq("_id", profileId.toString()), newDoc);
        } else {
            ProfilesDatabase.collection.insertOne(newDoc);
        }

        SkyBlockDataHandler.skyBlockCache.remove(playerUuid);
    }

    public static String saveForTransfer(SkyBlockPlayer player) {
        SkyBlockDataHandler handler = SkyBlockDataHandler.skyBlockCache.get(player.getUuid());
        if (handler == null) throw new IllegalStateException("SkyBlock profile data is not loaded");

        handler.runOnSave(player);
        Document document = handler.toProfileDocument();
        new ProfilesDatabase(handler.getCurrentProfileId().toString()).saveDocument(document);

        // A profile switch updates the selected profile before the proxy prepares the transfer.
        // Save the profile the player is leaving, but send the destination profile's document;
        // otherwise the destination server loads the old profile data under the new profile ID.
        UUID selectedProfileId = player.getProfiles().getCurrentlySelected();
        if (!handler.getCurrentProfileId().equals(selectedProfileId)) {
            Document selectedDocument = new ProfilesDatabase(selectedProfileId.toString()).getDocument();
            if (selectedDocument == null) {
                throw new IllegalStateException("Selected SkyBlock profile does not exist: " + selectedProfileId);
            }
            return selectedDocument.toJson();
        }

        return document.toJson();
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

    private static void syncCoopValues(SkyBlockPlayer player, SkyBlockPlayerProfiles profiles, SkyBlockDataHandler handler) {
        if (!handler.get(SkyBlockDataHandler.Data.IS_COOP, DatapointBoolean.class).getValue()) return;

        CoopDatabase.Coop coop = CoopDatabase.getFromMember(player.getUuid());
        if (coop == null) return;
        if (coop.members().size() == 1) return;

        SkyBlockDataHandler sourceData;
        if (SkyBlockGenericLoader.getLoadedPlayers().stream()
                .anyMatch(player1 -> !player1.getUuid().equals(player.getUuid()) && coop.members().contains(player1.getUuid()))) {
            SkyBlockPlayer otherCoopMember = SkyBlockGenericLoader.getLoadedPlayers().stream()
                    .filter(player1 -> !player1.getUuid().equals(player.getUuid()) && coop.members().contains(player1.getUuid()))
                    .findFirst()
                    .get();
            sourceData = otherCoopMember.getSkyblockDataHandler();
        } else {
            UUID finalProfileId = profiles.getCurrentlySelected();
            sourceData = SkyBlockDataHandler.createFromProfileOnly(
                    new ProfilesDatabase(coop.memberProfiles().stream()
                            .filter(uuid -> !uuid.equals(finalProfileId))
                            .findFirst()
                            .get().toString()).getDocument()
            );
        }

        sourceData.getCoopValues().forEach((key, value) -> {
            SkyBlockDatapoint<Object> targetDatapoint = (SkyBlockDatapoint<Object>) handler.getSkyBlockDatapoint(key);
            targetDatapoint.setValueBypassCoop(value);
        });
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

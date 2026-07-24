package net.swofty.type.skyblockgeneric.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.swofty.commons.ServerType;
import net.swofty.commons.data.SwoftyData;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.packer.packs.TestingTexture;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.datapoints.DatapointStringList;
import net.swofty.type.generic.data.domain.DomainKey;
import net.swofty.type.generic.data.domain.PlayerDataDomain;
import net.swofty.type.generic.data.domain.PlayerDataService;
import net.swofty.type.generic.data.mongodb.UserDatabase;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.CustomGroups;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointUUID;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegion;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.island.SkyBlockIsland;
import net.swofty.type.skyblockgeneric.warps.TravelScrollIslands;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class SkyBlockDomain implements PlayerDataDomain<SkyBlockDataHandler> {
    public static final DomainKey<SkyBlockDataHandler> KEY = new DomainKey<>("skyblock-profile", SkyBlockDataHandler.class);

    @Override
    public DomainKey<SkyBlockDataHandler> key() {
        return KEY;
    }

    @Override
    public boolean appliesTo(ServerType type) {
        return type.isSkyBlock();
    }

    @Override
    public int order() {
        return 10;
    }

    @Override
    public void load(UUID uuid) {
        if (PlayerDataService.isLoaded(KEY, uuid)) return;

        UUID profileId = loadProfiles(uuid).getCurrentlySelected();
        SkyBlockDataHandler handler = SkyBlockDataHandler.initUserWithDefaultData(uuid, profileId);
        SwoftyData.profile().link(profileId, CoopLinks.COOP, resolveCoopId(profileId));
        handler.loadFromApi();

        DatapointUUID islandDatapoint = handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class);
        if (islandDatapoint.getValue() == null) islandDatapoint.setValue(profileId);

        DatapointString profileNameDatapoint = handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class);
        if (Objects.equals(profileNameDatapoint.getValue(), "null")) {
            profileNameDatapoint.setValue(SkyBlockPlayerProfiles.getRandomName());
        }

        PlayerDataService.store(KEY, uuid, handler);
    }

    @Override
    public void attach(HypixelPlayer player) {
        SkyBlockDataHandler handler = PlayerDataService.get(KEY, player.getUuid());
        UUID islandUuid = handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue();
        ((SkyBlockPlayer) player).setSkyBlockIsland(SkyBlockIsland.getOrCreate(islandUuid, handler.getCurrentProfileId()));
    }

    @Override
    public void applyToPlayer(HypixelPlayer player) {
        SkyBlockPlayer sbp = (SkyBlockPlayer) player;
        PlayerDataService.get(KEY, player.getUuid()).runOnLoad(sbp);
        scheduleRegionRefresh(sbp);
        sendProfileIntro(sbp);
    }

    @Override
    public void save(HypixelPlayer player) {
        SkyBlockDataHandler handler = PlayerDataService.find(KEY, player.getUuid()).orElse(null);
        if (handler == null) return;
        handler.runOnSave((SkyBlockPlayer) player);
        handler.saveToApi();
    }

    @Override
    public void unload(UUID uuid) {
        SkyBlockDataHandler handler = PlayerDataService.find(KEY, uuid).orElse(null);
        if (handler != null) SwoftyData.profile().unload(handler.getCurrentProfileId());
        PlayerDataService.evict(KEY, uuid);
    }

    private static UUID resolveCoopId(UUID profileId) {
        CoopDatabase.Coop coop = CoopDatabase.getFromMemberProfile(profileId);
        return coop != null ? coop.coopUUID() : profileId;
    }

    private static SkyBlockPlayerProfiles loadProfiles(UUID uuid) {
        UserDatabase userDatabase = new UserDatabase(uuid);
        SkyBlockPlayerProfiles profiles = userDatabase.getProfiles();

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
                HypixelEventHandler.callCustomEvent(new PlayerRegionChangeEvent(player, null, playerRegion.getType()));
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
                        .get(SkyBlockDataHandler.Data.VISITED_ISLANDS, DatapointStringList.class).getValue();
                if (!visitedIslands.contains(island.getInternalName())) {
                    visitedIslands.add(island.getInternalName());
                    player.getSkyblockDataHandler()
                            .get(SkyBlockDataHandler.Data.VISITED_ISLANDS, DatapointStringList.class).setValue(visitedIslands);
                }
            }
        });
    }
}

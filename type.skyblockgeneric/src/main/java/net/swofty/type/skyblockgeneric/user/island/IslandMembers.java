package net.swofty.type.skyblockgeneric.user.island;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IslandMembers {

    public static List<SkyBlockPlayer> onlineMembers(UUID islandId, CoopDatabase.Coop coop) {
        if (coop != null) {
            return coop.getOnlineMembers();
        }

        try {
            return List.of(SkyBlockGenericLoader.getPlayerFromProfileUUID(islandId));
        } catch (NullPointerException ignored) {
            return List.of();
        }
    }

    public static List<UUID> memberProfiles(UUID islandId, CoopDatabase.Coop coop) {
        return coop != null ? coop.memberProfiles() : List.of(islandId);
    }
}

package net.swofty.type.bedwarslobby.util;

import net.swofty.commons.StringUtility;
import net.swofty.commons.bedwars.BedwarsLevelColor;
import net.swofty.commons.bedwars.BedwarsLevelUtil;
import net.swofty.type.generic.data.HypixelDataHandler;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardLong;
import net.swofty.type.generic.data.datapoints.DatapointRank;
import net.swofty.type.generic.data.datapoints.DatapointString;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.user.categories.Rank;
import org.jetbrains.annotations.Blocking;

import java.util.UUID;

public record BedwarsNameFormatter(UUID uuid) {
    @Blocking
    public String getDisplayName() {
        BedWarsDataHandler bwHandler = BedWarsDataHandler.getOfOfflinePlayer(uuid);
        String levelPrefix = BedwarsLevelColor.constructLevelBrackets(
                BedwarsLevelUtil.calculateLevel(bwHandler.get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLeaderboardLong.class).getValue())
        ) + " ";

        HypixelDataHandler handler = HypixelDataHandler.getOfOfflinePlayer(uuid);
        Rank rank = handler.get(HypixelDataHandler.Data.RANK, DatapointRank.class).getValue();
        String rankPrefix = rank.getPrefix();

        String ign = handler.get(HypixelDataHandler.Data.IGN, DatapointString.class).getValue();

        return levelPrefix + rankPrefix + ign;
    }
}

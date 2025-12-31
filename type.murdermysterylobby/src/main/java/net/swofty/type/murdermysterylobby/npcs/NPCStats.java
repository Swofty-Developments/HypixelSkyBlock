package net.swofty.type.murdermysterylobby.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.murdermystery.MurderMysteryLeaderboardPeriod;
import net.swofty.commons.murdermystery.MurderMysteryModeStats;
import net.swofty.type.generic.achievement.AchievementCategory;
import net.swofty.type.generic.achievement.AchievementRegistry;
import net.swofty.type.generic.achievement.PlayerAchievementHandler;
import net.swofty.type.generic.data.datapoints.DatapointMurderMysteryModeStats;
import net.swofty.type.generic.data.handlers.MurderMysteryDataHandler;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.murdermysterylobby.gui.GUIMurderMysteryStatistics;

import java.text.DecimalFormat;

public class NPCStats extends HypixelNPC {
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,###");

    public NPCStats() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                // Get actual achievement count from player data
                PlayerAchievementHandler achievementHandler = new PlayerAchievementHandler(player);
                int playerAchievements = achievementHandler.getUnlockedCount(AchievementCategory.MURDER_MYSTERY);
                int totalAchievements = AchievementRegistry.getByCategory(AchievementCategory.MURDER_MYSTERY).size();

                // Get actual wins from player data
                long totalWins = 0;
                MurderMysteryDataHandler handler = MurderMysteryDataHandler.getUser(player);
                if (handler != null) {
                    MurderMysteryModeStats stats = handler.get(MurderMysteryDataHandler.Data.MODE_STATS, DatapointMurderMysteryModeStats.class).getValue();
                    totalWins = stats.getTotalWins(MurderMysteryLeaderboardPeriod.LIFETIME);
                }

                return new String[]{
                        "§6§lYour Murder Mystery Profile",
                        "§fAchievements: §e" + playerAchievements + "§7/§a" + totalAchievements,
                        "§fTotal Wins: §a" + NUMBER_FORMAT.format(totalWins),
                        "§e§lCLICK FOR STATS",
                };
            }

            @Override
            public String texture(HypixelPlayer player) {
                return player.getPlayerSkin().textures();
            }

            @Override
            public String signature(HypixelPlayer player) {
                return player.getPlayerSkin().signature();
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(3.5, 68, 10.5, -130, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return false;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        new GUIMurderMysteryStatistics().open(event.player());
    }
}

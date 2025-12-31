package net.swofty.type.skyblockgeneric.mission;

import lombok.Getter;
import lombok.SneakyThrows;
import net.swofty.type.skyblockgeneric.mission.missions.*;
import net.swofty.type.skyblockgeneric.mission.missions.barn.MissionCraftWheatMinion;
import net.swofty.type.skyblockgeneric.mission.missions.barn.MissionTalkToFarmHand;
import net.swofty.type.skyblockgeneric.mission.missions.barn.MissionTalkToFarmhandAgain;
import net.swofty.type.skyblockgeneric.mission.missions.blacksmith.MissionMineCoal;
import net.swofty.type.skyblockgeneric.mission.missions.blacksmith.MissionTalkToBlacksmith;
import net.swofty.type.skyblockgeneric.mission.missions.blacksmith.MissionTalkToBlacksmithAgain;
import net.swofty.type.skyblockgeneric.mission.missions.farmer.MissionCollectWheat;
import net.swofty.type.skyblockgeneric.mission.missions.farmer.MissionTalkToFarmer;
import net.swofty.type.skyblockgeneric.mission.missions.farmer.MissionTalkToFarmerAgain;
import net.swofty.type.skyblockgeneric.mission.missions.goldmine.lazyminer.MissionFindLazyMinerPickaxe;
import net.swofty.type.skyblockgeneric.mission.missions.goldmine.lazyminer.MissionTalkToLazyMiner;
import net.swofty.type.skyblockgeneric.mission.missions.lumber.MissionBreakOaklog;
import net.swofty.type.skyblockgeneric.mission.missions.lumber.MissionTalkToLumberjack;
import net.swofty.type.skyblockgeneric.mission.missions.lumber.MissionTalkToLumberjackAgain;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.birchpark.*;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.darkthicket.*;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.jungle.*;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.savanna.MissionCheckOnMelody;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.savanna.MissionCollectAcaciaLogs;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.savanna.MissionGiveMelodyAcaciaLogs;
import net.swofty.type.skyblockgeneric.mission.missions.thepark.savanna.MissionTravelToTheSavannaWoodland;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

@Getter
public enum MissionSet {
    GETTING_STARTED(MissionBreakLog.class, MissionCraftWorkbench.class, MissionCraftWoodenPickaxe.class, MissionTalkJerry.class, MissionUseTeleporter.class),
    SAVING_UP(MissionTalkToBanker.class, MissionDepositCoinsInBank.class),
    LIBRARY_CARD(MissionTalkToLibrarian.class),
    AUCTIONEER(MissionTalkToAuctionMaster.class),
    TIME_TO_MINE(MissionTalkToBlacksmith.class, MissionMineCoal.class, MissionTalkToBlacksmithAgain.class),
    TIME_TO_STRIKE(MissionKillZombies.class, MissionTalkToBartender.class),
    TIMBER(MissionTalkToLumberjack.class, MissionBreakOaklog.class, MissionTalkToLumberjackAgain.class),
    INTO_THE_WOODS(MissionTravelToThePark.class, MissionTalkToCharlie.class, MissionCollectBirchLogs.class, MissionGiveCharlieBirchLogs.class, MissionClaimTheTrousers.class, MissionTalkToCharlieAgain.class),
    FIRST_HARVEST(MissionTalkToFarmer.class, MissionCollectWheat.class, MissionTalkToFarmerAgain.class),
    BACK_AT_THE_BARNYARD(MissionTalkToFarmHand.class, MissionCraftWheatMinion.class, MissionTalkToFarmhandAgain.class),
    CARPENTRY(MissionGiveWoolToCarpenter.class),
    LOST_AND_FOUND(MissionFindLazyMinerPickaxe.class, MissionTalkToLazyMiner.class),
    THE_CAMPFIRE_CULT(MissionTravelToTheDarkThicket.class, MissionFindTheCampfire.class, MissionSneakUpOnRyan.class, MissionCompleteTrialOfFireOne.class, MissionTalkToRyan.class, MissionCollectDarkOakLogs.class, MissionGiveRyanDarkOakLogs.class),
    THE_REBUILD(MissionTravelToTheSavannaWoodland.class, MissionCheckOnMelody.class, MissionCollectAcaciaLogs.class, MissionGiveMelodyAcaciaLogs.class),
    MOLE_PROBLEMS(MissionTalkToMolbert.class, MissionCollectJungleLogs.class, MissionGiveMolbertJungleLogs.class, MissionLeaveTheArea.class, MissionTalkToMolbertAgain.class, MissionPlaceTraps.class, MissionTalkToMolbertAgainAgain.class,
            MissionLeaveTheAreaAgain.class, MissionTalkToMolbertAgainAgainAgain.class)
    ;

    private final Class<? extends SkyBlockMission>[] missions;

    @SafeVarargs
    MissionSet(Class<? extends SkyBlockMission>... missions) {
        this.missions = missions;
    }

    /**
     * @param missionID The mission ID to check
     * @return The mission set that the mission is in, or null if it is not in a set
     */
    @SneakyThrows
    public static MissionSet getFromMission(String missionID) {
        for (MissionSet missionSet : MissionSet.values()) {
            for (Class<? extends SkyBlockMission> mission : missionSet.missions) {
                if (mission.newInstance().getID().equalsIgnoreCase(missionID)) {
                    return missionSet;
                }
            }
        }

        return null;
    }

    /**
     * @param player The player to check
     * @return Whether the player has completed all missions in the set
     */
    @SneakyThrows
    public boolean hasCompleted(SkyBlockPlayer player) {
        for (Class<? extends SkyBlockMission> mission : missions) {
            if (!player.getMissionData().hasCompleted(mission.newInstance().getID())) {
                return false;
            }
        }

        return true;
    }

    /**
     * @param player The player to get the next mission for
     * @return The next mission in the set, or null if there are no more missions
     */
    @SneakyThrows
    public @Nullable Class<? extends SkyBlockMission> getNextMission(SkyBlockPlayer player) {
        for (Class<? extends SkyBlockMission> mission : missions) {
            if (!player.getMissionData().hasCompleted(mission.newInstance().getID())) {
                return mission;
            }
        }

        return null;
    }
}

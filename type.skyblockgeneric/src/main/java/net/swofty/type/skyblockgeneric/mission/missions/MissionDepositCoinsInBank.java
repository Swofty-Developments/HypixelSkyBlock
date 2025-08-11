package net.swofty.type.skyblockgeneric.mission.missions;

import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.skyblockgeneric.mission.MissionData;
import net.swofty.type.skyblockgeneric.mission.HypixelMission;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;

public class MissionDepositCoinsInBank extends HypixelMission {

    @Override
    public String getID() {
        return "deposit_coins_in_bank";
    }

    @Override
    public String getName() {
        return "Deposit coins in the Bank";
    }

    @Override
    public Map<String, Object> onStart(SkyBlockPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(SkyBlockPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("§610 §7Coins")))
                .forEach(player::sendMessage);
        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(
                player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue() + 10
        );
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.BANK);
    }
}

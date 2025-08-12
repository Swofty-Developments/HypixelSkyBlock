package net.swofty.type.skyblockgeneric.mission.missions;

import net.swofty.type.generic.data.datapoints.DatapointDouble;
import net.swofty.type.generic.mission.MissionData;
import net.swofty.type.generic.mission.HypixelMission;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;

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
    public Map<String, Object> onStart(HypixelPlayer player, MissionData.ActiveMission mission) {
        return new HashMap<>();
    }

    @Override
    public void onEnd(HypixelPlayer player, Map<String, Object> customData, MissionData.ActiveMission mission) {
        mission.getObjectiveCompleteText(new ArrayList<>(List.of("§610 §7Coins")))
                .forEach(player::sendMessage);
        player.getSkyBlockData().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(
                player.getSkyBlockData().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COINS, DatapointDouble.class).getValue() + 10
        );
    }

    @Override
    public Set<RegionType> getValidRegions() {
        return Collections.singleton(RegionType.BANK);
    }
}

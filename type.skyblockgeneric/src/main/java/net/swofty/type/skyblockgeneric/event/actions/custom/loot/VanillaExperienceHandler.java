package net.swofty.type.skyblockgeneric.event.actions.custom.loot;

import net.swofty.type.skyblockgeneric.bestiary.BestiaryData;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class VanillaExperienceHandler implements HypixelEventClass {
    BestiaryData bestiaryData = new BestiaryData();

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
    public void run(PlayerKilledSkyBlockMobEvent event) {

        SkyBlockPlayer player = event.getPlayer();
        SkyBlockMob mob = event.getKilledMob();

        if (mob == null) return;

        long xpOrbsDropped;

        //Base XP Orbs (additive)
        long baseXpOrbs = mob.getOtherLoot().getXpOrbAmount();

        //Bestiary Boost (multiplicative)
        float bestiaryBoost = 1;
        if (mob instanceof BestiaryMob bestiaryMob) {
            int tier = bestiaryData.getCurrentBestiaryTier(bestiaryMob, player.getBestiaryData().getAmount(bestiaryMob));
            bestiaryBoost += (float) bestiaryData.getTotalExtraXPPercentage(tier) / 100;
        }

        xpOrbsDropped = Math.round((baseXpOrbs) * bestiaryBoost);

        player.addExperience(xpOrbsDropped);
    }
}

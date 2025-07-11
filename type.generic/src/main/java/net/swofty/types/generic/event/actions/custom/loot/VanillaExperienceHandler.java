package net.swofty.types.generic.event.actions.custom.loot;

import net.swofty.types.generic.bestiary.BestiaryData;
import net.swofty.types.generic.entity.mob.BestiaryMob;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class VanillaExperienceHandler implements SkyBlockEventClass {
    BestiaryData bestiaryData = new BestiaryData();

    @SkyBlockEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
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

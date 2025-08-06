package net.swofty.types.generic.event.actions.custom.loot;

import net.swofty.types.generic.bestiary.BestiaryData;
import net.swofty.types.generic.enchantment.impl.EnchantmentScavenger;
import net.swofty.types.generic.entity.mob.BestiaryMob;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class CoinDropHandler implements SkyBlockEventClass {
    BestiaryData bestiaryData = new BestiaryData();

    @SkyBlockEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
    public void run(PlayerKilledSkyBlockMobEvent event) {

        SkyBlockPlayer player = event.getPlayer();
        SkyBlockMob mob = event.getKilledMob();

        if (mob == null) return;

        int coinsDropped;

        //Base Mob Coins (additive)
        int baseCoins = mob.getOtherLoot().getCoinAmount();

        //Scavenger Enchant (additive)
        EnchantmentScavenger enchantmentScavenger = new EnchantmentScavenger();
        int scavengedCoins = enchantmentScavenger.getScavengedCoins(event);

        //Bestiary Boost (multiplicative)
        float bestiaryBoost = 1;
        if (mob instanceof BestiaryMob bestiaryMob) {
            int tier = bestiaryData.getCurrentBestiaryTier(bestiaryMob, player.getBestiaryData().getAmount(bestiaryMob));
            bestiaryBoost += (float) bestiaryData.getTotalExtraCoinPercentage(tier) / 100;
        }

        coinsDropped = Math.round((baseCoins + scavengedCoins) * bestiaryBoost);

        player.addCoins(coinsDropped);
    }
}

package net.swofty.type.skyblockgeneric.event.actions.custom.loot;

import net.swofty.type.skyblockgeneric.bestiary.BestiaryData;
import net.swofty.type.skyblockgeneric.enchantment.impl.EnchantmentScavenger;
import net.swofty.type.skyblockgeneric.entity.mob.BestiaryMob;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.PlayerKilledSkyBlockMobEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class CoinDropHandler implements HypixelEventClass {
    BestiaryData bestiaryData = new BestiaryData();

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
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

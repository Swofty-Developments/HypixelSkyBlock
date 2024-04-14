package net.swofty.types.generic.item.set.sets;

import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.types.generic.event.value.SkyBlockValueEvent;
import net.swofty.types.generic.event.value.ValueUpdateEvent;
import net.swofty.types.generic.event.value.events.MiningValueUpdateEvent;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class MinerOutfitSet extends SkyBlockValueEvent implements ArmorSet {

    @Override
    public String getName() {
        return "Haste";
    }

    @Override
    public String getDescription() {
        return "§7Grants the wearer with §apermanent Haste II §7while worn.";
    }

    @Override
    public Class<? extends ValueUpdateEvent> getValueEvent() {
        return MiningValueUpdateEvent.class;
    }

    @Override
    public void run(ValueUpdateEvent event) {
        if (!isWearingSet(event.getPlayer())) return;
        SkyBlockPlayer player = event.getPlayer();
        player.addEffect(new Potion(PotionEffect.HASTE, (byte) 2 , 1000000));
    }
}

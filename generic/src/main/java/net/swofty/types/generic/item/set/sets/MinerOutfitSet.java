package net.swofty.types.generic.item.set.sets;

import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.item.set.impl.SetEvents;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.List;

public class MinerOutfitSet implements ArmorSet, SetEvents {

    @Override
    public String getName() {
        return "Haste";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(
                "§7Grants the wearer with §apermanent",
                "§7Haste II §7while worn.");
    }

    @Override
    public void setPutOn(SkyBlockPlayer player) {
        player.addEffect(new Potion(PotionEffect.HASTE, (byte) 2, -1));
    }

    @Override
    public void setTakeOff(SkyBlockPlayer player) {
        player.removeEffect(PotionEffect.HASTE);
    }
}

package net.swofty.type.skyblockgeneric.item.set.sets;

import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.generic.item.set.impl.ArmorSet;
import net.swofty.type.generic.item.set.impl.MuseumableSet;
import net.swofty.type.generic.item.set.impl.SetEvents;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class MinerOutfitSet implements ArmorSet, SetEvents, MuseumableSet {

    @Override
    public String getName() {
        return "Haste";
    }

    @Override
    public ArrayList<String> getDescription() {
        return new ArrayList<>(Arrays.asList(
                "§7Grants the wearer with §apermanent",
                "§aHaste II §7while worn."
        ));
    }

    @Override
    public void setPutOn(HypixelPlayer player) {
        player.addEffect(new Potion(PotionEffect.HASTE, (byte) 1, -1));
    }

    @Override
    public void setTakeOff(HypixelPlayer player) {
        player.removeEffect(PotionEffect.HASTE);
    }
}

package net.swofty.type.skyblockgeneric.item.set.sets;

import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.generic.item.set.impl.ArmorSet;
import net.swofty.type.generic.item.set.impl.SetEvents;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class MushroomSet implements ArmorSet, SetEvents {
    @Override
    public String getName() {
        return "Night Affinity";
    }

    @Override
    public ArrayList<String> getDescription() {
        return new ArrayList<>(Arrays.asList(
                "§7Grants the wearer permanent §aNight",
                "§aVision §7while worn. During the night,",
                "the stats of the armor pieces are",
                "§atrippled§7."
        ));
    }

    @Override
    public void setPutOn(HypixelPlayer player) {
        player.addEffect(new Potion(PotionEffect.NIGHT_VISION, (byte) 0, -1));
    }

    @Override
    public void setTakeOff(HypixelPlayer player) {
        player.removeEffect(PotionEffect.NIGHT_VISION);
    }
}

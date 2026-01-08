package net.swofty.type.skyblockgeneric.item.set.sets;

import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skyblockgeneric.item.set.impl.ArmorSet;
import net.swofty.type.skyblockgeneric.item.set.impl.SetEvents;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MushroomSet implements ArmorSet, SetEvents {
    @Override
    public String getName() {
        return "Night Affinity";
    }

    @Override
    public ArrayList<String> getDescription() {
        return new ArrayList<>(List.of(
                "§7Grants the wearer permanent §aNight",
                "§aVision §7while worn. During the night,",
                "the stats of the armor pieces are",
                "§atripled§7."
        ));
    }

    @Override
    public void setPutOn(SkyBlockPlayer player) {
        player.addEffect(new Potion(PotionEffect.NIGHT_VISION, (byte) 0, -1));
    }

    @Override
    public void setTakeOff(SkyBlockPlayer player) {
        player.removeEffect(PotionEffect.NIGHT_VISION);
    }
}

package net.swofty.types.generic.item.set.sets;

import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.types.generic.item.set.impl.ArmorSet;
import net.swofty.types.generic.item.set.impl.SetEvents;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class MushroomSet implements ArmorSet, SetEvents {
    @Override
    public String getName() {
        return "Night Affinity";
    }

    @Override
    public String getDescription() {
        return """
                §7Grants the wearer with §aNight
                §aVision §7while worn. During the night,
                the stats of the armor pieces are
                §atrippled§7.""";
    }

    @Override
    public void setPutOn(SkyBlockPlayer player) {
        player.addEffect(new Potion(PotionEffect.NIGHT_VISION, (byte) 1, -1));
    }

    @Override
    public void setTakeOff(SkyBlockPlayer player) {
        player.removeEffect(PotionEffect.NIGHT_VISION);
    }
}

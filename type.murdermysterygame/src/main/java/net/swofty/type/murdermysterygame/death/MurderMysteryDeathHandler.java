package net.swofty.type.murdermysterygame.death;

import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import org.jetbrains.annotations.Nullable;

public class MurderMysteryDeathHandler {

    public static MurderMysteryDeathResult handleDeath(MurderMysteryPlayer victim, @Nullable MurderMysteryPlayer killer, MurderMysteryDeathType deathType) {
        return new MurderMysteryDeathResult(victim, killer, deathType);
    }
}

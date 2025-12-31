package net.swofty.type.murdermysterygame.death;

import net.swofty.type.murdermysterygame.user.MurderMysteryPlayer;
import org.jetbrains.annotations.Nullable;

public record MurderMysteryDeathResult(
        MurderMysteryPlayer victim,
        @Nullable MurderMysteryPlayer killer,
        MurderMysteryDeathType deathType
) {
    public boolean hasKiller() {
        return killer != null;
    }
}

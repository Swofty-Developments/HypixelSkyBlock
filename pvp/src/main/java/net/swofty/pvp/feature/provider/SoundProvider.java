package net.swofty.pvp.feature.provider;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import net.swofty.pvp.feature.CombatFeature;
import org.jetbrains.annotations.NotNull;

public interface SoundProvider extends CombatFeature {
    SoundProvider DEFAULT = Audience::playSound;

    // TODO: support emitters
    void playSound(Audience audience, final @NotNull Sound original, final double x, final double y, final double z);
}

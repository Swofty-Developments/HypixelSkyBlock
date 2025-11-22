package net.swofty.pvp.projectile;

import net.minestom.server.coordinate.Point;
import org.jetbrains.annotations.NotNull;

public interface Projectile {
    void shoot(@NotNull Point from, double power, double spread);
    void shoot(@NotNull Point from, @NotNull Point to, double power, double spread);
}
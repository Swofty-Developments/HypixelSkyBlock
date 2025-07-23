package net.swofty.types.generic.utility;

import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.EntityDamage;
import net.swofty.types.generic.entity.mob.SkyBlockMob;
import org.tinylog.Logger;

public record DeathMessageCreator(Damage type) {
    public String createPersonal() {
        return switch (type.getType().name()) {
            case "minecraft:mob_attack":
                yield "were slain by " + ((SkyBlockMob) ((EntityDamage) type).getSource()).getDisplayName() + ".";
            case "minecraft:out_of_world":
                yield "fell out of the world.";
            case "minecraft:on_fire":
                yield "burned to death.";
            case "minecraft:fall":
                yield "fell from a high place.";
            default:
                Logger.warn("Unknown death type: " + type.getType().name());
                yield "died.";
        };
    }

    public String createOther() {
        return switch (type.getType().name()) {
            case "minecraft:mob_attack":
                yield "was slain by " + ((SkyBlockMob) ((EntityDamage) type).getSource()).getDisplayName() + ".";
            case "minecraft:out_of_world":
                yield "fell out of the world.";
            case "minecraft:on_fire":
                yield "burned to death.";
            case "minecraft:fall":
                yield "fell from a high place.";
            default:
                Logger.warn("Unknown death type: " + type.getType().name());
                yield "died.";
        };
    }
}

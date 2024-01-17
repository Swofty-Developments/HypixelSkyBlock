package net.swofty.types.generic.utility;

import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.damage.EntityDamage;
import net.swofty.types.generic.entity.mob.SkyBlockMob;

public record DeathMessageCreator(Damage type) {
    public String createPersonal() {
        System.out.println(type.getType().name());

        return switch (type.getType().name()) {
            case "entity_source":
                yield "were slain by " + ((SkyBlockMob) ((EntityDamage) type).getSource()).getDisplayName();
            case "attack.outOfWorld":
                yield "fell out of the world";
            case "attack.onFire":
                yield "burned to death";
            case "attack.fall":
                yield "fell from a high place";
            default:
                yield "died";
        };
    }

    public String createOther() {
        return switch (type.getType().name()) {
            case "entity_source":
                yield "was slain by " + ((SkyBlockMob) ((EntityDamage) type).getSource()).getDisplayName();
            case "attack.outOfWorld":
                yield "fell out of the world";
            case "attack.onFire":
                yield "burned to death";
            case "attack.fall":
                yield "fell from a high place";
            default:
                yield "died";
        };
    }
}

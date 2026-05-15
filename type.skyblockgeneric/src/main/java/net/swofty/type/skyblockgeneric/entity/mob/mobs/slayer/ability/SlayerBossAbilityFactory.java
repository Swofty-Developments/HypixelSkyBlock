package net.swofty.type.skyblockgeneric.entity.mob.mobs.slayer.ability;

import net.swofty.type.skyblockgeneric.slayer.SlayerType;

public final class SlayerBossAbilityFactory {
    private SlayerBossAbilityFactory() {
    }

    public static SlayerBossAbility create(SlayerType type) {
        return switch (type) {
            case REVENANT_HORROR -> new RevenantHorrorAbility();
            case TARANTULA_BROODFATHER -> new TarantulaBroodfatherAbility();
            case SVEN_PACKMASTER -> new SvenPackmasterAbility();
            case VOIDGLOOM_SERAPH -> new VoidgloomSeraphAbility();
            case INFERNO_DEMONLORD -> new InfernoDemonlordAbility();
            case RIFTSTALKER_BLOODFIEND -> SlayerBossAbility.none();
        };
    }
}

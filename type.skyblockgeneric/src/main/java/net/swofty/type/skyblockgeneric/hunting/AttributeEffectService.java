package net.swofty.type.skyblockgeneric.hunting;

import net.swofty.commons.ServerType;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointHunting;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.concurrent.ThreadLocalRandom;

import static net.swofty.type.skyblockgeneric.hunting.AttributeDefinition.*;

public final class AttributeEffectService {
    private AttributeEffectService() {
    }

    public static ItemStatistics statistics(SkyBlockPlayer player) {
        DatapointHunting.HuntingData data = player.getHuntingData();
        ItemStatistics result = ItemStatistics.empty();
        for (AttributeDefinition definition : AttributeRegistry.values()) {
            int level = data.level(definition.id());
            if (level == 0 || !data.enabled(definition.id())) continue;
            for (AttributeEffect effect : definition.effects()) {
                if (effect.statistic() == null || !active(effect.condition())) continue;
                double value = effect.atLevel(level) * familyMultiplier(data, definition);
                result = effect.type() == EffectType.PERCENTAGE_STATISTIC_PER_LEVEL
                        ? result.addAdditive(effect.statistic(), value / 100D) : result.addBase(effect.statistic(), value);
            }
        }
        return result;
    }

    public static double value(DatapointHunting.HuntingData data, AttributeId id) {
        AttributeDefinition definition = AttributeRegistry.get(id);
        if (definition == null || !data.enabled(id.toString())) return 0;
        return definition.effects().stream().findFirst().map(effect -> effect.atLevel(data.level(id.toString()))
                * familyMultiplier(data, definition)).orElse(0D);
    }

    public static double outgoingDamageMultiplier(DatapointHunting.HuntingData data, SkyBlockMob mob, boolean ranged) {
        double percentage = value(data, AttributeId.parse(ranged ? "R30" : "E27"));
        percentage += typedValue(data, mob, MobType.SKELETAL, "C12");
        percentage += typedValue(data, mob, MobType.UNDEAD, "U24");
        percentage += typedValue(data, mob, MobType.WOODLAND, "U29");
        percentage += typedValue(data, mob, MobType.ARTHROPOD, "U33");
        percentage += typedValue(data, mob, MobType.ENDER, "U36");
        percentage += typedValue(data, mob, MobType.MAGMATIC, "U38");
        percentage += typedValue(data, mob, MobType.HUMANOID, "R18");
        percentage += typedValue(data, mob, MobType.INFERNAL, "E18");
        return 1D + percentage / 100D;
    }

    public static double resistanceDefense(DatapointHunting.HuntingData data, SkyBlockMob mob) {
        return typedValue(data, mob, MobType.ARTHROPOD, "C15") + typedValue(data, mob, MobType.UNDEAD, "C18")
                + typedValue(data, mob, MobType.ENDER, "C21") + typedValue(data, mob, MobType.INFERNAL, "C30")
                + typedValue(data, mob, MobType.MYTHOLOGICAL, "C39");
    }

    public static double hunterFortune(SkyBlockPlayer player, AttributeDefinition hunted) {
        double fortune = player.getStatistics().allStatistics().getOverall(ItemStatistic.HUNTER_FORTUNE);
        String karma = switch (hunted.rarity()) {
            case COMMON -> "C35";
            case UNCOMMON -> "U8";
            case RARE -> "R8";
            case EPIC -> "E5";
            case LEGENDARY -> "L29";
        };
        return fortune + (karma == null ? 0 : value(player.getHuntingData(), AttributeId.parse(karma)));
    }

    public static int fortunateAmount(SkyBlockPlayer player, AttributeDefinition hunted) {
        double fortune = hunterFortune(player, hunted);
        int amount = 1 + (int) (fortune / 100D);
        return ThreadLocalRandom.current().nextDouble() < fortune % 100D / 100D ? amount + 1 : amount;
    }

    private static double typedValue(DatapointHunting.HuntingData data, SkyBlockMob mob, MobType type, String id) {
        return mob.getMobTypes().contains(type) ? value(data, AttributeId.parse(id)) : 0;
    }

    private static boolean active(Condition condition) {
        ServerType server = HypixelConst.getTypeLoader().getType();
        return switch (condition) {
            case ALWAYS -> true;
            case DAY ->
                    net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar.getHour() >= 6 && net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar.getHour() < 20;
            case NIGHT ->
                    net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar.getHour() < 6 || net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar.getHour() >= 20;
            case FORAGING_ISLAND -> server == ServerType.SKYBLOCK_GALATEA || server == ServerType.SKYBLOCK_THE_PARK;
            case FISHING_ISLAND ->
                    server == ServerType.SKYBLOCK_BACKWATER_BAYOU || server == ServerType.SKYBLOCK_JERRYS_WORKSHOP;
            case COMBAT_ISLAND ->
                    server == ServerType.SKYBLOCK_SPIDERS_DEN || server == ServerType.SKYBLOCK_THE_END || server == ServerType.SKYBLOCK_CRIMSON_ISLE;
            case END -> server == ServerType.SKYBLOCK_THE_END;
        };
    }

    private static double familyMultiplier(DatapointHunting.HuntingData data, AttributeDefinition definition) {
        double multiplier = 1, echo = 1 + rawValue(data, "L6") / 100D;
        if (definition.family() == AttributeFamily.ELEMENTAL && !definition.id().equals(AttributeId.parse("L44")))
            multiplier += rawValue(data, "L44") * echo / 100D;
        return multiplier;
    }

    private static double rawValue(DatapointHunting.HuntingData data, String id) {
        AttributeDefinition definition = AttributeRegistry.get(id);
        if (definition == null || !data.enabled(id)) return 0;
        return definition.effects().stream().findFirst().map(effect -> effect.atLevel(data.level(id))).orElse(0D);
    }
}

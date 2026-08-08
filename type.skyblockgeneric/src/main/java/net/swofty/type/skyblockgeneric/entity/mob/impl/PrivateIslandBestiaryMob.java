package net.swofty.type.skyblockgeneric.entity.mob.impl;

import net.minestom.server.entity.attribute.Attribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.List;

public abstract class PrivateIslandBestiaryMob extends SimpleBestiaryMob implements RegionPopulator {
    private final double[] healthByLevel;
    private final double[] damageByLevel;
    private int activeLevel;

    protected PrivateIslandBestiaryMob(MobDefinition definition, double[] healthByLevel, double[] damageByLevel) {
        super(definition, false);
        if (healthByLevel.length != damageByLevel.length || healthByLevel.length == 0) {
            throw new IllegalArgumentException("Private island mob stat arrays must have the same non-zero length");
        }
        this.healthByLevel = Arrays.copyOf(healthByLevel, healthByLevel.length);
        this.damageByLevel = Arrays.copyOf(damageByLevel, damageByLevel.length);
        this.activeLevel = Math.max(1, Math.min(definition.level(), healthByLevel.length));
        initializeMob();
    }

    @Override
    public Integer getLevel() {
        return activeLevel;
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        int index = activeLevel - 1;
        return statistics(healthByLevel[index], damageByLevel[index], definition.speed());
    }

    @Override
    public void onSpawn() {
        if (getInstance() == null) {
            return;
        }

        getInstance().getPlayers().stream()
                .filter(SkyBlockPlayer.class::isInstance)
                .map(SkyBlockPlayer.class::cast)
                .findFirst()
                .ifPresent(player -> applyCombatLevel(player.getSkills().getCurrentLevel(SkillCategories.COMBAT)));
    }

    @Override
    public List<Populator> getPopulators() {
        return List.of(new Populator(RegionType.PRIVATE_ISLAND, 20));
    }

    private void applyCombatLevel(int combatLevel) {
        int level = switch (combatLevel) {
            case 0, 1, 2, 3, 4 -> 1;
            case 5, 6, 7 -> 2;
            case 8, 9 -> 3;
            case 10, 11 -> 4;
            case 12, 13 -> 5;
            case 14, 15 -> 6;
            case 16, 17 -> 7;
            case 18, 19 -> 8;
            case 20 -> 9;
            case 21, 22, 23 -> 10;
            case 24, 25 -> 11;
            case 26, 27 -> 12;
            case 28, 29 -> 13;
            case 30, 31 -> 14;
            default -> 15;
        };
        activeLevel = Math.min(level, healthByLevel.length);
        double health = healthByLevel[activeLevel - 1];
        getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);
        setHealth((float) health);
        updateCustomName(net.kyori.adventure.text.Component.text("§8[§7Lv" + activeLevel + "§8] §c")
                .append(getMobTypes().getFirst().getDisplaySymbolComponent())
                .append(net.kyori.adventure.text.Component.text("§c " + getDisplayName() + " §a" + Math.round(health) + "§f/§a" + Math.round(health))));
    }
}

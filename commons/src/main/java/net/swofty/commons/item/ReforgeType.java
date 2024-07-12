package net.swofty.commons.item;


import lombok.Getter;
import net.swofty.commons.statistics.ItemStatistic;
import net.swofty.commons.statistics.ItemStatistics;

import java.util.List;
import java.util.function.BiFunction;

@Getter
public enum ReforgeType {
    SWORDS(List.of(
            new Reforge("Epic", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        15, 20, 25, 32, 40, 50);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        10, 15, 20, 27, 35, 45);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        1, 2, 4, 7, 10, 15);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            }),
            new Reforge("Gentle", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        3, 5, 7, 10, 15, 20);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        8, 10, 15, 20, 25, 30);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            }),
            new Reforge("Odd", (originalStatistics, level) -> {
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        12, 15, 15, 20, 25, 30);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        10, 15, 15, 22, 30, 40);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        -5, -10, -18, -32, -50, -75);
                return originalStatistics
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level));
            }),
            new Reforge("Fair", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            }),
            new Reforge("Fast", (originalStatistics, level) -> {
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        10, 20, 30, 40, 50, 60);
                return originalStatistics
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            }),
            new Reforge("Sharp", (originalStatistics, level) -> {
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        10, 12, 14, 17, 20, 25);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        20, 30, 40, 55, 75, 90);
                return originalStatistics
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level));
            }),
            new Reforge("Heroic", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        15, 20, 25, 32, 40, 50);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        40, 50, 65, 80, 100, 125);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        1, 2, 2, 3, 5, 7);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level));
            }),
            new Reforge("Spicy", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        1, 1, 1, 1, 1, 1);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        25, 35, 45, 60, 80, 100);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        1, 2, 4, 7, 10, 15);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            }),
            new Reforge("Legendary", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        3, 7, 12, 18, 25, 32);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        5, 7, 9, 12, 15, 18);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        5, 10, 15, 22, 28, 36);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        5, 8, 12, 18, 25, 35);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        2, 3, 5, 7, 10, 15);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            })
    )),
    BOWS(List.of(
            new Reforge("Grand", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        25, 32, 40, 50, 60, 75);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level));
            }),
            new Reforge("Deadly", (originalStatistics, level) -> {
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        10, 13, 16, 19, 22, 25);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        5, 10, 18, 32, 50, 78);
                return originalStatistics
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level));
            }),
            new Reforge("Fine", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        3, 7, 12, 18, 25, 33);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        5, 7, 9, 12, 15, 18);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        2, 4, 7, 10, 15, 20);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level));
            }),
            new Reforge("Hasty", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        3, 5, 7, 10, 15, 20);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        20, 25, 30, 40, 50, 60);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level));
            }),
            new Reforge("Neat", (originalStatistics, level) -> {
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        4, 8, 14, 20, 30, 40);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        10, 12, 14, 17, 20, 25);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        3, 6, 10, 15, 20, 25);
                return originalStatistics
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level));
            }),
            new Reforge("Awkward", (originalStatistics, level) -> {
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        5, 10, 15, 22, 30, 35);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        10, 12, 15, 20, 25, 30);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        -5, -10, -18, -32, -50, -72);
                return originalStatistics
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level));
            }),
            new Reforge("Rich", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 15);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        10, 12, 14, 17, 20, 25);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        1, 2, 4, 7, 10, 15);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        3, 6, 10, 15, 20, 25);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level));
            }),
            new Reforge("Rapid", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 15);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        35, 45, 55, 65, 75, 90);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level));
            }),
            new Reforge("Unreal", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        3, 7, 12, 18, 25, 34);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        8, 9, 10, 11, 13, 15);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        5, 10, 18, 32, 50, 70);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level));
            })
    )),
    ARMOR(List.of(
            new Reforge("Clean", (originalStatistics, level) -> {
                ReforgeValue<Integer> health = new ReforgeValue<>(
                        5, 7, 10, 15, 20, 25);
                ReforgeValue<Integer> defense = new ReforgeValue<>(
                        5, 7, 10, 15, 20, 25);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        2, 4, 6, 8, 10, 12);
                return originalStatistics
                        .addBase(ItemStatistic.HEALTH, (double) health.getForRarity(level))
                        .addBase(ItemStatistic.DEFENSE, (double) defense.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level));
            }),
            new Reforge("Fierce", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        2, 4, 6, 8, 10, 12);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        2, 3, 4, 5, 6, 8);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        4, 7, 10, 14, 18, 24);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level));
            }),
            new Reforge("Heavy", (originalStatistics, level) -> {
                ReforgeValue<Integer> defense = new ReforgeValue<>(
                        25, 35, 50, 65, 80, 110);
                ReforgeValue<Integer> speed = new ReforgeValue<>(
                        -1, -1, -1, -1, -1, -1);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        1, 2, 2, 3, 5, 7);
                return originalStatistics
                        .addBase(ItemStatistic.DEFENSE, (double) defense.getForRarity(level))
                        .addBase(ItemStatistic.SPEED, (double) speed.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level));
            }),
            new Reforge("Light", (originalStatistics, level) -> {
                ReforgeValue<Integer> health = new ReforgeValue<>(
                        5, 7, 10, 15, 20, 25);
                ReforgeValue<Integer> defense = new ReforgeValue<>(
                        1, 2, 3, 4, 5, 6);
                ReforgeValue<Integer> speed = new ReforgeValue<>(
                        1, 2, 3, 4, 5, 6);
                ReforgeValue<Integer> bonusAttackspeed = new ReforgeValue<>(
                        1, 2, 3, 4, 5, 6);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        1, 2, 3, 4, 5, 6);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        1, 1, 2, 2, 3, 3);
                return originalStatistics
                        .addBase(ItemStatistic.HEALTH, (double) health.getForRarity(level))
                        .addBase(ItemStatistic.DEFENSE, (double) defense.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.SPEED, (double) speed.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackspeed.getForRarity(level));
            }),
            new Reforge("Mythic", (originalStatistics, level) -> {
                ReforgeValue<Integer> health = new ReforgeValue<>(
                        2, 4, 6, 8, 10, 12);
                ReforgeValue<Integer> defense = new ReforgeValue<>(
                        2, 4, 6, 8, 10, 12);
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        2, 4, 6, 8, 10, 12);
                ReforgeValue<Integer> speed = new ReforgeValue<>(
                        2, 2, 2, 2, 2, 2);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        1, 2, 3, 4, 5, 6);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        20, 25, 30, 40, 50, 60);
                return originalStatistics
                        .addBase(ItemStatistic.HEALTH, (double) health.getForRarity(level))
                        .addBase(ItemStatistic.DEFENSE, (double) defense.getForRarity(level))
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.SPEED, (double) speed.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level));
            }),
            new Reforge("Pure", (originalStatistics, level) -> {
                ReforgeValue<Integer> health = new ReforgeValue<>(
                        2, 3, 4, 6, 8, 10);
                ReforgeValue<Integer> defense = new ReforgeValue<>(
                        2, 3, 4, 6, 8, 10);
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        2, 3, 4, 6, 8, 10);
                ReforgeValue<Integer> speed = new ReforgeValue<>(
                        1, 1, 1, 1, 1, 1);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        2, 4, 6, 8, 10, 12);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        2, 3, 4, 6, 8, 8);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        1, 1, 2, 3, 4, 5);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        2, 3, 4, 6, 8, 10);
                return originalStatistics
                        .addBase(ItemStatistic.HEALTH, (double) health.getForRarity(level))
                        .addBase(ItemStatistic.DEFENSE, (double) defense.getForRarity(level))
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.SPEED, (double) speed.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level));
            }),
            new Reforge("Smart", (originalStatistics, level) -> {
                ReforgeValue<Integer> health = new ReforgeValue<>(
                        4, 6, 9, 12, 15, 20);
                ReforgeValue<Integer> defense = new ReforgeValue<>(
                        4, 6, 9, 12, 15, 20);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        20, 40, 60, 80, 100, 120);
                return originalStatistics
                        .addBase(ItemStatistic.HEALTH, (double) health.getForRarity(level))
                        .addBase(ItemStatistic.DEFENSE, (double) defense.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level));
            }),
            new Reforge("Titanic", (originalStatistics, level) -> {
                ReforgeValue<Integer> health = new ReforgeValue<>(
                        10, 15, 20, 25, 35, 50);
                ReforgeValue<Integer> defense = new ReforgeValue<>(
                        10, 15, 20, 25, 35, 50);
                return originalStatistics
                        .addBase(ItemStatistic.HEALTH, (double) health.getForRarity(level))
                        .addBase(ItemStatistic.DEFENSE, (double) defense.getForRarity(level));
            }),
            new Reforge("Wise", (originalStatistics, level) -> {
                ReforgeValue<Integer> health = new ReforgeValue<>(
                       6, 8, 10, 12, 15, 20);
                ReforgeValue<Integer> speed = new ReforgeValue<>(
                        1, 1, 1, 2, 2, 3);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        25, 50, 75, 100, 125, 150);
                return originalStatistics
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level))
                        .addBase(ItemStatistic.HEALTH, (double) health.getForRarity(level))
                        .addBase(ItemStatistic.SPEED, (double) speed.getForRarity(level));
            })
    )),
    EQUIPMENT(List.of()),
    FISHING_RODS(List.of(
            new Reforge("Epic", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        15, 20, 25, 32, 40, 50);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        10, 15, 20, 27, 35, 45);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        1, 2, 4, 7, 10, 15);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            }),
            new Reforge("Gentle", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        3, 5, 7, 10, 15, 20);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        8, 10, 15, 20, 25, 30);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            }),
            new Reforge("Odd", (originalStatistics, level) -> {
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        12, 15, 15, 20, 25, 30);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        10, 15, 15, 22, 30, 40);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        -5, -10, -18, -32, -50, -75);
                return originalStatistics
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level));
            }),
            new Reforge("Fair", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            }),
            new Reforge("Fast", (originalStatistics, level) -> {
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        10, 20, 30, 40, 50, 60);
                return originalStatistics
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            }),
            new Reforge("Sharp", (originalStatistics, level) -> {
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        10, 12, 14, 17, 20, 25);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        20, 30, 40, 55, 75, 90);
                return originalStatistics
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level));
            }),
            new Reforge("Heroic", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        15, 20, 25, 32, 40, 50);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        40, 50, 65, 80, 100, 125);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        1, 2, 2, 3, 5, 7);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level));
            }),
            new Reforge("Spicy", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        2, 3, 4, 7, 10, 12);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        1, 1, 1, 1, 1, 1);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        25, 35, 45, 60, 80, 100);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        1, 2, 4, 7, 10, 15);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            }),
            new Reforge("Legendary", (originalStatistics, level) -> {
                ReforgeValue<Integer> strength = new ReforgeValue<>(
                        3, 7, 12, 18, 25, 32);
                ReforgeValue<Integer> critChance = new ReforgeValue<>(
                        5, 7, 9, 12, 15, 18);
                ReforgeValue<Integer> critDamage = new ReforgeValue<>(
                        5, 10, 15, 22, 28, 36);
                ReforgeValue<Integer> intelligence = new ReforgeValue<>(
                        5, 8, 12, 18, 25, 35);
                ReforgeValue<Integer> bonusAttackSpeed = new ReforgeValue<>(
                        2, 3, 5, 7, 10, 15);
                return originalStatistics
                        .addBase(ItemStatistic.STRENGTH, (double) strength.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_CHANCE, (double) critChance.getForRarity(level))
                        .addBase(ItemStatistic.CRIT_DAMAGE, (double) critDamage.getForRarity(level))
                        .addBase(ItemStatistic.INTELLIGENCE, (double) intelligence.getForRarity(level))
                        .addBase(ItemStatistic.BONUS_ATTACK_SPEED, (double) bonusAttackSpeed.getForRarity(level));
            })
    )),
    PICKAXES(List.of(
            new Reforge("Unyielding", (originalStatistics, level) -> {
                return originalStatistics.addBase(ItemStatistic.SPEED, level * 1.15)
                        .addBase(ItemStatistic.MINING_SPEED, level.doubleValue());
            }),
            new Reforge("Excellent", (originalStatistics, level) -> {
                return originalStatistics.addBase(ItemStatistic.SPEED, level * 1.1)
                        .addBase(ItemStatistic.MINING_SPEED, level.doubleValue() * 4);
            })
    )),
    AXES(List.of()),
    HOES(List.of()),
    VACUUMS(List.of()),
    ;

    private final List<Reforge> reforges;

    ReforgeType(List<Reforge> reforges) {
        this.reforges = reforges;
    }

    public record Reforge(String prefix, BiFunction<ItemStatistics, Integer, ItemStatistics> calculation) {
        public ItemStatistics getAfterCalculation(ItemStatistics statistic, Integer level) {
            return calculation.apply(statistic, level);
        }
    }

    private record ReforgeValue<T>(T common, T uncommon, T rare, T epic, T legendary, T mythic) {

        private T getForRarity(Integer rarity) {
            return switch (rarity) {
                case 1 -> common;
                case 2 -> uncommon;
                case 3 -> rare;
                case 4 -> epic;
                case 5 -> legendary;
                case 6, 7, 8 -> mythic;

                default -> throw new IllegalStateException("Unexpected value: " + rarity);
            };
        }
    }
}

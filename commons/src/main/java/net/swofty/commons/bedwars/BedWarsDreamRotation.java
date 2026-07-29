package net.swofty.commons.bedwars;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BedWarsDreamRotation {
    private static final LocalDate EPOCH_WEEK = LocalDate.of(2026, 1, 1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    private static final List<DreamMode> MODES = List.of(
        new DreamMode("One Block", BedWarsGameType.ONE_BLOCK, BedWarsGameType.ONE_BLOCK),
        new DreamMode("Rush V2", BedWarsGameType.RUSH_DOUBLES, BedWarsGameType.RUSH_FOURS),
        new DreamMode("Ultimate V2", BedWarsGameType.ULTIMATE_DOUBLES, BedWarsGameType.ULTIMATE_FOURS),
        new DreamMode("Swappage", BedWarsGameType.SWAPPAGE_DOUBLES, BedWarsGameType.SWAPPAGE_FOURS),
        new DreamMode("Castle V2", BedWarsGameType.CASTLE, BedWarsGameType.CASTLE),
        new DreamMode("Voidless", BedWarsGameType.VOIDLESS_DOUBLES, BedWarsGameType.VOIDLESS_FOURS),
        new DreamMode("Armed", BedWarsGameType.ARMED_DOUBLES, BedWarsGameType.ARMED_FOURS),
        new DreamMode("Lucky V2", BedWarsGameType.LUCKY_BLOCK_DOUBLES, BedWarsGameType.LUCKY_BLOCK_FOURS)
    );

    public static RotationEntry current(LocalDate date) {
        return entryFor(date, 0);
    }

    public static RotationEntry entryFor(LocalDate date, int weekOffset) {
        LocalDate weekStart = weekStart(date).plusWeeks(weekOffset);
        DreamMode mode = modeForWeek(weekStart);
        return new RotationEntry(mode, weekStart, weekStart.plusDays(7), weekOffset);
    }

    public static List<RotationEntry> upcoming(LocalDate date, int count) {
        List<RotationEntry> entries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            entries.add(entryFor(date, i));
        }
        return entries;
    }

    public static List<DreamMode> modes() {
        return MODES;
    }

    private static LocalDate weekStart(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    private static DreamMode modeForWeek(LocalDate weekStart) {
        long week = ChronoUnit.WEEKS.between(EPOCH_WEEK, weekStart);
        List<DreamMode> shuffled = new ArrayList<>(MODES);
        long seed = 0x6A09E667F3BCC909L ^ (week / MODES.size()) * 0x9E3779B97F4A7C15L;
        Collections.shuffle(shuffled, new Random(seed));
        return shuffled.get(Math.floorMod((int) week, shuffled.size()));
    }

    public record DreamMode(String displayName, BedWarsGameType doublesType, BedWarsGameType foursType) {
        public BedWarsGameType preferredQueueType() {
            if (doublesType.isLuckyBlock()) {
                return doublesType;
            }
            return doublesType.getMapCompatibilityType();
        }
    }

    public record RotationEntry(DreamMode mode, LocalDate startsAt, LocalDate endsAt, int weekOffset) {
        public boolean current() {
            return weekOffset == 0;
        }
    }
}

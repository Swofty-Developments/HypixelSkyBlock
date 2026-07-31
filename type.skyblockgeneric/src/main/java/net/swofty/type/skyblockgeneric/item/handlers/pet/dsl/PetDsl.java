package net.swofty.type.skyblockgeneric.item.handlers.pet.dsl;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class PetDsl {
    private PetDsl() {
    }

    public static Builder ability(String name) {
        return new Builder(name);
    }

    public static final class Builder {
        private final String name;
        private Function<SkyBlockItem, List<String>> description;
        private String unimplementedReason;
        private final List<StatisticsFragment> statistics = new ArrayList<>();
        private final List<ConditionalHandler<PetEvent.Kill>> kills = new ArrayList<>();
        private final List<ConditionalHandler<PetEvent.FallDamage>> fallDamages = new ArrayList<>();
        private final List<ConditionalHandler<PetEvent.DamagedByMob>> damagedByMobs = new ArrayList<>();
        private final List<ConditionalHandler<PetEvent.XpGain>> xpGains = new ArrayList<>();
        private final List<ConditionalHandler<PetEvent.Jump>> jumps = new ArrayList<>();
        private final List<ConditionalHandler<PetEvent.PetInteract>> petInteracts = new ArrayList<>();
        private final List<ConditionalHandler<PetEvent.FishCaught>> fishCaughts = new ArrayList<>();
        private final List<ConditionalHandler<PetEvent.CropHarvested>> cropHarvests = new ArrayList<>();

        private Builder(String name) {
            this.name = Objects.requireNonNull(name, "name");
        }

        public Builder description(Function<SkyBlockItem, List<String>> description) {
            this.description = Objects.requireNonNull(description, "description");
            return this;
        }

        public Builder unimplemented(String reason) {
            this.unimplementedReason = Objects.requireNonNull(reason, "reason");
            return this;
        }

        public Builder statistics(Function<PetStatisticsContext, ItemStatistics> action) {
            return statistics(context -> true, action);
        }

        public Builder statistics(Predicate<PetStatisticsContext> condition, Function<PetStatisticsContext, ItemStatistics> action) {
            statistics.add(new StatisticsFragment(
                    Objects.requireNonNull(condition, "condition"),
                    Objects.requireNonNull(action, "action")
            ));
            return this;
        }

        public Builder onKill(Consumer<PetEvent.Kill> action) {
            return onKill(_ -> true, action);
        }

        public Builder onKill(Predicate<PetEvent.Kill> condition, Consumer<PetEvent.Kill> action) {
            kills.add(ConditionalHandler.of(condition, action));
            return this;
        }

        public Builder onFallDamage(Consumer<PetEvent.FallDamage> action) {
            return onFallDamage(_ -> true, action);
        }

        public Builder onFallDamage(Predicate<PetEvent.FallDamage> condition, Consumer<PetEvent.FallDamage> action) {
            fallDamages.add(ConditionalHandler.of(condition, action));
            return this;
        }

        public Builder onDamagedByMob(Consumer<PetEvent.DamagedByMob> action) {
            return onDamagedByMob(_ -> true, action);
        }

        public Builder onDamagedByMob(Predicate<PetEvent.DamagedByMob> condition, Consumer<PetEvent.DamagedByMob> action) {
            damagedByMobs.add(ConditionalHandler.of(condition, action));
            return this;
        }

        public Builder onXpGain(Consumer<PetEvent.XpGain> action) {
            return onXpGain(_ -> true, action);
        }

        public Builder onXpGain(Predicate<PetEvent.XpGain> condition, Consumer<PetEvent.XpGain> action) {
            xpGains.add(ConditionalHandler.of(condition, action));
            return this;
        }

        public Builder onJump(Consumer<PetEvent.Jump> action) {
            return onJump(_ -> true, action);
        }

        public Builder onJump(Predicate<PetEvent.Jump> condition, Consumer<PetEvent.Jump> action) {
            jumps.add(ConditionalHandler.of(condition, action));
            return this;
        }

        public Builder onPetInteract(Consumer<PetEvent.PetInteract> action) {
            return onPetInteract(_ -> true, action);
        }

        public Builder onPetInteract(Predicate<PetEvent.PetInteract> condition, Consumer<PetEvent.PetInteract> action) {
            petInteracts.add(ConditionalHandler.of(condition, action));
            return this;
        }

        public Builder onFishCaught(Consumer<PetEvent.FishCaught> action) {
            return onFishCaught(_ -> true, action);
        }

        public Builder onFishCaught(Predicate<PetEvent.FishCaught> condition, Consumer<PetEvent.FishCaught> action) {
            fishCaughts.add(ConditionalHandler.of(condition, action));
            return this;
        }

        public Builder onCropHarvested(Consumer<PetEvent.CropHarvested> action) {
            return onCropHarvested(_ -> true, action);
        }

        public Builder onCropHarvested(Predicate<PetEvent.CropHarvested> condition, Consumer<PetEvent.CropHarvested> action) {
            cropHarvests.add(ConditionalHandler.of(condition, action));
            return this;
        }

        public PetAbility build() {
            return new BuiltAbility(
                    name,
                    Objects.requireNonNull(description, "description must be set"),
                    List.copyOf(statistics),
                    List.copyOf(kills),
                    List.copyOf(fallDamages),
                    List.copyOf(damagedByMobs),
                    List.copyOf(xpGains),
                    List.copyOf(jumps),
                    List.copyOf(petInteracts),
                    List.copyOf(fishCaughts),
                    List.copyOf(cropHarvests),
                    unimplementedReason
            );
        }
    }

    private record BuiltAbility(
            String name,
            Function<SkyBlockItem, List<String>> description,
            List<StatisticsFragment> statistics,
            List<ConditionalHandler<PetEvent.Kill>> kills,
            List<ConditionalHandler<PetEvent.FallDamage>> fallDamages,
            List<ConditionalHandler<PetEvent.DamagedByMob>> damagedByMobs,
            List<ConditionalHandler<PetEvent.XpGain>> xpGains,
            List<ConditionalHandler<PetEvent.Jump>> jumps,
            List<ConditionalHandler<PetEvent.PetInteract>> petInteracts,
            List<ConditionalHandler<PetEvent.FishCaught>> fishCaughts,
            List<ConditionalHandler<PetEvent.CropHarvested>> cropHarvests,
            @Nullable String unimplementedReason
    ) implements PetAbility {
        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<String> getDescription(SkyBlockItem pet) {
            List<String> base = description.apply(pet);
            if (unimplementedReason == null) return base;

            List<String> lines = new ArrayList<>(base);
            lines.add("");
            lines.add("§c⚠ §lNOT IMPLEMENTED§r§c — " + unimplementedReason);
            return List.copyOf(lines);
        }

        @Override
        public ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
            return statisticsFor(new PetStatisticsContext(player, pet), statistics);
        }

        @Override
        public void onEvent(PetEvent event) {
            switch (event) {
                case PetEvent.Kill kill -> runConditional(kills, kill);
                case PetEvent.FallDamage fallDamage -> runConditional(fallDamages, fallDamage);
                case PetEvent.DamagedByMob damagedByMob -> runConditional(damagedByMobs, damagedByMob);
                case PetEvent.XpGain xpGain -> runConditional(xpGains, xpGain);
                case PetEvent.Jump jump -> runConditional(jumps, jump);
                case PetEvent.PetInteract petInteract -> runConditional(petInteracts, petInteract);
                case PetEvent.FishCaught fishCaught -> runConditional(fishCaughts, fishCaught);
                case PetEvent.CropHarvested cropHarvested -> runConditional(cropHarvests, cropHarvested);
            }
        }
    }

    private static ItemStatistics statisticsFor(PetStatisticsContext context, List<StatisticsFragment> statistics) {
        ItemStatistics result = null;
        for (StatisticsFragment fragment : statistics) {
            if (!fragment.condition().test(context)) continue;

            ItemStatistics fragmentStatistics = fragment.action().apply(context);
            if (fragmentStatistics == null) continue;
            result = result == null ? fragmentStatistics : ItemStatistics.add(result, fragmentStatistics);
        }
        return result == null ? ItemStatistics.empty() : result;
    }

    private static <E> void runConditional(List<ConditionalHandler<E>> handlers, E event) {
        for (ConditionalHandler<E> handler : handlers) {
            if (handler.condition().test(event)) handler.action().accept(event);
        }
    }

    private record StatisticsFragment(Predicate<PetStatisticsContext> condition,
            Function<PetStatisticsContext, ItemStatistics> action) {
    }

    private record ConditionalHandler<E>(Predicate<E> condition, Consumer<E> action) {
        private static <E> ConditionalHandler<E> of(Predicate<E> condition, Consumer<E> action) {
            return new ConditionalHandler<>(
                    Objects.requireNonNull(condition, "condition"),
                    Objects.requireNonNull(action, "action")
            );
        }
    }
}

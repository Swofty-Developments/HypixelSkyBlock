package net.swofty.type.skyblockgeneric.item.handlers.pet.dsl;

import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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
        private final Map<Class<?>, List<ConditionalHandler<?>>> eventHandlers = new HashMap<>();

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
            statistics.add(StatisticsFragment.plain(condition, action));
            return this;
        }

        public Builder statistics(BiFunction<AbilityRuntime, PetStatisticsContext, ItemStatistics> action) {
            return statistics(_ -> true, action);
        }

        public Builder statistics(Predicate<PetStatisticsContext> condition, BiFunction<AbilityRuntime, PetStatisticsContext, ItemStatistics> action) {
            statistics.add(StatisticsFragment.stateful(condition, action));
            return this;
        }

        public <E extends PetEvent> Builder on(Class<E> type, Consumer<E> action) {
            return on(type, _ -> true, action);
        }

        public <E extends PetEvent> Builder on(Class<E> type, Predicate<E> condition, Consumer<E> action) {
            eventHandlers.computeIfAbsent(type, _ -> new ArrayList<>())
                    .add(ConditionalHandler.stateless(condition, action));
            return this;
        }

        public <E extends PetEvent> Builder on(Class<E> type, BiConsumer<AbilityRuntime, E> action) {
            return on(type, _ -> true, action);
        }

        public <E extends PetEvent> Builder on(Class<E> type, Predicate<E> condition, BiConsumer<AbilityRuntime, E> action) {
            eventHandlers.computeIfAbsent(type, _ -> new ArrayList<>())
                    .add(ConditionalHandler.stateful(condition, action));
            return this;
        }

        public PetAbility build() {
            Map<Class<?>, List<ConditionalHandler<?>>> handlers = new HashMap<>();
            eventHandlers.forEach((type, list) -> handlers.put(type, List.copyOf(list)));

            return new BuiltAbility(
                    name,
                    Objects.requireNonNull(description, "description must be set"),
                    List.copyOf(statistics),
                    handlers,
                    unimplementedReason
            );
        }
    }

    private record BuiltAbility(
            String name,
            Function<SkyBlockItem, List<String>> description,
            List<StatisticsFragment> statistics,
            Map<Class<?>, List<ConditionalHandler<?>>> eventHandlers,
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
            return statisticsFor(this, new PetStatisticsContext(player, pet), statistics);
        }

        @Override
        public void onEvent(PetEvent event) {
            List<ConditionalHandler<?>> handlers = eventHandlers.get(event.getClass());
            if (handlers == null) return;

            for (ConditionalHandler<?> raw : handlers) {
                ConditionalHandler<PetEvent> handler = cast(raw);
                if (!handler.condition().test(event)) continue;

                if (handler.runtimeAction() == null) {
                    handler.action().accept(event);
                } else {
                    AbilityRuntime runtime = event.player().getPetData().getAbilityRuntime(this);
                    handler.runtimeAction().accept(runtime, event);
                }
            }
        }

        @SuppressWarnings("unchecked")
        private static ConditionalHandler<PetEvent> cast(ConditionalHandler<?> handler) {
            return (ConditionalHandler<PetEvent>) handler;
        }
    }

    private static ItemStatistics statisticsFor(PetAbility ability, PetStatisticsContext context, List<StatisticsFragment> statistics) {
        ItemStatistics result = null;
        for (StatisticsFragment fragment : statistics) {
            if (!fragment.condition().test(context)) continue;

            ItemStatistics fragmentStatistics;
            if (fragment.runtimeAction() == null) {
                fragmentStatistics = fragment.action().apply(context);
            } else {
                AbilityRuntime runtime = context.player().getPetData().getAbilityRuntime(ability);
                fragmentStatistics = fragment.runtimeAction().apply(runtime, context);
            }
            if (fragmentStatistics == null) continue;
            result = result == null ? fragmentStatistics : ItemStatistics.add(result, fragmentStatistics);
        }
        return result == null ? ItemStatistics.empty() : result;
    }

    private record StatisticsFragment(
            Predicate<PetStatisticsContext> condition,
            @Nullable Function<PetStatisticsContext, ItemStatistics> action,
            @Nullable BiFunction<AbilityRuntime, PetStatisticsContext, ItemStatistics> runtimeAction) {

        private static StatisticsFragment plain(Predicate<PetStatisticsContext> condition, Function<PetStatisticsContext, ItemStatistics> action) {
            return new StatisticsFragment(
                    Objects.requireNonNull(condition, "condition"),
                    Objects.requireNonNull(action, "action"),
                    null
            );
        }

        private static StatisticsFragment stateful(Predicate<PetStatisticsContext> condition, BiFunction<AbilityRuntime, PetStatisticsContext, ItemStatistics> action) {
            return new StatisticsFragment(
                    Objects.requireNonNull(condition, "condition"),
                    null,
                    Objects.requireNonNull(action, "action")
            );
        }
    }

    private record ConditionalHandler<E extends PetEvent>(
            Predicate<E> condition,
            @Nullable Consumer<E> action,
            @Nullable BiConsumer<AbilityRuntime, E> runtimeAction) {

        private static <E extends PetEvent> ConditionalHandler<E> stateless(Predicate<E> condition, Consumer<E> action) {
            return new ConditionalHandler<>(
                    Objects.requireNonNull(condition, "condition"),
                    Objects.requireNonNull(action, "action"),
                    null
            );
        }

        private static <E extends PetEvent> ConditionalHandler<E> stateful(Predicate<E> condition, BiConsumer<AbilityRuntime, E> action) {
            return new ConditionalHandler<>(
                    Objects.requireNonNull(condition, "condition"),
                    null,
                    Objects.requireNonNull(action, "action")
            );
        }
    }
}

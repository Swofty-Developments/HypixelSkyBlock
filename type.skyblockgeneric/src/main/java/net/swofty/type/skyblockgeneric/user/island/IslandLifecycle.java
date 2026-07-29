package net.swofty.type.skyblockgeneric.user.island;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.tinylog.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IslandLifecycle {
    private static final List<IslandLifecycleStep> steps = new CopyOnWriteArrayList<>();

    public static void register(IslandLifecycleStep step) {
        steps.add(step);
        steps.sort(Comparator.comparing(IslandLifecycleStep::phase).thenComparingInt(IslandLifecycleStep::order));
    }

    public static void run(IslandLifecyclePhase phase, IslandLifecycleContext context) {
        steps.stream()
                .filter(step -> step.phase() == phase)
                .forEach(step -> runStep(step, context));
    }

    private static void runStep(IslandLifecycleStep step, IslandLifecycleContext context) {
        long started = System.currentTimeMillis();
        Logger.info("[{}] Starting island {} step {}", context.island().getIslandID(), step.phase(), step.getClass().getSimpleName());

        try {
            step.run(context);
            Logger.info("[{}] Completed island {} step {} in {}ms",
                    context.island().getIslandID(),
                    step.phase(),
                    step.getClass().getSimpleName(),
                    System.currentTimeMillis() - started);
        } catch (Throwable throwable) {
            Logger.error(throwable, "[{}] Failed island {} step {} after {}ms",
                    context.island().getIslandID(),
                    step.phase(),
                    step.getClass().getSimpleName(),
                    System.currentTimeMillis() - started);
            if (throwable instanceof RuntimeException runtimeException) throw runtimeException;
            if (throwable instanceof Error error) throw error;
            throw new RuntimeException(throwable);
        }
    }
}

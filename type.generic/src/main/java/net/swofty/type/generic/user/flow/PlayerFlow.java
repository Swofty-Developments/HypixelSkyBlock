package net.swofty.type.generic.user.flow;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerFlow {

    public static void run(HypixelPlayer player, String stage, Runnable action) {
        long started = System.currentTimeMillis();
        Logger.info("[{}] Starting {} for {}", player.getUuid(), stage, player.getUsername());

        try {
            action.run();
            Logger.info("[{}] Completed {} for {} in {}ms",
                    player.getUuid(),
                    stage,
                    player.getUsername(),
                    System.currentTimeMillis() - started);
        } catch (Throwable throwable) {
            Logger.error(throwable, "[{}] Failed {} for {} after {}ms",
                    player.getUuid(),
                    stage,
                    player.getUsername(),
                    System.currentTimeMillis() - started);
            if (throwable instanceof RuntimeException runtimeException) throw runtimeException;
            if (throwable instanceof Error error) throw error;
            throw new RuntimeException(throwable);
        }
    }
}

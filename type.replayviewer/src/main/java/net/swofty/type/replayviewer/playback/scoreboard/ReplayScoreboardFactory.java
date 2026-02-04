package net.swofty.type.replayviewer.playback.scoreboard;

import net.swofty.commons.ServerType;
import net.swofty.type.replayviewer.playback.ReplaySession;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ReplayScoreboardFactory {

    private static final Map<ServerType, Function<ReplaySession, ReplayScoreboard>> FACTORIES = new HashMap<>();

    static {
        registerFactory(ServerType.BEDWARS_GAME, BedWarsReplayScoreboard::new);
    }

    /**
     * Registers a scoreboard factory for a game mode.
     *
     * @param gameMode the game mode identifier
     * @param factory  the factory function to create scoreboards
     */
    public static void registerFactory(ServerType gameMode, Function<ReplaySession, ReplayScoreboard> factory) {
        FACTORIES.put(gameMode, factory);
    }

    /**
     * Creates a scoreboard for the given replay session.
     *
     * @param session the replay session
     * @return the appropriate scoreboard implementation
     */
    public static ReplayScoreboard create(ReplaySession session) {
        ServerType gameMode = session.getMetadata().getServerType();
        Function<ReplaySession, ReplayScoreboard> factory = FACTORIES.get(gameMode);
        if (factory != null) {
            return factory.apply(session);
        }

        return new GenericReplayScoreboard(session);
    }

}

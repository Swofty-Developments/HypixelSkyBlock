package net.swofty.type.replayviewer.playback.scoreboard;

import net.minestom.server.entity.Player;
import net.swofty.type.replayviewer.playback.ReplaySession;

import java.util.List;

public interface ReplayScoreboard {

    /**
     * Creates the scoreboard for a viewer.
     *
     * @param viewer the player viewing the replay
     */
    void create(Player viewer);

    /**
     * Updates the scoreboard with current replay state.
     *
     * @param session the replay session containing current state
     */
    void update(ReplaySession session);

    /**
     * Removes the scoreboard from a viewer.
     *
     * @param viewer the player to remove the scoreboard from
     */
    void remove(Player viewer);

    /**
     * Gets the title for the scoreboard.
     *
     * @return the scoreboard title
     */
    String getTitle();

    /**
     * Gets the current lines to display.
     *
     * @param session the replay session
     * @return list of lines for the scoreboard
     */
    List<String> getLines(ReplaySession session);

    /**
     * Called when a game-specific event occurs during replay.
     * Implementations can use this to update cached state.
     *
     * @param eventType the type of event
     * @param data      event-specific data
     */
    default void onGameEvent(String eventType, Object data) {

    }
}

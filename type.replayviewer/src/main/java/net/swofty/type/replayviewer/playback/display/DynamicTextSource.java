package net.swofty.type.replayviewer.playback.display;

import java.util.List;

public interface DynamicTextSource {

    /**
     * Gets the unique identifier for this text source.
     *
     * @return the source identifier
     */
    String getIdentifier();

    /**
     * Gets the display type category (e.g., "generator", "hologram", "countdown").
     *
     * @return the display type
     */
    String getDisplayType();

    /**
     * Gets the current text lines for the given tick.
     *
     * @param currentTick the current replay tick
     * @return the text lines to display
     */
    List<String> getTextAt(int currentTick);

    /**
     * Checks if the text has changed since the last tick.
     *
     * @param lastTick    the previous tick checked
     * @param currentTick the current tick
     * @return true if text has changed
     */
    default boolean hasChangedSince(int lastTick, int currentTick) {
        return !getTextAt(lastTick).equals(getTextAt(currentTick));
    }

    /**
     * Gets the initial text lines.
     *
     * @return the initial text to display
     */
    default List<String> getInitialText() {
        return getTextAt(0);
    }

    /**
     * Checks if this source is still active at the given tick.
     *
     * @param currentTick the current tick
     * @return true if the source should still display
     */
    default boolean isActiveAt(int currentTick) {
        return true;
    }
}

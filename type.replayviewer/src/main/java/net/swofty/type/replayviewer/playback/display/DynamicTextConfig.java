package net.swofty.type.replayviewer.playback.display;

import java.util.List;
import java.util.Map;

/**
 * Configuration for creating a dynamic text source.
 *
 * @param identifier      unique identifier for this text source
 * @param displayType     the type of display (e.g., "generator", "countdown")
 * @param initialText     the initial text lines
 * @param metadata        additional type-specific configuration
 */
public record DynamicTextConfig(
    String identifier,
    String displayType,
    List<String> initialText,
    Map<String, Object> metadata
) {
    /**
     * Gets a metadata value.
     *
     * @param key the metadata key
     * @param <T> the expected type
     * @return the value, or null if not present
     */
    @SuppressWarnings("unchecked")
    public <T> T getMeta(String key) {
        return metadata != null ? (T) metadata.get(key) : null;
    }

    /**
     * Gets a metadata value with a default.
     *
     * @param key          the metadata key
     * @param defaultValue the default value if not present
     * @param <T>          the expected type
     * @return the value, or the default
     */
    @SuppressWarnings("unchecked")
    public <T> T getMeta(String key, T defaultValue) {
        if (metadata == null) return defaultValue;
        T value = (T) metadata.get(key);
        return value != null ? value : defaultValue;
    }
}

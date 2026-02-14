package net.swofty.type.replayviewer.playback.npc;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NpcReplayData {
    private final int entityId;

    private String displayName;
    private String namePrefix;
    private String nameSuffix;
    private int nameColor;
    private boolean nameVisible;

    private List<String> textLines;
    private double textYOffset;
    private int textDisplayDurationTicks;

    private int textEntityId = -1;

    public NpcReplayData(int entityId) {
        this.entityId = entityId;
        this.displayName = "";
        this.namePrefix = "";
        this.nameSuffix = "";
        this.nameColor = 0xFFFFFF;
        this.nameVisible = true;
        this.textLines = new ArrayList<>();
        this.textYOffset = 2.5;
        this.textDisplayDurationTicks = 0;
    }

    /**
     * Gets the full formatted name including prefix and suffix.
     *
     * @return the full display name
     */
    public String getFullDisplayName() {
        return namePrefix + displayName + nameSuffix;
    }

    /**
     * Updates the display name.
     *
     * @param displayName the new display name
     * @param prefix      the prefix
     * @param suffix      the suffix
     * @param nameColor   the name color (RGB)
     * @param visible     whether the name is visible
     */
    public void updateDisplayName(String displayName, String prefix, String suffix,
                                   int nameColor, boolean visible) {
        this.displayName = displayName != null ? displayName : "";
        this.namePrefix = prefix != null ? prefix : "";
        this.nameSuffix = suffix != null ? suffix : "";
        this.nameColor = nameColor;
        this.nameVisible = visible;
    }

    /**
     * Updates the text lines.
     *
     * @param textLines the new text lines
     * @param yOffset   the vertical offset
     * @param duration  the display duration in ticks (0 = indefinite)
     */
    public void updateTextLines(List<String> textLines, double yOffset, int duration) {
        this.textLines = textLines != null ? new ArrayList<>(textLines) : new ArrayList<>();
        this.textYOffset = yOffset;
        this.textDisplayDurationTicks = duration;
    }

    /**
     * Checks if this NPC has text lines to display.
     *
     * @return true if there are text lines
     */
    public boolean hasTextLines() {
        return textLines != null && !textLines.isEmpty();
    }

    /**
     * Creates a copy of this NPC data.
     *
     * @return a new NpcReplayData with the same values
     */
    public NpcReplayData copy() {
        NpcReplayData copy = new NpcReplayData(entityId);
        copy.displayName = this.displayName;
        copy.namePrefix = this.namePrefix;
        copy.nameSuffix = this.nameSuffix;
        copy.nameColor = this.nameColor;
        copy.nameVisible = this.nameVisible;
        copy.textLines = new ArrayList<>(this.textLines);
        copy.textYOffset = this.textYOffset;
        copy.textDisplayDurationTicks = this.textDisplayDurationTicks;
        copy.textEntityId = this.textEntityId;
        return copy;
    }
}

package net.swofty.type.replayviewer.playback.display;

import java.util.List;

public class StaticTextSource implements DynamicTextSource {

    private final String identifier;
    private final String displayType;
    private final List<String> textLines;

    public StaticTextSource(DynamicTextConfig config) {
        this.identifier = config.identifier();
        this.displayType = config.displayType();
        this.textLines = config.initialText();
    }

    public StaticTextSource(String identifier, List<String> textLines) {
        this.identifier = identifier;
        this.displayType = "static";
        this.textLines = textLines;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDisplayType() {
        return displayType;
    }

    @Override
    public List<String> getTextAt(int currentTick) {
        return textLines;
    }

    @Override
    public boolean hasChangedSince(int lastTick, int currentTick) {
        return false; // Static text never changes
    }
}

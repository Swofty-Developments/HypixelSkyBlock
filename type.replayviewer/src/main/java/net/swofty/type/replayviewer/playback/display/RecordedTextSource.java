package net.swofty.type.replayviewer.playback.display;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class RecordedTextSource implements DynamicTextSource {

    private final String identifier;
    private final String displayType;
    private final TreeMap<Integer, List<String>> updatesByTick = new TreeMap<>();

    public RecordedTextSource(String identifier, String displayType, List<String> initialText, int startTick) {
        this.identifier = identifier;
        this.displayType = displayType;
        List<String> snapshot = copyLines(initialText);
        updatesByTick.put(startTick, snapshot);
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
        var entry = updatesByTick.floorEntry(currentTick);
        if (entry != null) {
            return entry.getValue();
        }
        return List.of();
    }

    @Override
    public boolean hasChangedSince(int lastTick, int currentTick) {
        Integer next = updatesByTick.higherKey(lastTick);
        return next != null && next <= currentTick;
    }

    public void recordUpdate(int tick, List<String> newTextLines, boolean replaceAll, int startIndex) {
        List<String> current = copyLines(getTextAt(tick));
        List<String> next;

        if (replaceAll) {
            next = copyLines(newTextLines);
        } else {
            next = current;
            int requiredSize = startIndex + (newTextLines != null ? newTextLines.size() : 0);
            while (next.size() < requiredSize) {
                next.add("");
            }
            if (newTextLines != null) {
                for (int i = 0; i < newTextLines.size(); i++) {
                    next.set(startIndex + i, newTextLines.get(i));
                }
            }
        }

        updatesByTick.put(tick, next);
    }

    private List<String> copyLines(List<String> lines) {
        return lines != null ? new ArrayList<>(lines) : new ArrayList<>();
    }
}

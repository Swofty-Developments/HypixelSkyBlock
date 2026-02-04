package net.swofty.type.replayviewer.playback.display;

import java.util.ArrayList;
import java.util.List;

public class CountdownTextSource implements DynamicTextSource {

    private final String identifier;
    private final int startTick;
    private final int endTick;
    private final String eventName;
    private final List<String> completedText;

    public CountdownTextSource(DynamicTextConfig config) {
        this.identifier = config.identifier();
        this.startTick = config.getMeta("startTick", 0);
        this.endTick = config.getMeta("endTick", 0);
        this.eventName = config.getMeta("eventName", "Event");
        this.completedText = config.getMeta("completedText", List.of("§aCompleted!"));
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getDisplayType() {
        return "countdown";
    }

    @Override
    public List<String> getTextAt(int currentTick) {
        if (currentTick >= endTick) {
            return completedText;
        }

        int remainingTicks = endTick - currentTick;
        int remainingSeconds = remainingTicks / 20;

        List<String> result = new ArrayList<>();
        result.add("§e" + eventName);
        result.add("§7in §f" + formatTime(remainingSeconds));

        return result;
    }

    @Override
    public boolean hasChangedSince(int lastTick, int currentTick) {
        // Changes every second
        int lastSecond = lastTick / 20;
        int currentSecond = currentTick / 20;
        return lastSecond != currentSecond;
    }

    @Override
    public boolean isActiveAt(int currentTick) {
        return currentTick >= startTick;
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}

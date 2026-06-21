package net.swofty.type.replayviewer.entity;

import lombok.Getter;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ReplayNpcTextEntity extends Entity {
    private final int parentEntityId;
    private final double yOffset;
    private List<String> textLines;
    private int remainingDurationTicks;

    public ReplayNpcTextEntity(int parentEntityId, List<String> textLines,
                                double yOffset, int displayDurationTicks) {
        super(EntityType.TEXT_DISPLAY);
        this.parentEntityId = parentEntityId;
        this.yOffset = yOffset;
        this.textLines = textLines != null ? new ArrayList<>(textLines) : new ArrayList<>();
        this.remainingDurationTicks = displayDurationTicks;

        // Configure entity
        setNoGravity(true);
        setGlowing(true);

        // Set initial text
        updateTextDisplay();
    }

    /**
     * Updates the text lines.
     *
     * @param newLines the new text to display
     */
    public void updateTextLines(List<String> newLines) {
        this.textLines = newLines != null ? new ArrayList<>(newLines) : new ArrayList<>();
        updateTextDisplay();
    }

    private void updateTextDisplay() {
        if (this.entityMeta instanceof TextDisplayMeta textMeta) {
            String combinedText = String.join("\n", textLines);
            textMeta.setText(LegacyComponentSerializer.legacySection().deserialize(combinedText));
            textMeta.setBillboardRenderConstraints(AbstractDisplayMeta.BillboardConstraints.CENTER);
            textMeta.setSeeThrough(true);
            textMeta.setBackgroundColor(0);
        }
    }

    /**
     * Updates position based on parent NPC position.
     *
     * @param parentPos the parent NPC's position
     */
    public void updatePositionFromParent(Pos parentPos) {
        double lineOffset = Math.max(0, textLines.size() - 1) * 0.15;
        Pos textPos = parentPos.add(0, yOffset + lineOffset, 0);
        teleport(textPos);
    }

    /**
     * Ticks the display duration.
     *
     * @return true if the text should be removed (duration expired)
     */
    public boolean tickDuration() {
        if (remainingDurationTicks <= 0) {
            return false; // Indefinite display
        }
        remainingDurationTicks--;
        return remainingDurationTicks <= 0;
    }

    /**
     * Checks if this text has a limited display duration.
     *
     * @return true if the text will expire
     */
    public boolean hasExpiration() {
        return remainingDurationTicks > 0;
    }

    @Override
    public void updateNewViewer(@NotNull Player player) {
        super.updateNewViewer(player);
        updateTextDisplay();
    }
}

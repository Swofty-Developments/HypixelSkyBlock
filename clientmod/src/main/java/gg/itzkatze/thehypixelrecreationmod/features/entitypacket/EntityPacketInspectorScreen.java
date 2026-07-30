package gg.itzkatze.thehypixelrecreationmod.features.entitypacket;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class EntityPacketInspectorScreen extends Screen {
    private int scroll;

    public EntityPacketInspectorScreen() {
        super(Component.literal("Entity Packet Inspector"));
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(
                Component.literal(EntityPacketInspector.recording() ? "Pause" : "Resume"),
                button -> {
                    EntityPacketInspector.toggleRecording();
                    button.setMessage(Component.literal(EntityPacketInspector.recording() ? "Pause" : "Resume"));
                }
        ).bounds(10, height - 30, 70, 20).build());
        addRenderableWidget(Button.builder(
                Component.literal("Clear"),
                button -> EntityPacketInspector.clearEntries()
        ).bounds(85, height - 30, 70, 20).build());
        addRenderableWidget(Button.builder(
                Component.literal("Deselect"),
                button -> EntityPacketInspector.clearSelection()
        ).bounds(160, height - 30, 80, 20).build());
        addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> onClose()
        ).bounds(width - 80, height - 30, 70, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(font, title, width / 2, 10, 0xFFFFFF);

        var selected = EntityPacketInspector.selectedEntity();
        String selection = selected == null
                ? "No entity selected. Close this screen, look at one, then press the inspector key."
                : selected.getName().getString() + " (" + selected.getType() + "), associated IDs: "
                + EntityPacketInspector.entityIds();
        graphics.text(font, selection, 10, 28, 0xB8D8FF);

        var entries = EntityPacketInspector.entries();
        int y = 48;
        int start = Math.min(scroll, Math.max(0, entries.size() - 1));
        int visibleRows = Math.max(1, (height - 88) / 22);
        for (int i = start; i < entries.size() && i < start + visibleRows; i++) {
            var entry = entries.get(i);
            graphics.text(font, entry.time() + "  " + entry.type(), 10, y, 0xFFFFFF);
            graphics.text(font, trim(entry.details(), Math.max(20, (width - 20) / 6)), 18, y + 10, 0xA0A0A0);
            y += 22;
        }
        graphics.text(font, entries.size() + " matching inbound packets", 250, height - 24, 0xA0A0A0);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int maxScroll = Math.max(0, EntityPacketInspector.entries().size() - 1);
        scroll = Math.max(0, Math.min(maxScroll, scroll - (int) Math.signum(verticalAmount)));
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static String trim(String value, int maxLength) {
        return value.length() <= maxLength ? value : value.substring(0, maxLength - 1) + "…";
    }
}

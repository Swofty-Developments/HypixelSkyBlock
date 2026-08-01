package net.swofty.type.ravengardgeneric.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.inventory.InventoryType;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;

public abstract class RavengardView extends StatelessView {
    private static final int PANEL_GLYPH = 0xF00F;
    private static final int PANEL_ICON = 0xE237;
    private static final int PANEL_LEAD_SPACE = -8192;
    private static final int PANEL_TRAIL_SPACE = -13;
    private static final TextColor TITLE_COLOR = TextColor.color(0xFEFD1A);

    protected abstract String title();

    protected InventoryType inventoryType() {
        return InventoryType.CHEST_6_ROW;
    }

    /** Glyph shown in the panel header; each menu has its own. */
    protected int panelIcon() {
        return PANEL_ICON;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(chrome(title()), inventoryType());
    }

    /**
     * Places a button at its origin slot and repeats it across the rest of its footprint, so a
     * multi-slot sprite reads as one button and shares a single hover glow.
     */
    protected void place(ViewLayout<DefaultState> layout, int originSlot, RavengardItems.Builder button) {
        button.origin(originSlot);
        for (int slot : button.sprite().coveredSlots(originSlot)) {
            layout.slot(slot, button.toBuilder());
        }
    }

    protected Component chrome(String title) {
        return Component.text(RavengardFont.glyph(PANEL_GLYPH))
                .append(Component.text(RavengardFont.space(PANEL_LEAD_SPACE)))
                .append(Component.text(RavengardFont.glyph(panelIcon())).color(NamedTextColor.WHITE))
                .append(Component.text(RavengardFont.space(PANEL_TRAIL_SPACE)))
                .append(Component.text(title).font(RavengardFont.HALF))
                .append(Component.text(title).color(TITLE_COLOR).font(RavengardFont.DEFAULT));
    }
}

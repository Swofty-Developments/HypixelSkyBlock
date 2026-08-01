package net.swofty.type.ravengardgeneric.gui;

import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.ravengardgeneric.classes.RavengardClass;
import net.swofty.type.ravengardgeneric.classes.RavengardSelection;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

public class GUISelectClass extends RavengardView {
    private static final int PANEL_ICON = 0xF238;

    /**
     * Origin slot and captured offset per class. The offsets are taken verbatim from the capture
     * because Hypixel authors them per menu -- these statues all share one vertical position (green
     * 73) even though their click slots are on different rows, so the slot grid does not predict them.
     */
    private enum Option {
        KNIGHT(RavengardClass.KNIGHT, RavengardButton.STATUE_KNIGHT, 10, 0x3D4900),
        WARRIOR(RavengardClass.WARRIOR, RavengardButton.STATUE_WARRIOR, 12, 0x604900),
        HUNTER(RavengardClass.HUNTER, RavengardButton.STATUE_HUNTER, 14, 0x844900),
        ASSASSIN(RavengardClass.ASSASSIN, RavengardButton.STATUE_ASSASSIN, 16, 0xA84900);

        private final RavengardClass value;
        private final RavengardButton statue;
        private final int slot;
        private final int offset;

        Option(RavengardClass value, RavengardButton statue, int slot, int offset) {
            this.value = value;
            this.statue = statue;
            this.slot = slot;
            this.offset = offset;
        }
    }

    @Override
    protected String title() {
        return "Select Class";
    }

    @Override
    protected int panelIcon() {
        return PANEL_ICON;
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        for (Option option : Option.values()) {
            RavengardItems.Builder button = RavengardItems.button(option.statue)
                    .hoverColor(option.offset)
                    .label("§a" + option.value.getDisplayName());

            String[] profileLore = option.value.profileLore();
            if (profileLore != null) {
                button.blankLine().lore(profileLore);
            }

            for (int slot : option.statue.coveredSlots(option.slot)) {
                layout.slot(slot, button.toBuilder(), (click, viewContext) -> {
                    if (click.player() instanceof RavengardPlayer player) {
                        RavengardSelection.choose(player, option.value);
                    }
                });
            }
        }
    }
}

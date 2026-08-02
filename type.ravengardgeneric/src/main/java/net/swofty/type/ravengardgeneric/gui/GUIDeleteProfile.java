package net.swofty.type.ravengardgeneric.gui;

import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.ViewNavigator;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.ravengardgeneric.profile.RavengardProfiles;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

import java.util.UUID;

public class GUIDeleteProfile extends RavengardView {
    private static final int PANEL_ICON = 0xF001;
    private static final int SLOT_CONFIRM = 28;
    private static final int SLOT_CANCEL = 32;
    private static final int SLOT_BACK = 45;

    private final UUID profileId;

    public GUIDeleteProfile(UUID profileId) {
        this.profileId = profileId;
    }

    @Override
    protected String title() {
        return "Delete profile?";
    }

    @Override
    protected int panelIcon() {
        return PANEL_ICON;
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        RavengardItems.Builder confirm = RavengardItems.button(RavengardButton.TEXT_CONFIRM)
                .label("§aConfirm")
                .blankLine()
                .lore("§eClick to confirm this change!")
                .origin(SLOT_CONFIRM);
        for (int slot : RavengardButton.TEXT_CONFIRM.coveredSlots(SLOT_CONFIRM)) {
            layout.slot(slot, confirm.toBuilder(), (click, viewContext) -> {
                if (viewContext.player() instanceof RavengardPlayer player) {
                    player.closeInventory();
                    RavengardProfiles.delete(player, profileId);
                }
            });
        }

        RavengardItems.Builder cancel = RavengardItems.button(RavengardButton.TEXT_CANCEL)
                .label("§cCancel")
                .blankLine()
                .lore("§eClick to cancel this change!")
                .origin(SLOT_CANCEL);
        for (int slot : RavengardButton.TEXT_CANCEL.coveredSlots(SLOT_CANCEL)) {
            layout.slot(slot, cancel.toBuilder(), (click, viewContext) -> viewContext.backOrClose());
        }

        RavengardItems.Builder back = RavengardItems.button(RavengardButton.BACK)
                .label("Go Back")
                .lore("§7Return to the previous menu.")
                .blankLine()
                .lore("§eClick to go back!")
                .origin(SLOT_BACK);
        layout.slot(SLOT_BACK, back.toBuilder(), (click, viewContext) -> viewContext.backOrClose());
    }
}

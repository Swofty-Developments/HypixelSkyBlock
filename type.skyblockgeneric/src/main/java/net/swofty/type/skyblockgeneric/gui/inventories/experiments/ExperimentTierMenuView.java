package net.swofty.type.skyblockgeneric.gui.inventories.experiments;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.experimentation.ExperimentTier;
import net.swofty.type.skyblockgeneric.experimentation.ExperimentType;
import net.swofty.type.skyblockgeneric.experimentation.ExperimentationManager;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Arrays;

abstract class ExperimentTierMenuView extends StatelessView {
    private static final int[] TIER_SLOTS = {20, 21, 22, 23, 24};
    private static final int[] BORDER_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53};

    protected abstract ExperimentType experimentType();

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>(experimentType().displayName() + " Tiers", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        layout.filler(Arrays.stream(BORDER_SLOTS).boxed().toList(),
                ItemStackCreator.getStack(" ", Material.PURPLE_STAINED_GLASS_PANE, 1));
        Components.backOrClose(layout, 40, ctx);

        ExperimentTier[] tiers = ExperimentTier.values();
        for (int i = 0; i < tiers.length; i++) {
            ExperimentTier tier = tiers[i];
            layout.slot(TIER_SLOTS[i], (s, c) -> ExperimentationGuiSupport.tierIcon(
                    experimentType(), tier, (SkyBlockPlayer) c.player()), (click, viewCtx) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) viewCtx.player();
                if (!tier.isUnlocked(player)) {
                    player.sendMessage(ExperimentationManager.requirementMessage(tier));
                    return;
                }
                if (!ExperimentationManager.start(player, experimentType(), tier)) {
                    player.sendMessage("§cYou already have an experiment in progress.");
                    return;
                }
                viewCtx.push(playView(tier));
            });
        }
    }

    protected abstract net.swofty.type.generic.gui.v2.View<DefaultState> playView(ExperimentTier tier);
}

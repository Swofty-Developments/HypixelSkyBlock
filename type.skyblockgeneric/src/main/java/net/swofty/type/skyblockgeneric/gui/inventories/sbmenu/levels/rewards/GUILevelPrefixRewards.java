package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.rewards;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointSkyBlockExperience;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelRequirement;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUILevelPrefixRewards extends StatelessView {
    private static final int[] SLOTS = new int[]{
            19, 20, 21, 22, 23, 24, 25,
            29, 30, 31, 32, 33
    };

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Prefix Rewards", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        // Title item
        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            DatapointSkyBlockExperience.PlayerSkyBlockExperience experience = player.getSkyBlockExperience();
            List<String> lore = new ArrayList<>();
            lore.add("§7New colors for your level prefix");
            lore.add("§7shown in TAB and in chat!");
            lore.add(" ");
            lore.add("§7Next Reward:");

            Map.Entry<SkyBlockLevelRequirement, String> nextPrefix = experience.getLevel().getNextPrefixChange();
            if (nextPrefix == null) {
                lore.add("§cNo more rewards!");
            } else {
                lore.add(nextPrefix.getValue() + nextPrefix.getKey().getPrefixDisplay());
                lore.add("§8at Level " + nextPrefix.getKey().asInt());
            }
            lore.add(" ");
            lore.addAll(GUILevelRewards.getAsDisplay(
                    player.getSkyBlockExperience().getLevel().getPreviousPrefixChanges().size(),
                    SkyBlockLevelRequirement.getAllPrefixChanges().size()
            ));

            return ItemStackCreator.getStack("§aPrefix Color Rewards", Material.GRAY_DYE, 1, lore);
        });

        // Prefix items
        int index = 0;
        for (Map.Entry<SkyBlockLevelRequirement, String> entry : SkyBlockLevelRequirement.getAllPrefixChanges().entrySet()) {
            if (index >= SLOTS.length) break;
            SkyBlockLevelRequirement level = entry.getKey();
            int slot = SLOTS[index];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                boolean unlocked = player.getSkyBlockExperience().getLevel().asInt() >= level.asInt();

                return ItemStackCreator.getStack(level.getPrefix() + level.getPrefixDisplay(),
                        level.getPrefixItem(), 1,
                        "§8Level " + level.asInt(),
                        " ",
                        "§7Preview: " + player.getFullDisplayName(level.getPrefix()),
                        " ",
                        (unlocked ? "§aYou have unlocked this reward!" : "§7Levels left to unlock: §3" + (level.asInt() - player.getSkyBlockExperience().getLevel().asInt())));
            });

            index++;
        }
    }
}

package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.rewards;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.emblem.GUIEmblems;
import net.swofty.type.skyblockgeneric.levels.SkyBlockEmblems;
import net.swofty.type.skyblockgeneric.levels.causes.LevelCause;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUILevelEmblemRewards extends StatelessView {
    private static final int[] SLOTS = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
    };

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Emblem Rewards", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        // View Emblems button
        layout.slot(50, (s, c) -> ItemStackCreator.getStack("§aPrefix Emblems", Material.NAME_TAG, 1,
                        "§7Add some spice by having an emblem",
                        "§7next to your name in chat and in tab!",
                        " ",
                        "§7Emblems are unlocked through various",
                        "§7activities such as leveling up",
                        "§7or completing achievements!",
                        " ",
                        "§7Emblems also show important data",
                        "§7associated with them in chat!",
                        " ",
                        "§eClick to view!"),
                (click, c) -> c.player().openView(new GUIEmblems()));

        // Title item
        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            List<String> lore = new ArrayList<>();
            lore.add("§7Emblems to show next to your name");
            lore.add("§7that signify special achievements.");
            lore.add(" ");
            lore.add("§7Next Reward:");

            List<SkyBlockEmblems.SkyBlockEmblem> levelEmblems = SkyBlockEmblems.getEmblemsWithLevelCause();
            SkyBlockEmblems.SkyBlockEmblem nextEmblem = null;
            for (SkyBlockEmblems.SkyBlockEmblem emblem : levelEmblems) {
                if (player.getSkyBlockExperience().hasExperienceFor(emblem.cause())) continue;
                nextEmblem = emblem;
                break;
            }

            if (nextEmblem == null) {
                lore.add("§cNo more rewards!");
            } else {
                lore.add("§f" + nextEmblem.displayName() + " " + nextEmblem.emblem());
                lore.add("§8at Level " + ((LevelCause) nextEmblem.cause()).getLevel());
            }

            lore.add(" ");
            lore.addAll(GUILevelRewards.getAsDisplay(
                    player.getSkyBlockExperience().getOfType(LevelCause.class).size(),
                    levelEmblems.size()
            ));

            return ItemStackCreator.getStack("§aEmblem Rewards", Material.NAME_TAG, 1, lore);
        });

        // Emblem items
        int index = 0;
        for (SkyBlockEmblems.SkyBlockEmblem emblem : SkyBlockEmblems.getEmblemsWithLevelCause()) {
            if (index >= SLOTS.length) break;
            int slot = SLOTS[index];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                return ItemStackCreator.getStack(emblem.displayName() + " " + emblem.emblem(),
                        Material.NAME_TAG, 1,
                        "§8Level " + ((LevelCause) emblem.cause()).getLevel(),
                        " ",
                        "§7Preview: " + player.getFullDisplayName(emblem),
                        " ",
                        (player.getSkyBlockExperience().hasExperienceFor(emblem.cause()) ? "§aYou have unlocked this reward!" : "§7Levels left to unlock: §3" + (((LevelCause) emblem.cause()).getLevel() - player.getSkyBlockExperience().getLevel().asInt())));
            });
            index++;
        }
    }
}

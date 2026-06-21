package net.swofty.type.bedwarslobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.bedwars.BedWarsDreamRotation;
import net.swofty.commons.bedwars.BedWarsGameType;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.lobby.GameQueueValidator;
import net.swofty.type.lobby.LobbyOrchestratorConnector;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class GUIDreamCalendar extends StatelessView {
    private static final int[] SLOTS = {10, 11, 12, 13, 14, 15, 16};

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Dream Calendar", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.back(layout, 31, ctx);

        LocalDate today = LocalDate.now();
        List<BedWarsDreamRotation.RotationEntry> entries = BedWarsDreamRotation.upcoming(today, SLOTS.length);
        for (int i = 0; i < entries.size(); i++) {
            BedWarsDreamRotation.RotationEntry entry = entries.get(i);
            layout.slot(SLOTS[i], (_, _) -> render(entry, today), (_, viewCtx) -> {
                if (!entry.current()) {
                    return;
                }
                queueCurrent(viewCtx, entry.mode().preferredQueueType());
            });
        }
    }

    private net.minestom.server.item.ItemStack.Builder render(BedWarsDreamRotation.RotationEntry entry, LocalDate today) {
        BedWarsDreamRotation.DreamMode mode = entry.mode();
        boolean current = entry.current();
        String prefix = current ? "§6✸ §b" : "§b";
        long days = Math.max(0, ChronoUnit.DAYS.between(today, entry.startsAt()));
        List<String> lore = new java.util.ArrayList<>();
        lore.add("§8Dream Rotation");
        lore.add("");
        lore.addAll(description(mode.displayName()));
        lore.add("");
        if (current) {
            lore.add("§aThis mode is currently featured.");
            lore.add("§eClick to play!");
        } else {
            lore.add("§eThis mode will be featured in " + days + " days.");
        }

        if (mode.displayName().contains("Lucky")) {
            return ItemStackCreator.getStackHead(prefix + mode.displayName(),
                "50d8f863e9b42653e642711ee8b854dd8f9463ef4bfcde7db9776daadb532b",
                1,
                lore);
        }
        return ItemStackCreator.getStack(prefix + mode.displayName(), icon(mode.displayName()), amount(mode.displayName()), lore);
    }

    private void queueCurrent(ViewContext ctx, BedWarsGameType type) {
        var player = ctx.player();
        player.closeInventory();
        if (!GameQueueValidator.canPlayerQueue(player, new GameQueueValidator.QueueRequirements(
            "Bed Wars",
            type.getQueueModeDisplayName(),
            type.getTeamSize()
        ))) {
            return;
        }
        new LobbyOrchestratorConnector(player).sendToGame(ServerType.BEDWARS_GAME, type.toString());
    }

    private Material icon(String name) {
        if (name.contains("Rush")) return Material.ENDER_EYE;
        if (name.contains("Ultimate")) return Material.NETHER_STAR;
        if (name.contains("Castle")) return Material.STONE_BRICKS;
        if (name.contains("Voidless")) return Material.BEDROCK;
        if (name.contains("Armed")) return Material.DIAMOND_HOE;
        return Material.RED_BED;
    }

    private int amount(String name) {
        return name.contains("Castle") ? 40 : 1;
    }

    private List<String> description(String name) {
        if (name.contains("Lucky"))
            return List.of("§7Find Lucky Blocks to earn a", "§7variety of events and effects,", "§7leading to total mayhem!");
        if (name.contains("Rush"))
            return List.of("§7Everything is upgraded at the start!", "§7Fight to the death right away!");
        if (name.contains("Ultimate"))
            return List.of("§7Pick an Ultimate Ability to use any", "§7time during the battle!");
        if (name.contains("Castle")) return List.of("§7Massive 40 versus 40 all out war on", "§7a unique Castle map!");
        if (name.contains("Voidless")) return List.of("§7No longer can you be a victim of the", "§7Void!");
        if (name.contains("Armed")) return List.of("§7Utilize a new arsenal of ranged", "§7weapons to win the game!");
        return List.of("§7Every few seconds brings a new", "§7surprise!");
    }
}

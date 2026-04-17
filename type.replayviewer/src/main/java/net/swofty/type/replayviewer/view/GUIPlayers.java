package net.swofty.type.replayviewer.view;

import net.minestom.server.entity.Entity;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.StatefulView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.replayviewer.TypeReplayViewerLoader;
import net.swofty.type.replayviewer.entity.ReplayPlayerEntity;
import net.swofty.type.replayviewer.playback.ReplaySession;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// TODO: use PaginatedView?
public class GUIPlayers implements StatefulView<GUIPlayers.State> {

    private static final int[] PLAYER_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    public record State(int page) {
    }

    private record PlayerEntry(int entityId, ReplayPlayerEntity entity) {
    }

    @Override
    public State initialState() {
        return new State(0);
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("Players", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        var sessionOpt = TypeReplayViewerLoader.getSession(ctx.player());
        if (sessionOpt.isEmpty()) {
            layout.slot(22, ItemStackCreator.getStack(
                "§cNo Replay Session",
                Material.BARRIER,
                1,
                "§7You are not currently watching",
                "§7a replay."
            ));
            Components.back(layout, 49, ctx);
            return;
        }

        ReplaySession replaySession = sessionOpt.get();
        List<PlayerEntry> players = collectPlayers(replaySession);

        int pageSize = PLAYER_SLOTS.length;
        int totalPages = Math.max(1, (players.size() + pageSize - 1) / pageSize);
        int currentPage = Math.min(state.page(), totalPages - 1);
        int startIndex = currentPage * pageSize;

        for (int i = 0; i < PLAYER_SLOTS.length; i++) {
            int slot = PLAYER_SLOTS[i];
            int index = startIndex + i;

            if (index >= players.size()) {
                layout.slot(slot, ItemStack.AIR.builder());
                continue;
            }

            PlayerEntry entry = players.get(index);
            ReplayPlayerEntity replayPlayer = entry.entity();
            String displayName = getDisplayName(replayPlayer);

            ItemStack.Builder head = replayPlayer.getSkin() != null
                ? ItemStackCreator.getStackHead(
                displayName,
                replayPlayer.getSkin(),
                1,
                "§7Health: §f" + Math.max(0, Math.round(replayPlayer.getHealth())),
                "",
                "§eLeft Click to teleport!",
                "§eRight Click for first person!"
            )
                : ItemStackCreator.getStack(
                displayName,
                Material.PLAYER_HEAD,
                1,
                "§7Health: §f" + Math.max(0, Math.round(replayPlayer.getHealth())),
                "",
                "§eLeft Click to teleport!",
                "§eRight Click for first person!"
            );

            layout.slot(slot, head, (click, c) -> {
                if (click.click() instanceof Click.Right) {
                    replaySession.followEntity(c.player(), entry.entityId());
                    c.player().closeInventory();
                    return;
                }

                c.player().teleport(replayPlayer.getPosition());
            });
        }

        if (currentPage > 0) {
            layout.slot(45, ItemStackCreator.getStack(
                "§aPrevious Page",
                Material.ARROW,
                1,
                "§7Page §e" + currentPage
            ), (_, c) -> c.session(State.class).setState(new State(currentPage - 1)));
        }

        if (currentPage < totalPages - 1) {
            layout.slot(53, ItemStackCreator.getStack(
                "§aNext Page",
                Material.ARROW,
                1,
                "§7Page §e" + (currentPage + 2)
            ), (_, c) -> c.session(State.class).setState(new State(currentPage + 1)));
        }

        Components.back(layout, 49, ctx);

        if (players.isEmpty()) {
            layout.slot(22, ItemStackCreator.getStack(
                "§cNo Players Found",
                Material.BARRIER,
                1,
                "§7No replay players are currently",
                "§7spawned for this timestamp."
            ));
        }
    }

    private static List<PlayerEntry> collectPlayers(ReplaySession session) {
        List<PlayerEntry> entries = new ArrayList<>();
        for (int entityId : session.getEntityManager().getEntityIds()) {
            Entity entity = session.getEntityManager().getEntity(entityId);
            if (entity instanceof ReplayPlayerEntity replayPlayerEntity) {
                entries.add(new PlayerEntry(entityId, replayPlayerEntity));
            }
        }

        entries.sort(Comparator.comparing(entry -> entry.entity().getPlayerName(), String.CASE_INSENSITIVE_ORDER));
        return entries;
    }

    private static String getDisplayName(ReplayPlayerEntity replayPlayer) {
        try {
            return HypixelPlayer.getDisplayName(replayPlayer.getActualUuid());
        } catch (Exception ignored) {
            return "§7" + replayPlayer.getPlayerName();
        }
    }
}

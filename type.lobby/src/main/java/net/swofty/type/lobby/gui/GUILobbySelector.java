package net.swofty.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.StatefulView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.lobby.LobbyServerOrder;
import net.swofty.type.lobby.ServerInfoCache;

import java.util.List;

public class GUILobbySelector implements StatefulView<GUILobbySelector.State> {
    private final ServerType lobbyType;
    private final String lobbyName;

    public GUILobbySelector(ServerType lobbyType, String lobbyName) {
        this.lobbyType = lobbyType;
        this.lobbyName = lobbyName;
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>(lobbyName + " Selector", InventoryType.CHEST_2_ROW);
    }

    @Override
    public State initialState() {
        return new State(null, null);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        if (state.error() != null) {
            layout.slot(4, ItemStackCreator.getStack("§cFailed to load lobbies", Material.BARRIER, 1, "§7" + state.error()));
            return;
        }
        if (state.lobbies() == null) {
            layout.slot(4, ItemStackCreator.getStack("§eLoading lobbies...", Material.CLOCK, 1, "§7Please wait..."));
            load(ctx);
            return;
        }
        if (state.lobbies().isEmpty()) {
            layout.slot(4, ItemStackCreator.getStack("§cNo lobbies available", Material.BARRIER, 1,
                "§7No lobbies are currently online."));
            return;
        }

        for (int i = 0; i < state.lobbies().size() && i < 18; i++) {
            UnderstandableProxyServer lobby = state.lobbies().get(i);
            boolean current = lobby.uuid().equals(HypixelConst.getServerUUID());
            int number = i + 1;
            layout.slot(i, ItemStackCreator.getStack(
                (current ? "§c" : "§a") + lobbyName + " #" + number,
                current ? Material.RED_TERRACOTTA : Material.QUARTZ_BLOCK,
                number,
                "§7Players: " + lobby.players().size() + "/" + lobby.maxPlayers(),
                "",
                current ? "§cAlready connected!" : "§eClick to connect!"
            ), (_, c) -> connect(c, lobby, number, current));
        }
    }

    private void load(ViewContext ctx) {
        ServerInfoCache.getServersByType(lobbyType)
            .thenApply(LobbyServerOrder::sortBySelectorOrder)
            .thenAccept(lobbies -> ctx.session(State.class).update(_ -> new State(lobbies, null)))
            .exceptionally(error -> {
                ctx.session(State.class).update(_ -> new State(List.of(), error.getMessage()));
                return null;
            });
    }

    private void connect(ViewContext ctx, UnderstandableProxyServer lobby, int number, boolean current) {
        if (current) {
            ctx.player().sendMessage("§cYou are already connected to this lobby!");
            return;
        }
        ctx.player().closeInventory();
        ctx.player().sendMessage("§aSending you to " + lobbyName + " #" + number + "...");
        ctx.player().asProxyPlayer().transferToWithIndication(lobby.uuid());
    }

    public record State(List<UnderstandableProxyServer> lobbies, String error) {
    }
}

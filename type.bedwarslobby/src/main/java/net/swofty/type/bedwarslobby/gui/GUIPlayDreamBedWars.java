package net.swofty.type.bedwarslobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.ServiceType;
import net.swofty.commons.bedwars.BedWarsDreamRotation;
import net.swofty.commons.bedwars.BedWarsGameType;
import net.swofty.commons.protocol.objects.orchestrator.ChooseGameProtocol;
import net.swofty.commons.protocol.objects.orchestrator.RejoinGameProtocol;
import net.swofty.proxyapi.ProxyService;
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
import java.util.ArrayList;
import java.util.List;

public class GUIPlayDreamBedWars extends StatelessView {
    private static final ProxyService ORCHESTRATOR = new ProxyService(ServiceType.ORCHESTRATOR);
    private static final String LUCKY_HEAD = "50d8f863e9b42653e642711ee8b854dd8f9463ef4bfcde7db9776daadb532b";

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Play Bed Wars", InventoryType.CHEST_5_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.close(layout, 40);

        layout.slot(4,
            (_, _) -> ItemStackCreator.getStack(
                "§bDream Calendar",
                Material.CLOCK,
                1,
                "",
                "§7See what §bDream Modes §7will enter",
                "§7rotation next!",
                "",
                "§eClick to open!"
            ),
            (_, viewCtx) -> viewCtx.push(new GUIDreamCalendar())
        );

        BedWarsDreamRotation.DreamMode dreamMode = BedWarsDreamRotation.current(LocalDate.now()).mode();
        List<BedWarsGameType> queueTypes = queueTypes(dreamMode);
        int[] slots = queueTypes.size() == 1 ? new int[]{22} : new int[]{21, 23};
        for (int i = 0; i < queueTypes.size(); i++) {
            BedWarsGameType type = queueTypes.get(i);
            layout.slot(slots[i], (_, _) -> dreamItem(dreamMode, type), (_, viewCtx) -> queue(viewCtx, type));
        }

        layout.slot(44,
            (_, _) -> ItemStackCreator.getStack(
                "§cClick here to rejoin!",
                Material.ENDER_PEARL,
                1,
                "§7Click here to rejoin your Bed Wars",
                "§7game if you have been disconnected",
                "§7from it."
            ),
            (_, viewCtx) -> rejoin(viewCtx)
        );
    }

    private List<BedWarsGameType> queueTypes(BedWarsDreamRotation.DreamMode mode) {
        List<BedWarsGameType> types = new ArrayList<>();
        types.add(mode.doublesType());
        if (mode.foursType() != null && mode.foursType() != mode.doublesType()) {
            types.add(mode.foursType());
        }
        return types;
    }

    private ItemStack.Builder dreamItem(BedWarsDreamRotation.DreamMode dreamMode, BedWarsGameType type) {
        String queueName = queueDisplay(type);
        List<String> lore = new ArrayList<>();
        lore.add("§7Play a game of Bed Wars " + type.getDisplayName());
        lore.add("§7" + queueName + ".");
        lore.add("");
        lore.addAll(type.getDescription());
        lore.add("");
        lore.add("§cOverall stats, achievements and");
        lore.add("§cquests will NOT be earned in this");
        lore.add("§cmode!");
        lore.add("");
        lore.add("§7Bed Wars Dreams is a variety of");
        lore.add("§7rotating game modes.");
        lore.add("");
        lore.add("§eClick to play!");

        if (type.getDisplayName().contains("Lucky")) {
            return ItemStackCreator.getStackHead("§a" + type.getDisplayName() + " " + queueName, LUCKY_HEAD, amount(type), lore);
        }
        return ItemStackCreator.getStack("§a" + type.getDisplayName() + " " + queueName, type.getIcon(), amount(type), lore);
    }

    private void queue(ViewContext ctx, BedWarsGameType type) {
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

    private void rejoin(ViewContext ctx) {
        var player = ctx.player();
        player.closeInventory();

        RejoinGameProtocol.RejoinGameRequest request =
            new RejoinGameProtocol.RejoinGameRequest(player.getUuid());

        ORCHESTRATOR.handleRequest(request).thenAccept(response -> {
            if (!(response instanceof RejoinGameProtocol.RejoinGameResponse resp)) {
                player.sendMessage("§cFailed to check for active games. Please try again.");
                return;
            }

            if (!resp.hasActiveGame() || resp.server() == null) {
                player.sendMessage("§cYou don't have an active game to rejoin!");
                return;
            }

            player.sendMessage("§aRejoining your game...");
            ChooseGameProtocol.ChooseGameMessage chooseMsg =
                new ChooseGameProtocol.ChooseGameMessage(player.getUuid(), resp.server(), resp.gameId());
            ORCHESTRATOR.handleRequest(chooseMsg);

            player.asProxyPlayer().transferToWithIndication(resp.server().uuid());
            player.getAchievementHandler().completeAchievement("bedwars.rejoining_the_dream");
        }).exceptionally(throwable -> {
            player.sendMessage("§cFailed to rejoin: " + throwable.getMessage());
            return null;
        });
    }

    private String queueDisplay(BedWarsGameType type) {
        if (type == BedWarsGameType.CASTLE) return "40v40";
        if (type.getTeamSize() == 4 && type.getTeams() == 4) return "4v4v4v4";
        if (type.getTeamSize() == 2) return "Doubles";
        if (type.getTeamSize() == 1) return "Solo";
        return type.getDisplayName();
    }

    private int amount(BedWarsGameType type) {
        return Math.max(1, type.getTeamSize());
    }

}

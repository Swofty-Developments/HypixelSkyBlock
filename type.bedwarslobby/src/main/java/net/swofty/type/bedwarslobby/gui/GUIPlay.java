package net.swofty.type.bedwarslobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
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

public class GUIPlay extends StatelessView {

	private final BedWarsGameType type;

	public GUIPlay(BedWarsGameType type) {
		if (type.isDream()) {
			throw new IllegalArgumentException("Dream types should not be used in this GUI!");
		}
		this.type = type;
	}

	@Override
	public ViewConfiguration<DefaultState> configuration() {
		return new ViewConfiguration<>("Play Bed Wars", InventoryType.CHEST_4_ROW);
	}

	@Override
	public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
		layout.allowHotkey(false);

		int playSlot = type == BedWarsGameType.FOUR_FOUR ? 13 : 12;
		layout.slot(playSlot, (s, viewCtx) -> {
			String description = String.join("\n", type.getDescription());
			return ItemStackCreator.getSingleLoreStackLineSplit(
				"§aBed Wars " + type.getDisplayName(),
				"§7",
				Material.RED_BED,
				1,
				"§7Play a game of Bed Wars " + type.getDisplayName() + "\n\n" + description + "\n\n§eClick to play!"
			);
		}, (_, viewCtx) -> {
			var player = viewCtx.player();
			player.closeInventory();

			if (!GameQueueValidator.canPlayerQueue(player, new GameQueueValidator.QueueRequirements(
				"Bed Wars",
				type.getQueueModeDisplayName(),
				type.getTeamSize()
			))) {
				return;
			}

			LobbyOrchestratorConnector connector = new LobbyOrchestratorConnector(player);
			connector.sendToGame(ServerType.BEDWARS_GAME, type.toString());
		});


		if (type != BedWarsGameType.FOUR_FOUR) {
			layout.slot(14,
				(s, viewCtx) -> ItemStackCreator.getStack("§aMap Selector " + type.getDisplayName(),
							Material.OAK_SIGN, 1,
							"§7Pick which map you want to play from",
							"§7a list of available maps.",
							"",
					"§eClick to browse!"),
				(_, viewCtx) -> viewCtx.push(new GUIMapSelection(type))
			);
		}

		layout.slot(35,
			(s, viewCtx) -> ItemStackCreator.getStack(
				"§cClick here to rejoin!",
				Material.ENDER_PEARL,
				1,
				"§7Click here to join your Bed Wars",
				"§7game if you have been disconnected",
				"§7from it."
			),
			(_, viewCtx) -> {
				// TODO: rejoin functionality
			}
		);

		Components.backOrClose(layout, 31, ctx);
	}
}

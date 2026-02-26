package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;
import net.swofty.commons.ServerType;
import net.swofty.velocity.gamemanager.balanceconfigurations.IslandCheck;
import net.swofty.velocity.gamemanager.balanceconfigurations.LowestPlayerCount;
import net.swofty.velocity.gamemanager.balanceconfigurations.ReadyGames;
import net.swofty.velocity.testflow.TestFlowManager;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BalanceConfigurations {
	public static HashMap<ServerType, List<BalanceConfiguration>> configurations = new HashMap<>(Map.ofEntries(
			Map.entry(ServerType.SKYBLOCK_ISLAND, List.of(
					new IslandCheck(),
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_HUB, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_DUNGEON_HUB, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_THE_FARMING_ISLANDS, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_SPIDERS_DEN, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_THE_END, List.of(
					new LowestPlayerCount()
            )),
			Map.entry(ServerType.SKYBLOCK_CRIMSON_ISLE, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_GOLD_MINE, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_DEEP_CAVERNS, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_DWARVEN_MINES, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_THE_PARK, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_GALATEA, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_BACKWATER_BAYOU, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.SKYBLOCK_JERRYS_WORKSHOP, List.of(
					new LowestPlayerCount()
			)),

			Map.entry(ServerType.PROTOTYPE_LOBBY, List.of(
					new LowestPlayerCount()
			)),

			Map.entry(ServerType.BEDWARS_LOBBY, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.BEDWARS_GAME, List.of(
					new ReadyGames()
			)),
            Map.entry(ServerType.SKYWARS_LOBBY, List.of(
                    new LowestPlayerCount()
            )),
            Map.entry(ServerType.SKYWARS_GAME, List.of(
                    new ReadyGames()
            )),
            Map.entry(ServerType.MURDER_MYSTERY_LOBBY, List.of(
                    new LowestPlayerCount()
            )),
            Map.entry(ServerType.MURDER_MYSTERY_GAME, List.of(
                    new ReadyGames()
            )),

            Map.entry(ServerType.SKYWARS_CONFIGURATOR, List.of(
                    new LowestPlayerCount()
            )),
			Map.entry(ServerType.BEDWARS_CONFIGURATOR, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.MURDER_MYSTERY_CONFIGURATOR, List.of(
					new LowestPlayerCount()
			)),
			Map.entry(ServerType.RAVENGARD_LOBBY, List.of(
					new LowestPlayerCount()
			))
	));

	public static @Nullable GameManager.GameServer getServerFor(Player player, ServerType type) {
		if (TestFlowManager.isPlayerInTestFlow(player.getUsername())) {
			player.sendPlainMessage("§eYou are currently in a network-isolated test flow, load balancing will be restricted to test flow servers!");
			player.sendPlainMessage("§8Executing test flow " + TestFlowManager.getTestFlowForPlayer(player.getUsername()).getName() + "...");
		}

		try {
			for (BalanceConfiguration configuration : configurations.get(type)) {
				List<GameManager.GameServer> serversToConsider = GameManager.getFromType(type);
				if (TestFlowManager.isPlayerInTestFlow(player.getUsername())) {
					serversToConsider.removeIf(server -> {
						boolean remove = server.maxPlayers() <= server.registeredServer().getPlayersConnected().size();

						if (!TestFlowManager.isServerInTestFlow(server.internalID())) {
							remove = true;
						}

						TestFlowManager.ProxyTestFlowInstance testFlowInstance = TestFlowManager.getFromServerUUID(
								server.internalID()
						);

						if (!testFlowInstance.hasPlayer(player.getUsername())) {
							remove = true;
						}

						return remove;
					});
				} else {
					serversToConsider.removeIf(server -> {
						boolean remove = server.maxPlayers() <= server.registeredServer().getPlayersConnected().size();

						if (TestFlowManager.isServerInTestFlow(server.internalID())) {
							remove = true;
						}

						return remove;
					});
				}

				GameManager.GameServer server = configuration.getServer(player, serversToConsider);

				if (server != null) {
					if (TestFlowManager.isPlayerInTestFlow(player.getUsername())) {
						player.sendPlainMessage("§8Done overriding the server manager for your test flow.");
					}
					return server;
				}
			}
			return null;
		} catch (Exception e) {
			System.out.println("Error in trying to balance type " + type.name() + " for player " + player.getUsername());
			throw e;
		}
	}
}

package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.swofty.commons.ServerType;
import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.redis.RedisMessage;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record TransferHandler(Player player) {
	public static final Map<Player, ServerType> playersGoalServerType = new HashMap<>();
	private static final Map<Player, RegisteredServer> playersOriginServer = new HashMap<>();
	private static final List<Player> disregard = new ArrayList<>();

	public boolean isInLimbo() {
		return playersGoalServerType.containsKey(player);
	}

	public void addToDisregard() {
		disregard.add(player);
	}

	public void removeFromDisregard() {
		disregard.remove(player);
	}

	public CompletableFuture<Boolean> sendToLimbo() {
		CompletableFuture<Boolean> future = new CompletableFuture<>();

		new Thread(() -> {
			if (player.getCurrentServer().isPresent()) {
				RegisteredServer previousServer = player.getCurrentServer().get().getServer();
				playersOriginServer.put(player, previousServer);
			}

			RegisteredServer limboServer = SkyBlockVelocity.getLimboServer();
			player.createConnectionRequest(limboServer).connectWithIndication();
			future.complete(true);
		}).start();

		return future;
	}

	public void previousServerIsFinished(RegisteredServer manualPick) {
		new Thread(() -> {
			if (disregard.contains(player)) return;

			RegisteredServer originServer = playersOriginServer.get(player);
			ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

			UUID serverUUID = UUID.fromString(manualPick.getServerInfo().getName());
			UUID originServerUUID = UUID.fromString(originServer.getServerInfo().getName());

			RedisMessage.sendMessageToServer(serverUUID,
					FromProxyChannels.GIVE_PLAYERS_ORIGIN_TYPE,
					new JSONObject().put("uuid", player.getUniqueId().toString())
							.put("origin-type", originServerType.name())
			);

			playersGoalServerType.remove(player);
			playersOriginServer.remove(player);

			GameManager.GameServer manualPickAsGame = GameManager.getFromUUID(serverUUID);
			player.sendMessage(Component.text("§7Sending to server " + manualPickAsGame.displayName() + "..."));
			player.createConnectionRequest(manualPick).connectWithIndication();

			RedisMessage.sendMessageToServer(originServerUUID,
					FromProxyChannels.PLAYER_HAS_SWITCHED_FROM_HERE,
					new JSONObject().put("uuid", player.getUniqueId().toString()));
		}).start();
	}

	public void previousServerIsFinished() {
		new Thread(() -> {
			if (disregard.contains(player)) return;

			ServerType type = playersGoalServerType.get(player);
			GameManager.GameServer server = BalanceConfigurations.getServerFor(player, type);

			if (server == null) {
				player.disconnect(Component.text("§cThere are no Hypixel (type=" + type.name() + ") servers available at the moment."));
				return;
			}

			RegisteredServer originServer = playersOriginServer.get(player);
			UUID originServerUUID = UUID.fromString(originServer.getServerInfo().getName());
			UUID sendingToServerUUID = server.internalID();
			ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

			RedisMessage.sendMessageToServer(sendingToServerUUID,
					FromProxyChannels.GIVE_PLAYERS_ORIGIN_TYPE,
					new JSONObject().put("uuid", player.getUniqueId().toString())
							.put("origin-type", originServerType.name())
			);

			playersOriginServer.remove(player);
			playersGoalServerType.remove(player);

			player.sendMessage(Component.text("§7Sending to server " + server.displayName() + "..."));
			player.createConnectionRequest(server.registeredServer()).connectWithIndication();

			RedisMessage.sendMessageToServer(originServerUUID,
					FromProxyChannels.PLAYER_HAS_SWITCHED_FROM_HERE,
					new JSONObject().put("uuid", player.getUniqueId().toString()));
		}).start();
	}

	public void transferTo(ServerType type) {
		new Thread(() -> {
			RegisteredServer originServer = playersOriginServer.get(player);
			if (originServer == null) {
				player.getCurrentServer().ifPresent(conn -> playersOriginServer.put(player, conn.getServer()));
				originServer = playersOriginServer.get(player);
			}
			ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

			playersGoalServerType.remove(player);
			playersOriginServer.remove(player);

			GameManager.GameServer server = BalanceConfigurations.getServerFor(player, type);

			if (server == null) {
				player.disconnect(Component.text("§cThere are no Hypixel (type=" + type.name() + ") servers available at the moment."));
				return;
			}

			if (originServer != null && originServerType != null) {
				RedisMessage.sendMessageToServer(server.internalID(),
						FromProxyChannels.GIVE_PLAYERS_ORIGIN_TYPE,
						new JSONObject().put("uuid", player.getUniqueId().toString())
								.put("origin-type", originServerType.name())
				);
			}

			player.sendMessage(Component.text("§7Sending to server " + server.displayName() + "..."));
			player.createConnectionRequest(server.registeredServer()).connectWithIndication();
		}).start();
	}

	public void forceRemoveFromLimbo() {
		playersGoalServerType.remove(player);
		playersOriginServer.remove(player);
	}

	public CompletableFuture<Void> transferTo(RegisteredServer toTransferTo) {
		CompletableFuture<Void> future = new CompletableFuture<>();
		new Thread(() -> {
			try {
				RegisteredServer originServer = playersOriginServer.get(player);
				if (originServer == null) {
					player.getCurrentServer().ifPresent(conn -> playersOriginServer.put(player, conn.getServer()));
					originServer = playersOriginServer.get(player);
				}
				ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

				playersGoalServerType.remove(player);
				playersOriginServer.remove(player);

				UUID serverUUID = UUID.fromString(toTransferTo.getServerInfo().getName());

				if (originServer != null && originServerType != null) {
					RedisMessage.sendMessageToServer(serverUUID,
							FromProxyChannels.GIVE_PLAYERS_ORIGIN_TYPE,
							new JSONObject().put("uuid", player.getUniqueId().toString())
									.put("origin-type", originServerType.name())
					);
				}

				player.createConnectionRequest(toTransferTo).connectWithIndication();
				future.complete(null);
			} catch (Exception e) {
				future.completeExceptionally(e);
			}
		}).start();
		return future;
	}
}

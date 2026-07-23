package net.swofty.velocity.gamemanager;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.swofty.commons.ServerType;
import net.swofty.commons.protocol.objects.proxy.from.GivePlayersOriginTypeProtocol;
import net.swofty.commons.protocol.objects.proxy.from.PrepareTransferProtocol;
import java.util.concurrent.TimeUnit;
import net.swofty.commons.protocol.objects.proxy.from.PlayerSwitchedProtocol;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.commons.redis.RedisClient;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public record TransferHandler(Player player) {
	public static final Map<Player, ServerType> playersGoalServerType = new ConcurrentHashMap<>();
	private static final Map<Player, RegisteredServer> playersOriginServer = new ConcurrentHashMap<>();
	private static final Set<Player> disregard = ConcurrentHashMap.newKeySet();
	private static final Map<UUID, ServerType> afkOriginLobbyType = new ConcurrentHashMap<>();
	private static final Set<UUID> afkReturnInProgress = ConcurrentHashMap.newKeySet();

	public boolean isInLimbo() {
		return playersGoalServerType.containsKey(player);
	}

	public boolean isInAfkLimbo() {
		return afkOriginLobbyType.containsKey(player.getUniqueId());
	}

	public static void clearPlayerState(UUID playerUuid) {
		afkOriginLobbyType.remove(playerUuid);
		afkReturnInProgress.remove(playerUuid);
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
			if (isInLimbo() && player.getCurrentServer()
				.map(server -> server.getServer().equals(SkyBlockVelocity.getLimboServer()))
				.orElse(false)) {
				future.complete(true);
				return;
			}

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

	public CompletableFuture<Boolean> sendToLimboFromAfk(ServerType originType) {
		CompletableFuture<Boolean> future = new CompletableFuture<>();

		new Thread(() -> {
			if (isInAfkLimbo() && player.getCurrentServer()
				.map(server -> server.getServer().equals(SkyBlockVelocity.getLimboServer()))
				.orElse(false)) {
				future.complete(true);
				return;
			}

			ServerType resolvedOriginType = isLobbyType(originType) ? originType : ServerType.PROTOTYPE_LOBBY;
			afkOriginLobbyType.put(player.getUniqueId(), resolvedOriginType);

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

	public void returnFromAfkLimbo() {
		new Thread(() -> {
			UUID uuid = player.getUniqueId();
			if (!afkReturnInProgress.add(uuid)) {
				return;
			}

			try {
				if (!isInAfkLimbo()) {
					return;
				}

				if (player.getCurrentServer().isEmpty()
					|| !player.getCurrentServer().get().getServer().equals(SkyBlockVelocity.getLimboServer())) {
					return;
				}

				ServerType preferredType = afkOriginLobbyType.get(uuid);
				GameManager.GameServer destination = null;

				if (isLobbyType(preferredType)
					&& GameManager.hasType(preferredType)
					&& GameManager.isAnyEmpty(preferredType)) {
					destination = BalanceConfigurations.getServerFor(player, preferredType);
				}

				if (destination == null
					&& GameManager.hasType(ServerType.PROTOTYPE_LOBBY)
					&& GameManager.isAnyEmpty(ServerType.PROTOTYPE_LOBBY)) {
					destination = BalanceConfigurations.getServerFor(player, ServerType.PROTOTYPE_LOBBY);
				}

				if (destination == null) {
					player.sendMessage(Component.text("§cThere are no lobby servers available right now."));
					return;
				}

				afkOriginLobbyType.remove(uuid);
				player.sendMessage(Component.text("§7Sending to server " + destination.displayName() + "..."));
				player.createConnectionRequest(destination.registeredServer()).connectWithIndication();
			} finally {
				afkReturnInProgress.remove(uuid);
			}
		}).start();
	}

	public void previousServerIsFinished(RegisteredServer manualPick) {
		new Thread(() -> {
			if (disregard.contains(player)) return;

			RegisteredServer originServer = playersOriginServer.get(player);
			ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

			UUID serverUUID = UUID.fromString(manualPick.getServerInfo().getName());
			UUID originServerUUID = UUID.fromString(originServer.getServerInfo().getName());

			RedisClient.requestServer(serverUUID,
					new GivePlayersOriginTypeProtocol(),
					new GivePlayersOriginTypeProtocol.Request(
							player.getUniqueId().toString(), originServerType.name()));

			playersGoalServerType.remove(player);
			playersOriginServer.remove(player);

			GameManager.GameServer manualPickAsGame = GameManager.getFromUUID(serverUUID);
			player.sendMessage(Component.text("§7Sending to server " + manualPickAsGame.displayName() + "..."));
			player.createConnectionRequest(manualPick).connectWithIndication();

			RedisClient.requestServer(originServerUUID,
					new PlayerSwitchedProtocol(),
					new PlayerSwitchedProtocol.Request(player.getUniqueId().toString()));
		}).start();
	}

	public void previousServerIsFinished() {
		new Thread(() -> {
			if (disregard.contains(player) || !isInLimbo()) return;

			ServerType type = playersGoalServerType.get(player);
			if (type == null) {
				playersGoalServerType.remove(player);
				playersOriginServer.remove(player);
				return;
			}
			GameManager.GameServer server = BalanceConfigurations.getServerFor(player, type);

			if (server == null) {
				playersGoalServerType.remove(player);
				playersOriginServer.remove(player);
				player.disconnect(Component.text("§cThere are no Hypixel (type=" + type.name() + ") servers available at the moment."));
				return;
			}

			RegisteredServer originServer = playersOriginServer.get(player);
			if (originServer == null) {
				playersGoalServerType.remove(player);
				return;
			}
			UUID originServerUUID = UUID.fromString(originServer.getServerInfo().getName());
			UUID sendingToServerUUID = server.internalID();
			ServerType originServerType = GameManager.getTypeFromRegisteredServer(originServer);

			RedisClient.requestServer(sendingToServerUUID,
					new GivePlayersOriginTypeProtocol(),
					new GivePlayersOriginTypeProtocol.Request(
							player.getUniqueId().toString(), originServerType.name()));

			playersOriginServer.remove(player);
			playersGoalServerType.remove(player);

			player.sendMessage(Component.text("§7Sending to server " + server.displayName() + "..."));
			player.createConnectionRequest(server.registeredServer()).connectWithIndication();

			RedisClient.requestServer(originServerUUID,
					new PlayerSwitchedProtocol(),
					new PlayerSwitchedProtocol.Request(player.getUniqueId().toString()));
		}).start();
	}

	public void transferTo(ServerType type) {
		if (type == null) {
			return;
		}

		GameManager.GameServer server = BalanceConfigurations.getServerFor(player, type);
		if (server == null) {
			player.sendMessage(Component.text("§cThere are no Hypixel (type=" + type.name() + ") servers available at the moment."));
			return;
		}

		player.sendMessage(Component.text("§7Sending to server " + server.displayName() + "..."));
		transferTo(server.registeredServer());
	}

	public void forceRemoveFromLimbo() {
		playersGoalServerType.remove(player);
		playersOriginServer.remove(player);
		clearPlayerState(player.getUniqueId());
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
					RedisClient.requestServer(serverUUID,
							new GivePlayersOriginTypeProtocol(),
							new GivePlayersOriginTypeProtocol.Request(
									player.getUniqueId().toString(), originServerType.name()));
				}

				try {
					RedisClient.requestServer(serverUUID,
							new PrepareTransferProtocol(),
							new PrepareTransferProtocol.Request(player.getUniqueId().toString()))
							.orTimeout(3, TimeUnit.SECONDS).join();
				} catch (Exception ignored) {
				}

				player.createConnectionRequest(toTransferTo).connectWithIndication();
				future.complete(null);
			} catch (Exception e) {
				future.completeExceptionally(e);
			}
		}).start();
		return future;
	}

	private static boolean isLobbyType(ServerType type) {
		return type != null && type.name().endsWith("_LOBBY");
	}
}

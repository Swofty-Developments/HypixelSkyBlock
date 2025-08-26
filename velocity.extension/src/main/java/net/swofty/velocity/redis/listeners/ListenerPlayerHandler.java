package net.swofty.velocity.redis.listeners;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.swofty.commons.Configuration;
import net.swofty.commons.ServerType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.proxy.FromProxyChannels;
import net.swofty.commons.proxy.ToProxyChannels;
import net.swofty.commons.proxy.requirements.to.PlayerHandlerRequirements;
import net.swofty.velocity.SkyBlockVelocity;
import net.swofty.velocity.gamemanager.GameManager;
import net.swofty.velocity.gamemanager.TransferHandler;
import net.swofty.velocity.redis.ChannelListener;
import net.swofty.velocity.redis.RedisListener;
import net.swofty.velocity.redis.RedisMessage;
import org.json.JSONObject;

import java.util.Optional;
import java.util.UUID;

@ChannelListener(channel = ToProxyChannels.PLAYER_HANDLER)
public class ListenerPlayerHandler extends RedisListener {
	private static final java.util.Map<java.util.UUID, org.json.JSONObject> bedwarsPreferences = new java.util.concurrent.ConcurrentHashMap<>();

	@Override
	public JSONObject receivedMessage(JSONObject message, UUID serverUUID) {
		UUID uuid = UUID.fromString(message.getString("uuid"));
		PlayerHandlerRequirements.PlayerHandlerActions action =
				PlayerHandlerRequirements.PlayerHandlerActions.valueOf(
						message.getString("action"));

		Optional<Player> potentialPlayer = SkyBlockVelocity.getServer().getPlayer(uuid);
		if (potentialPlayer.isEmpty()) {
			if (action == PlayerHandlerRequirements.PlayerHandlerActions.IS_ONLINE) {
				return new JSONObject().put("isOnline", false);
			}
			return new JSONObject();
		}
		if (action == PlayerHandlerRequirements.PlayerHandlerActions.IS_ONLINE) {
			return new JSONObject().put("isOnline", true);
		}
		Player player = potentialPlayer.get();
		Optional<ServerConnection> potentialServer = player.getCurrentServer();

		switch (action) {
			case GET_SERVER -> {
				UUID playersServer = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
				GameManager.GameServer serverInfo = GameManager.getFromUUID(playersServer);
				if (serverInfo == null) {
					return new JSONObject();
				}

				return new JSONObject().put("server", new UnderstandableProxyServer(
						serverInfo.displayName(),
						serverInfo.internalID(),
						GameManager.getTypeFromRegisteredServer(serverInfo.registeredServer()),
						serverInfo.registeredServer().getServerInfo().getAddress().getPort(),
						serverInfo.registeredServer().getPlayersConnected().stream().map(Player::getUniqueId).toList(),
						serverInfo.maxPlayers(),
						serverInfo.shortDisplayName()
				).toJSON());
			}
			case BEDWARS_SET_PREFERENCE -> {
				net.swofty.velocity.bedwars.BedWarsPreferenceStore.put(uuid, message);
			}
			case TRANSFER_WITH_UUID -> {
				UUID server = UUID.fromString(message.getString("server_uuid"));
				System.out.println("Transfer with UUID: " + server);
				GameManager.GameServer serverInfo = GameManager.getFromUUID(server);
				TransferHandler transferHandler = new TransferHandler(player);

				if (serverInfo == null) {
					player.sendMessage(Component.text(
							"§cWe encountered an issue while attempting to locate the server on the network. Please try again later."
					));
					return new JSONObject();
				}

				player.sendMessage(Component.text("§7Sending to server " + serverInfo.displayName() + "..."));

				if (!serverInfo.hasEmptySlots()) {
					player.sendMessage(Component.text(
							"§cAttempted to connect to " + serverInfo.displayName() + ", but there are no empty slots available. Please try again later."
					));
					return new JSONObject();
				}

				transferHandler.addToDisregard();
				transferHandler.sendToLimbo().join();

				// Trick the packet blocker into thinking player is in normal transfer process
				TransferHandler.playersGoalServerType.put(player, ServerType.SKYBLOCK_HUB);

				try {
					Thread.sleep(Long.parseLong(Configuration.get("transfer-timeout")));
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}

				TransferHandler.playersGoalServerType.remove(player);
				// forward any stored Be dWars preference to the target server before final transfer
				org.json.JSONObject pref = net.swofty.velocity.bedwars.BedWarsPreferenceStore.take(uuid);
				if (pref != null) {
					RedisMessage.sendMessageToServer(server,
							FromProxyChannels.BEDWARS_JOIN_PREFERENCE,
							new JSONObject()
									.put("uuid", uuid.toString())
									.put("mode", pref.optString("mode", ""))
									.put("map", pref.optString("map", ""))
					).join();
				}

				transferHandler.noLimboTransferTo(serverInfo.registeredServer());
				transferHandler.removeFromDisregard();
			}
			case TRANSFER -> {
				ServerType type = ServerType.valueOf(message.getString("type"));
				if (!GameManager.hasType(type)
						|| new TransferHandler(player).isInLimbo()
						|| !GameManager.isAnyEmpty(type)) {
					player.sendMessage(Component.text(
							"§cAttempted to transfer to a " + StringUtility.toNormalCase(type.name()) + " server, but there are no empty slots available. Please try again later."
					));
					return new JSONObject();
				}
				new TransferHandler(player).standardTransferTo(
						player.getCurrentServer().get().getServer(),
						type
				);
			}
			case TELEPORT -> {
				if (potentialServer.isEmpty()) {
					return new JSONObject();
				}
				UUID server = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
				return RedisMessage.sendMessageToServer(server,
						FromProxyChannels.TELEPORT,
						message).join();
			}
			case EVENT -> {
				if (potentialServer.isEmpty()) {
					return new JSONObject();
				}
				UUID server = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
				RedisMessage.sendMessageToServer(server,
						FromProxyChannels.RUN_EVENT_ON_SERVER,
						message
				).join();
			}
			case REFRESH_COOP_DATA -> {
				if (potentialServer.isEmpty()) {
					return new JSONObject();
				}
				UUID server = UUID.fromString(potentialServer.get().getServer().getServerInfo().getName());
				RedisMessage.sendMessageToServer(server,
						FromProxyChannels.REFRESH_COOP_DATA_ON_SERVER,
						message
				).join();
			}
			case MESSAGE -> {
				String messageToSend = message.getString("message");
				player.sendMessage(JSONComponentSerializer.json().deserialize(messageToSend));
			}
		}
		;
		return new JSONObject();
	}
}

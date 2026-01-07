package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.ServerType;
import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.GetServerForMapProtocolObject;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.murdermystery.MurderMysteryGameType;
import net.swofty.commons.skywars.SkywarsGameType;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.service.generic.redis.ServiceToServerManager;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.orchestrator.OrchestratorCache;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class GetServerForMapEndpoint implements ServiceEndpoint
		<GetServerForMapProtocolObject.GetServerForMapMessage,
				GetServerForMapProtocolObject.GetServerForMapResponse> {

	@Override
	public ProtocolObject<GetServerForMapProtocolObject.GetServerForMapMessage, GetServerForMapProtocolObject.GetServerForMapResponse> associatedProtocolObject() {
		return new GetServerForMapProtocolObject();
	}

	@Override
	public GetServerForMapProtocolObject.GetServerForMapResponse onMessage(ServiceProxyRequest message,
																		   GetServerForMapProtocolObject.GetServerForMapMessage body) {
		return switch (body.type()) {
			case BEDWARS_GAME -> handleBedwars(body);
			case MURDER_MYSTERY_GAME -> handleMurderMystery(body);
			case SKYWARS_GAME -> handleSkywars(body);
			default -> new GetServerForMapProtocolObject.GetServerForMapResponse(null, null);
		};
	}

	private GetServerForMapProtocolObject.GetServerForMapResponse handleBedwars(
			GetServerForMapProtocolObject.GetServerForMapMessage body) {
		try {
			BedwarsGameType gameType = parseBedwarsGameType(body.mode());
			if (gameType == null) {
				return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null);
			}

			int neededSlots = body.neededSlots() > 0 ? body.neededSlots() : 1;

			// First, try to find an existing joinable game with enough slots
			OrchestratorCache.GameWithServer existingGameWithServer = OrchestratorCache.findExisting(gameType, body.map(), neededSlots);
			if (existingGameWithServer != null) {
				OrchestratorCache.GameServerState hostingServer = OrchestratorCache.getServerByUuid(existingGameWithServer.serverUuid());
				if (hostingServer != null) {
					UnderstandableProxyServer proxy = new UnderstandableProxyServer(
							hostingServer.shortName(),
							hostingServer.uuid(),
							hostingServer.type(),
							-1,
							new ArrayList<>(),
							hostingServer.maxPlayers(),
							hostingServer.shortName()
					);
					return new GetServerForMapProtocolObject.GetServerForMapResponse(proxy, existingGameWithServer.game().getGameId().toString());
				}
			}

			// If no existing game found, find a server that can instantiate a new one
			OrchestratorCache.GameServerState availableServer = OrchestratorCache.instantiateServer(gameType, body.map());
			if (availableServer != null) {
				// Send service message to create the game
				JSONObject instantiateMessage = new JSONObject();
				instantiateMessage.put("gameType", gameType.toString());
				instantiateMessage.put("map", body.map());

				try {
					CompletableFuture<JSONObject> responseFuture = ServiceToServerManager.sendToServer(
							availableServer.uuid(),
							FromServiceChannels.INSTANTIATE_GAME,
							instantiateMessage
					);

					JSONObject response = responseFuture.get();

					if (response != null && response.optBoolean("success", false)) {
						// Game created successfully, return the server
						UnderstandableProxyServer proxy = new UnderstandableProxyServer(
								availableServer.shortName(),
								availableServer.uuid(),
								availableServer.type(),
								-1,
								new ArrayList<>(),
								availableServer.maxPlayers(),
								availableServer.shortName()
						);
						return new GetServerForMapProtocolObject.GetServerForMapResponse(proxy, response.getString("gameId"));
					}
				} catch (Exception e) {
					System.err.println("Failed to instantiate Bedwars game: " + e.getMessage());
				}
			}

			return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null);
		} catch (Exception e) {
			return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null);
		}
	}

	private GetServerForMapProtocolObject.GetServerForMapResponse handleMurderMystery(
			GetServerForMapProtocolObject.GetServerForMapMessage body) {
		try {
			MurderMysteryGameType gameType = parseMurderMysteryGameType(body.mode());
			if (gameType == null) {
				return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null);
			}

			int neededSlots = body.neededSlots() > 0 ? body.neededSlots() : 1;

			// First, try to find an existing joinable game with enough slots
			OrchestratorCache.GameWithServer existingGameWithServer = OrchestratorCache.findExisting(
					ServerType.MURDER_MYSTERY_GAME, gameType.getMaxPlayers(), body.map(), neededSlots, gameType.name());
			if (existingGameWithServer != null) {
				OrchestratorCache.GameServerState hostingServer = OrchestratorCache.getServerByUuid(existingGameWithServer.serverUuid());
				if (hostingServer != null) {
					UnderstandableProxyServer proxy = new UnderstandableProxyServer(
							hostingServer.shortName(),
							hostingServer.uuid(),
							hostingServer.type(),
							-1,
							new ArrayList<>(),
							hostingServer.maxPlayers(),
							hostingServer.shortName()
					);
					return new GetServerForMapProtocolObject.GetServerForMapResponse(proxy, existingGameWithServer.game().getGameId().toString());
				}
			}

			// If no existing game found, find a server that can instantiate a new one
			OrchestratorCache.GameServerState availableServer = OrchestratorCache.instantiateServer(
					ServerType.MURDER_MYSTERY_GAME, gameType.getMaxPlayers());
			if (availableServer != null) {
				// Send service message to create the game
				JSONObject instantiateMessage = new JSONObject();
				instantiateMessage.put("gameType", gameType.name());
				instantiateMessage.put("map", body.map());

				try {
					CompletableFuture<JSONObject> responseFuture = ServiceToServerManager.sendToServer(
							availableServer.uuid(),
							FromServiceChannels.INSTANTIATE_GAME,
							instantiateMessage
					);

					JSONObject response = responseFuture.get();

					if (response != null && response.optBoolean("success", false)) {
						// Game created successfully, return the server
						UnderstandableProxyServer proxy = new UnderstandableProxyServer(
								availableServer.shortName(),
								availableServer.uuid(),
								availableServer.type(),
								-1,
								new ArrayList<>(),
								availableServer.maxPlayers(),
								availableServer.shortName()
						);
						return new GetServerForMapProtocolObject.GetServerForMapResponse(proxy, response.getString("gameId"));
					}
				} catch (Exception e) {
					System.err.println("Failed to instantiate Murder Mystery game: " + e.getMessage());
				}
			}

			return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null);
		} catch (Exception e) {
			return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null);
		}
	}

	private BedwarsGameType parseBedwarsGameType(String mode) {
		if (mode == null) return null;

		try {
			return BedwarsGameType.valueOf(mode.toUpperCase());
		} catch (IllegalArgumentException e) {
			switch (mode.toLowerCase()) {
				case "solo", "1v1v1v1v1v1v1v1" -> { return BedwarsGameType.SOLO; }
				case "doubles", "2v2v2v2" -> { return BedwarsGameType.DOUBLES; }
				case "triples", "3v3v3v3" -> { return BedwarsGameType.THREE_THREE_THREE_THREE; }
				case "quads", "4v4v4v4" -> { return BedwarsGameType.FOUR_FOUR_FOUR_FOUR; }
				case "4v4" -> { return BedwarsGameType.FOUR_FOUR; }
				default -> { return null; }
			}
		}
	}

	private MurderMysteryGameType parseMurderMysteryGameType(String mode) {
		if (mode == null) return null;

		// First try the enum's from() method
		MurderMysteryGameType type = MurderMysteryGameType.from(mode);
		if (type != null) return type;

		// Try display name
		type = MurderMysteryGameType.fromDisplayName(mode);
		if (type != null) return type;

		// Handle common aliases
		return switch (mode.toLowerCase()) {
			case "classic" -> MurderMysteryGameType.CLASSIC;
			case "double_up", "doubleup", "double up" -> MurderMysteryGameType.DOUBLE_UP;
			case "assassins" -> MurderMysteryGameType.ASSASSINS;
			default -> null;
		};
	}

	private GetServerForMapProtocolObject.GetServerForMapResponse handleSkywars(
			GetServerForMapProtocolObject.GetServerForMapMessage body) {
		try {
			SkywarsGameType gameType = parseSkywarsGameType(body.mode());
			if (gameType == null) {
				return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null);
			}

			int neededSlots = body.neededSlots() > 0 ? body.neededSlots() : 1;

			// First, try to find an existing joinable game with enough slots
			OrchestratorCache.GameWithServer existingGameWithServer = OrchestratorCache.findExisting(
					ServerType.SKYWARS_GAME, gameType.getMaxPlayers(), body.map(), neededSlots, gameType.name());
			if (existingGameWithServer != null) {
				OrchestratorCache.GameServerState hostingServer = OrchestratorCache.getServerByUuid(existingGameWithServer.serverUuid());
				if (hostingServer != null) {
					UnderstandableProxyServer proxy = new UnderstandableProxyServer(
							hostingServer.shortName(),
							hostingServer.uuid(),
							hostingServer.type(),
							-1,
							new ArrayList<>(),
							hostingServer.maxPlayers(),
							hostingServer.shortName()
					);
					return new GetServerForMapProtocolObject.GetServerForMapResponse(proxy, existingGameWithServer.game().getGameId().toString());
				}
			}

			// If no existing game found, find a server that can instantiate a new one
			OrchestratorCache.GameServerState availableServer = OrchestratorCache.instantiateServer(
					ServerType.SKYWARS_GAME, gameType.getMaxPlayers());
			if (availableServer != null) {
				// Send service message to create the game
				JSONObject instantiateMessage = new JSONObject();
				instantiateMessage.put("gameType", gameType.name());
				instantiateMessage.put("map", body.map());

				try {
					CompletableFuture<JSONObject> responseFuture = ServiceToServerManager.sendToServer(
							availableServer.uuid(),
							FromServiceChannels.INSTANTIATE_GAME,
							instantiateMessage
					);

					JSONObject response = responseFuture.get();

					if (response != null && response.optBoolean("success", false)) {
						// Game created successfully, return the server
						UnderstandableProxyServer proxy = new UnderstandableProxyServer(
								availableServer.shortName(),
								availableServer.uuid(),
								availableServer.type(),
								-1,
								new ArrayList<>(),
								availableServer.maxPlayers(),
								availableServer.shortName()
						);
						return new GetServerForMapProtocolObject.GetServerForMapResponse(proxy, response.getString("gameId"));
					}
				} catch (Exception e) {
					System.err.println("Failed to instantiate Skywars game: " + e.getMessage());
				}
			}

			return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null);
		} catch (Exception e) {
			return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null);
		}
	}

	private SkywarsGameType parseSkywarsGameType(String mode) {
		if (mode == null) return null;

		// First try the enum's from() method
		SkywarsGameType type = SkywarsGameType.from(mode);
		if (type != null) return type;

		// Try display name
		type = SkywarsGameType.fromDisplayName(mode);
		if (type != null) return type;

		// Handle common aliases
		return switch (mode.toLowerCase()) {
			case "solo_normal", "solo normal", "normal" -> SkywarsGameType.SOLO_NORMAL;
			case "solo_insane", "solo insane", "insane" -> SkywarsGameType.SOLO_INSANE;
			case "doubles_normal", "doubles normal", "doubles" -> SkywarsGameType.DOUBLES_NORMAL;
			case "solo_lucky_block", "lucky_block", "lucky block", "luckyblock" -> SkywarsGameType.SOLO_LUCKY_BLOCK;
			default -> null;
		};
	}
}

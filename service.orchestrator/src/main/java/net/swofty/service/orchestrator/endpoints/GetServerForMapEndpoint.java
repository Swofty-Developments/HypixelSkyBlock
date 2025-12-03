package net.swofty.service.orchestrator.endpoints;

import net.swofty.commons.UnderstandableProxyServer;
import net.swofty.commons.impl.ServiceProxyRequest;
import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.objects.orchestrator.GetServerForMapProtocolObject;
import net.swofty.commons.BedwarsGameType;
import net.swofty.commons.service.FromServiceChannels;
import net.swofty.service.generic.redis.ServiceToServerManager;
import net.swofty.service.generic.redis.ServiceEndpoint;
import net.swofty.service.orchestrator.OrchestratorCache;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
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
		try {
			BedwarsGameType gameType = parseBedwarsGameType(body.mode());
			if (gameType == null) {
				return new GetServerForMapProtocolObject.GetServerForMapResponse(null, null);
			}

			// First, try to find an existing joinable game
			OrchestratorCache.GameWithServer existingGameWithServer = OrchestratorCache.findExisting(gameType, body.map());
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
					System.err.println("Failed to instantiate game: " + e.getMessage());
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
}
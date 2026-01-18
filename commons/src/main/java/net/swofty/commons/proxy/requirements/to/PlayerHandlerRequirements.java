package net.swofty.commons.proxy.requirements.to;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class PlayerHandlerRequirements extends ProxyChannelRequirements {
	@Override
	public List<RequiredKey> getRequiredKeysForProxy() {
		return List.of();
	}

	@Override
	public List<RequiredKey> getRequiredKeysForServer() {
		return List.of(
				new RequiredKey("uuid"), // The UUID of the player
				new RequiredKey("action") // see {@link PlayerHandlerActions}
		);
	}

	public enum PlayerHandlerActions {
		TRANSFER,
		TELEPORT,
		BANK_HASH,
		VERSION,
		IS_ONLINE,
		EVENT,
		REFRESH_COOP_DATA,
		MESSAGE,
		TRANSFER_WITH_UUID,
		GET_SERVER,
		LIMBO,
	}
}

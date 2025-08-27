package net.swofty.commons.proxy.requirements.from;

import net.swofty.commons.proxy.ProxyChannelRequirements;

import java.util.List;

public class BedWarsJoinPreferenceRequirements extends ProxyChannelRequirements {
	@Override
	public List<RequiredKey> getRequiredKeysForProxy() {
		return List.of(
				new RequiredKey("uuid"),
				new RequiredKey("mode"), // SOLO / DOUBLES...
				new RequiredKey("map")   // map id or empty string for random
		);
	}

	@Override
	public List<RequiredKey> getRequiredKeysForServer() {
		return List.of();
	}
}

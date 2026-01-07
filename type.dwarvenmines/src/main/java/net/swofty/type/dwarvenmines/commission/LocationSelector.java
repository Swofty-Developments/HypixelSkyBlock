package net.swofty.type.dwarvenmines.commission;

import net.swofty.type.skyblockgeneric.region.RegionType;

import java.util.Objects;
import java.util.Optional;

public final class LocationSelector {

	private static final LocationSelector ANY = new LocationSelector(null);
	private final RegionType region;

	private LocationSelector(RegionType region) {
		this.region = region;
	}

	public static LocationSelector any() {
		return ANY;
	}

	public static LocationSelector of(RegionType region) {
		Objects.requireNonNull(region);
		return new LocationSelector(region);
	}

	public boolean allows(RegionType region) {
		return this.region == null || this.region == region;
	}

	public boolean isAny() {
		return region == null;
	}

	public Optional<RegionType> getRegion() {
		return Optional.ofNullable(region);
	}

}

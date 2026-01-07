package net.swofty.type.dwarvenmines.commission;

import net.minestom.server.entity.Entity;
import net.minestom.server.item.Material;

import java.util.List;

public class Objective {
	public final ObjectiveType type;
	public final BlockTarget target;
	public final int amount;
	public final LocationSelector location;
	public final EventType event;

	public Objective(
			ObjectiveType type,
			BlockTarget target,
			int amount,
			LocationSelector location,
			EventType event
	) {
		this.type = type;
		this.target = target;
		this.amount = amount;
		this.location = location;
		this.event = event;
	}

	public enum BlockTarget {
		NONE,
		GOBLIN,
		GLACITE_WALKER,
		TREASURE_HOARDER,
		GOLDEN_GOBLIN,
		STAR_SENTRY,
		MITHRIL,
		TITANIUM;
	}
}

package net.swofty.type.bedwarsgame.user;

import lombok.Getter;

@Getter
public enum ExperienceCause {
	// WIN
	SOLO_DOUBLES_WIN(100),
	THREE_FOUR(50),
	DREAM_4V4_WIN(25),

	// KILL
	FIRST_KILL(5), // first non-final kill of EACH player
	FINAL_KILL(10),
	BED_BREAK(15),

	// PICKUP
	EMERALDS(3),
	DIAMONDS(2),

	// PLAYTIME
	TIME_PLAYED(15, false); // every 1 minute of playtime

	private final long experience;
	private final boolean boostable; // means if xp boosts (like 5% from tournament) can apply to this cause

	ExperienceCause(long experience) {
		this.experience = experience;
		this.boostable = true;
	}

	ExperienceCause(long experience, boolean boostable) {
		this.experience = experience;
		this.boostable = boostable;
	}

	public long calculateXp(long units) {
		if (units <= 1) return experience;
		return experience * units;
	}


	public String getFormattedName() {
		String[] parts = this.name().toLowerCase().split("_");
		StringBuilder formattedName = new StringBuilder();
		for (String part : parts) {
			formattedName.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1)).append(" ");
		}
		return formattedName.toString().trim();
	}

}

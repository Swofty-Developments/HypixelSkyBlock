package net.swofty.type.bedwarsgame.user;

import lombok.Getter;

// most likely not actual token values
@Getter
public enum TokenCause {
	// WIN
	WIN(40),

	// KILL
	FINAL_KILL(4),
	BED_BREAK(40),

	// PLAYTIME
	TIME_PLAYED(25); // every 1 minute of playtime

	private final long experience;

	TokenCause(long experience) {
		this.experience = experience;
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

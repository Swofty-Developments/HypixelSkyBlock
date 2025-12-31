package net.swofty.type.murdermysterygame.role;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public enum GameRole {
    MURDERER("Murderer", NamedTextColor.RED, "Kill all innocents without being caught"),
    DETECTIVE("Detective", NamedTextColor.BLUE, "Find and eliminate the murderer"),
    INNOCENT("Innocent", NamedTextColor.GREEN, "Survive and help identify the murderer"),
    ASSASSIN("Assassin", NamedTextColor.GOLD, "Eliminate your assigned target");

    private final String displayName;
    private final NamedTextColor color;
    private final String description;

    GameRole(String displayName, NamedTextColor color, String description) {
        this.displayName = displayName;
        this.color = color;
        this.description = description;
    }
}

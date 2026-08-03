package net.swofty.type.ravengardgeneric.hud;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RavengardHudState {
    public static final float VANILLA_HEARTS_HEALTH = 40.0f;

    private int health = 160;
    private int maxHealth = 160;
    private int stamina = 100;
    private int crowns;
    private int abilityOne;
    private int abilityTwo;
    private int maxStamina = 100;

    private String clock = "00:00";
    private int teamCount;
    private int portalCount;
    private int playerCount;
    private int killCount;
    private boolean dungeon;
    private boolean spectating;

    private java.util.UUID playerUuid;
    private double worldX;
    private double worldZ;
    private float yaw;
    private String location = "Ravenport";
    private String date = "";
    private String serverId = "";

    private RavengardMinimapArea area = RavengardMinimapArea.HUB;

    public String healthText() {
        return health + "/" + maxHealth;
    }

    public String staminaText() {
        return stamina + "/" + maxStamina;
    }
}

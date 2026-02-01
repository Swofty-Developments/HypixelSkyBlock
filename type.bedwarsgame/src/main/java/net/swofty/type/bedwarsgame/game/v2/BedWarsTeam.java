package net.swofty.type.bedwarsgame.game.v2;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.commons.game.team.SimpleGameTeam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class BedWarsTeam extends SimpleGameTeam {
    private final TeamKey teamKey;
    @Setter
    private boolean bedAlive = true;

    // upgrade name -> level
    private final Map<String, Integer> upgrades = new HashMap<>();

    // team traps (trap keys)
    private final List<String> traps = new ArrayList<>();

    public BedWarsTeam(TeamKey teamKey) {
        super(teamKey.name(), teamKey.getName(), teamKey.chatColor());
        this.teamKey = teamKey;
    }

    public boolean canRespawn() {
        return bedAlive;
    }

    /**
     * Gets the level of an upgrade.
     *
     * @return The upgrade level, or 0 if not purchased
     */
    public int getUpgradeLevel(String upgradeName) {
        return upgrades.getOrDefault(upgradeName, 0);
    }

    public void setUpgradeLevel(String upgradeName, int level) {
        upgrades.put(upgradeName, level);
    }

    public void destroyBed() {
        this.bedAlive = false;
    }

    public void addTrap(String trapKey) {
        traps.add(trapKey);
    }

    public void removeTrap(String trapKey) {
        traps.remove(trapKey);
    }

    public boolean hasTrap(String trapKey) {
        return traps.contains(trapKey);
    }

    public int getTrapCount() {
        return traps.size();
    }
}

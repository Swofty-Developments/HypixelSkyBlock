package net.swofty.dungeons.catacombs.boss;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CatacombsBosses {
    public static CatacombsBossEncounter watcher() {
        return encounter("WATCHER", "The Watcher", "Stalker",
                phase("WATCHER_SUMMONS", "Undead Collection", BossPhaseTrigger.ENTER_BOSS_ROOM, 0,
                        "Summons waves of undead enemies",
                        "Opens the blood portal once every summoned enemy is defeated"));
    }

    public static CatacombsBossEncounter bonzo() {
        return encounter("BONZO", "Bonzo", "New Necromancer",
                phase("BONZO_SHOWTIME", "Showtime", BossPhaseTrigger.ENTER_BOSS_ROOM, 0,
                        "Throws balloons",
                        "Summons undead performers"),
                phase("BONZO_UNDEAD", "Undead Finale", BossPhaseTrigger.BOSS_HEALTH_THRESHOLD, 0,
                        "Revives after the first defeat",
                        "Uses stronger balloon barrages"));
    }

    public static CatacombsBossEncounter scarf() {
        return encounter("SCARF", "Scarf", "Apprentice Necromancer",
                phase("SCARF_STUDENTS", "Undead Students", BossPhaseTrigger.ENTER_BOSS_ROOM, 0,
                        "Warrior, Priest, Mage, Archer, and Scarf must be fought as a group"),
                phase("SCARF_REVIVE", "Second Lesson", BossPhaseTrigger.ADDS_DEFEATED, 0,
                        "Revives defeated students",
                        "Ends when Scarf is defeated after the revive"));
    }

    public static CatacombsBossEncounter professor() {
        return encounter("PROFESSOR", "The Professor", "Professor",
                phase("PROFESSOR_GUARDIANS", "Guardian Experiment", BossPhaseTrigger.ENTER_BOSS_ROOM, 0,
                        "Four guardians protect the professor",
                        "Guardians must be defeated close together"),
                phase("PROFESSOR_GIANT", "Frenzy", BossPhaseTrigger.ADDS_DEFEATED, 0,
                        "Professor enters a giant guardian body",
                        "Arena floods during the final phase"));
    }

    public static CatacombsBossEncounter thorn() {
        return encounter("THORN", "Thorn", "Shaman Necromancer",
                phase("THORN_SPIRITS", "Spirit Animals", BossPhaseTrigger.ENTER_BOSS_ROOM, 0,
                        "Animal waves fill the arena",
                        "Spirit Bears drop Spirit Bows"),
                phase("THORN_SHOT", "Spirit Shot", BossPhaseTrigger.PLAYER_OBJECTIVE, 4,
                        "Players damage Thorn by shooting him with Spirit Bow charges"));
    }

    public static CatacombsBossEncounter livid() {
        return encounter("LIVID", "Livid", "Master Necromancer",
                phase("LIVID_CLONES", "Clone Ambush", BossPhaseTrigger.ENTER_BOSS_ROOM, 0,
                        "Multiple Livid clones spawn",
                        "Only the correct Livid must be killed to finish the fight"));
    }

    public static CatacombsBossEncounter sadan() {
        return encounter("SADAN", "Sadan", "Necromancer Lord",
                phase("SADAN_TERRACOTTAS", "Terracotta Army", BossPhaseTrigger.ENTER_BOSS_ROOM, 0,
                        "Survive the terracotta army"),
                phase("SADAN_GIANTS", "Giant Reanimation", BossPhaseTrigger.TIMER, 0,
                        "Ancient Giants awaken one by one"),
                phase("SADAN_FINAL", "Necromancer Lord", BossPhaseTrigger.ADDS_DEFEATED, 0,
                        "Sadan enters the arena after the giants fall"));
    }

    public static CatacombsBossEncounter witherLords(boolean masterMode) {
        CatacombsBossEncounter encounter = encounter("WITHER_LORDS", "The Wither Lords", "The Wither Lords",
                phase("MAXOR", "Maxor", BossPhaseTrigger.ENTER_BOSS_ROOM, 0,
                        "Complete crystal objectives",
                        "Damage Maxor after he is stunned"),
                phase("STORM", "Storm", BossPhaseTrigger.PHASE_COMPLETE, 0,
                        "Shelter from lightning",
                        "Use crusher objectives to expose Storm"),
                phase("GOLDOR", "Goldor", BossPhaseTrigger.PHASE_COMPLETE, 0,
                        "Advance through terminal sections",
                        "Complete devices while Goldor approaches"),
                phase("NECRON", "Necron", BossPhaseTrigger.PHASE_COMPLETE, 0,
                        "Final arena against Necron",
                        "Defeat Necron to complete normal Floor VII"));

        if (!masterMode) {
            return encounter;
        }

        return encounter("MASTER_WITHER_LORDS", "The Wither Lords", "The Wither Lords",
                phase("MAXOR", "Maxor", BossPhaseTrigger.ENTER_BOSS_ROOM, 0,
                        "Complete crystal objectives",
                        "Damage Maxor after he is stunned"),
                phase("STORM", "Storm", BossPhaseTrigger.PHASE_COMPLETE, 0,
                        "Shelter from lightning",
                        "Use crusher objectives to expose Storm"),
                phase("GOLDOR", "Goldor", BossPhaseTrigger.PHASE_COMPLETE, 0,
                        "Advance through terminal sections",
                        "Complete devices while Goldor approaches"),
                phase("NECRON", "Necron", BossPhaseTrigger.PHASE_COMPLETE, 0,
                        "Final arena against Necron"),
                phase("WITHER_KING", "Wither King", BossPhaseTrigger.PHASE_COMPLETE, 0,
                        "Master Mode adds the Wither King after Necron"));
    }

    private static CatacombsBossEncounter encounter(String id, String displayName, String status,
                                                    CatacombsBossPhase... phases) {
        return new CatacombsBossEncounter(id, displayName, status, List.of(phases));
    }

    private static CatacombsBossPhase phase(String id, String displayName, BossPhaseTrigger trigger,
                                            int triggerValue, String... mechanics) {
        return new CatacombsBossPhase(id, displayName, trigger, triggerValue, Set.of(mechanics));
    }
}

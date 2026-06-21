package net.swofty.type.skyblockgeneric.entity.mob.mobs.seacreature;

import java.util.List;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;
import net.swofty.type.skyblockgeneric.loottable.SkyBlockLootTable;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import org.jetbrains.annotations.Nullable;

/**
 * A sea creature is fully described by a {@link SeaCreatureProfile} — one
 * concrete class drives every variant. The thread-local handoff
 * ({@link #IN_CONSTRUCTION}) is the standard workaround for SkyBlockMob's
 * super-constructor calling abstract accessors before subclass field
 * assignment happens; without it those accessors would NPE on the profile
 * field. Once super() returns the field is set and future calls hit the
 * fast path.
 */
public final class SeaCreatureMob extends SkyBlockMob {

    private static final ThreadLocal<SeaCreatureProfile> IN_CONSTRUCTION = new ThreadLocal<>();

    private final SeaCreatureProfile profile;

    private SeaCreatureMob(SeaCreatureProfile profile) {
        super(profile.entityType());
        this.profile = profile;
    }

    public static SeaCreatureMob create(SeaCreatureProfile profile) {
        IN_CONSTRUCTION.set(profile);
        try {
            return new SeaCreatureMob(profile);
        } finally {
            IN_CONSTRUCTION.remove();
        }
    }

    public String getSeaCreatureId() {
        return live().id();
    }

    public SeaCreatureProfile getProfile() {
        return live();
    }

    @Override public String getDisplayName() { return live().displayName(); }
    @Override public Integer getLevel() { return live().level(); }
    @Override public List<MobType> getMobTypes() { return live().mobTypes(); }
    @Override public ItemStatistics getBaseStatistics() { return live().asBaseStatistics(); }
    @Override public OtherLoot getOtherLoot() { return live().asOtherLoot(); }
    @Override public long damageCooldown() { return live().damageCooldownMs(); }
    @Override public @Nullable SkyBlockLootTable getLootTable() { return live().lootTable(); }
    @Override public SkillCategories getSkillCategory() { return SkillCategories.FISHING; }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        return live().behaviour().goals(this);
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return live().behaviour().targets(this);
    }

    private SeaCreatureProfile live() {
        return profile != null ? profile : IN_CONSTRUCTION.get();
    }
}

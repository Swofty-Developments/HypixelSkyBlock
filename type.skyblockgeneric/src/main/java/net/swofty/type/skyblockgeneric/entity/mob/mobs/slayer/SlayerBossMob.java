package net.swofty.type.skyblockgeneric.entity.mob.mobs.slayer;

import java.util.List;
import java.util.UUID;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.entity.mob.MobType;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.loottable.OtherLoot;
import net.swofty.type.skyblockgeneric.loottable.SkyBlockLootTable;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import org.jetbrains.annotations.Nullable;

public final class SlayerBossMob extends SkyBlockMob {
    private static final ThreadLocal<SlayerBossProfile> IN_CONSTRUCTION = new ThreadLocal<>();

    private final SlayerBossProfile profile;
    private final UUID ownerUuid;

    private SlayerBossMob(UUID ownerUuid, SlayerBossProfile profile) {
        super(profile.entityType());
        this.ownerUuid = ownerUuid;
        this.profile = profile;
    }

    public static SlayerBossMob create(UUID ownerUuid, SlayerBossProfile profile) {
        IN_CONSTRUCTION.set(profile);
        try {
            return new SlayerBossMob(ownerUuid, profile);
        } finally {
            IN_CONSTRUCTION.remove();
        }
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public SlayerBossProfile getProfile() {
        return live();
    }

    @Override public String getDisplayName() { return live().displayName(); }
    @Override public Integer getLevel() { return live().tier().bossLevel(); }
    @Override public List<MobType> getMobTypes() { return live().mobTypes(); }
    @Override public ItemStatistics getBaseStatistics() { return live().asBaseStatistics(); }
    @Override public OtherLoot getOtherLoot() { return live().asOtherLoot(); }
    @Override public long damageCooldown() { return 500L; }
    @Override public @Nullable SkyBlockLootTable getLootTable() { return live().asLootTable(); }
    @Override public SkillCategories getSkillCategory() { return SkillCategories.COMBAT; }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        return SlayerBossBehaviour.goals(this);
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return SlayerBossBehaviour.targets(this);
    }

    private SlayerBossProfile live() {
        return profile != null ? profile : IN_CONSTRUCTION.get();
    }
}

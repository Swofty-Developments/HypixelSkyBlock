package net.swofty.type.bedwarsgame.death;

import net.minestom.server.entity.Entity;
import net.minestom.server.item.Material;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record BedWarsDeathResult(
        @NotNull BedWarsDeathType deathType,
        @NotNull BedWarsPlayer victim,
        @Nullable BedWarsPlayer killer,
        @Nullable BedWarsPlayer assistPlayer,
        @Nullable Entity attackerEntity,
        @Nullable Material weaponUsed,
        boolean isFinalKill
) {
    @Nullable
    public BedWarsPlayer getKillCreditPlayer() {
        return killer != null ? killer : assistPlayer;
    }

    public boolean hasPlayerInvolvement() {
        return killer != null || assistPlayer != null;
    }

    public boolean isDirectKill() {
        return killer != null && assistPlayer == null;
    }

    public boolean isAssistedKill() {
        return assistPlayer != null && killer == null;
    }

    public boolean wasKilledWith(Material material) {
        return weaponUsed != null && weaponUsed == material;
    }

    public static class Builder {
        private BedWarsDeathType deathType = BedWarsDeathType.GENERIC;
        private BedWarsPlayer victim;
        private BedWarsPlayer killer;
        private BedWarsPlayer assistPlayer;
        private Entity attackerEntity;
        private Material weaponUsed;
        private boolean isFinalKill;

        public Builder victim(@NotNull BedWarsPlayer victim) {
            this.victim = victim;
            return this;
        }

        public Builder deathType(@NotNull BedWarsDeathType deathType) {
            this.deathType = deathType;
            return this;
        }

        public Builder killer(@Nullable BedWarsPlayer killer) {
            this.killer = killer;
            return this;
        }

        public Builder assistPlayer(@Nullable BedWarsPlayer assistPlayer) {
            this.assistPlayer = assistPlayer;
            return this;
        }

        public Builder attackerEntity(@Nullable Entity attackerEntity) {
            this.attackerEntity = attackerEntity;
            return this;
        }

        public Builder weaponUsed(@Nullable Material weaponUsed) {
            this.weaponUsed = weaponUsed;
            return this;
        }

        public Builder isFinalKill(boolean isFinalKill) {
            this.isFinalKill = isFinalKill;
            return this;
        }

        public BedWarsDeathResult build() {
            if (victim == null) {
                throw new IllegalStateException("Victim must be set");
            }
            return new BedWarsDeathResult(deathType, victim, killer, assistPlayer, attackerEntity, weaponUsed, isFinalKill);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}


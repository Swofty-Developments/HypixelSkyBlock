package net.swofty.type.skyblockgeneric.user.statistics;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class TemporaryStatistic {
    private ItemStatistics statistics;
    private long expiration;
    // Optional display info for tablist
    private @Nullable String displayName;
    private @Nullable String displayColor;

    public TemporaryStatistic(ItemStatistics statistics, long expiration) {
        this.statistics = statistics;
        this.expiration = expiration;
    }

    public TemporaryStatistic(ItemStatistics statistics, long expiration, String displayName, String displayColor) {
        this.statistics = statistics;
        this.expiration = expiration;
        this.displayName = displayName;
        this.displayColor = displayColor;
    }

    /**
     * Get remaining duration in milliseconds
     */
    public long getRemainingMs() {
        return Math.max(0, expiration - System.currentTimeMillis());
    }

    /**
     * Check if this statistic has display info for tablist
     */
    public boolean hasDisplayInfo() {
        return displayName != null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ItemStatistics statistics;
        private long expiration;
        private String displayName;
        private String displayColor;

        public Builder withStatistics(ItemStatistics statistics) {
            this.statistics = statistics;
            return this;
        }

        public Builder withExpirationInMs(long expiration) {
            this.expiration = System.currentTimeMillis() + (expiration);
            return this;
        }

        public Builder withExpirationInTicks(long expiration) {
            this.expiration = System.currentTimeMillis() + (expiration * 50);
            return this;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder withDisplayColor(String displayColor) {
            this.displayColor = displayColor;
            return this;
        }

        public TemporaryStatistic build() {
            return new TemporaryStatistic(statistics, expiration, displayName, displayColor);
        }
    }
}

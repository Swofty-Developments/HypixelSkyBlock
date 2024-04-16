package net.swofty.types.generic.user.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemporaryStatistic {
    private ItemStatistics statistics;
    private long expiration;

    public TemporaryStatistic(ItemStatistics statistics, long expiration) {
        this.statistics = statistics;
        this.expiration = expiration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ItemStatistics statistics;
        private long expiration;

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

        public TemporaryStatistic build() {
            return new TemporaryStatistic(statistics, expiration);
        }
    }
}

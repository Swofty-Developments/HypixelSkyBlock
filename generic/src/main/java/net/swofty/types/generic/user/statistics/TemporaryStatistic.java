package net.swofty.types.generic.user.statistics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemporaryStatistic {
    private ItemStatistic statistic;
    private Double value;
    private long expiration;

    public TemporaryStatistic(ItemStatistic statistic, Double value, long expiration) {
        this.statistic = statistic;
        this.value = value;
        this.expiration = expiration;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ItemStatistic statistic;
        private Double value;
        private long expiration;

        public Builder withStatistic(ItemStatistic statistic) {
            this.statistic = statistic;
            return this;
        }

        public Builder withValue(Double value) {
            this.value = value;
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
            return new TemporaryStatistic(statistic, value, expiration);
        }
    }
}

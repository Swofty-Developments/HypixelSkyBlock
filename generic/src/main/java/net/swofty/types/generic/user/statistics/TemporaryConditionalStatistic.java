package net.swofty.types.generic.user.statistics;

import lombok.Getter;
import lombok.Setter;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.function.Function;

@Getter
@Setter
public class TemporaryConditionalStatistic {
    private ItemStatistic statistic;
    private Double value;
    private Function<SkyBlockPlayer, Boolean> expiry;

    public TemporaryConditionalStatistic(ItemStatistic statistic, Double value, Function<SkyBlockPlayer, Boolean> expiry) {
        this.statistic = statistic;
        this.value = value;
        this.expiry = expiry;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ItemStatistic statistic;
        private Double value;
        private Function<SkyBlockPlayer, Boolean> expiry;

        public Builder withStatistic(ItemStatistic statistic) {
            this.statistic = statistic;
            return this;
        }

        public Builder withValue(Double value) {
            this.value = value;
            return this;
        }

        // When returning false, the statistic will be removed from the player
        public Builder withExpiry(Function<SkyBlockPlayer, Boolean> expiry) {
            this.expiry = expiry;
            return this;
        }

        public TemporaryConditionalStatistic build() {
            return new TemporaryConditionalStatistic(statistic, value, expiry);
        }
    }
}

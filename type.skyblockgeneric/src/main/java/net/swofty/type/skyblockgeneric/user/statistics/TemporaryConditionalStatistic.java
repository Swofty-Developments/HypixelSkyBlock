package net.swofty.type.skyblockgeneric.user.statistics;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.statistics.ItemStatistics;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.function.Function;

@Getter
@Setter
public class TemporaryConditionalStatistic {
    private Function<HypixelPlayer, ItemStatistics>  statistics;
    private Function<HypixelPlayer, Boolean> expiry;

    public TemporaryConditionalStatistic(Function<HypixelPlayer, ItemStatistics>  statistics, Function<HypixelPlayer, Boolean> expiry) {
        this.statistics = statistics;
        this.expiry = expiry;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Function<HypixelPlayer, ItemStatistics>  statistics;
        private Function<HypixelPlayer, Boolean> expiry;

        public Builder withStatistics(Function<HypixelPlayer, ItemStatistics> statistics) {
            this.statistics = statistics;
            return this;
        }

        // When returning false, the statistic will be removed from the player
        public Builder withExpiry(Function<HypixelPlayer, Boolean> expiry) {
            this.expiry = expiry;
            return this;
        }

        public TemporaryConditionalStatistic build() {
            return new TemporaryConditionalStatistic(statistics, expiry);
        }
    }
}

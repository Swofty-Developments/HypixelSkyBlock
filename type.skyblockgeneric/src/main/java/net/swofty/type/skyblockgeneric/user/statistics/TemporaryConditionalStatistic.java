package net.swofty.type.skyblockgeneric.user.statistics;

import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.function.Function;

@Getter
@Setter
public class TemporaryConditionalStatistic {
    private Function<SkyBlockPlayer, ItemStatistics>  statistics;
    private Function<SkyBlockPlayer, Boolean> expiry;

    public TemporaryConditionalStatistic(Function<SkyBlockPlayer, ItemStatistics>  statistics, Function<SkyBlockPlayer, Boolean> expiry) {
        this.statistics = statistics;
        this.expiry = expiry;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Function<SkyBlockPlayer, ItemStatistics>  statistics;
        private Function<SkyBlockPlayer, Boolean> expiry;

        public Builder withStatistics(Function<SkyBlockPlayer, ItemStatistics> statistics) {
            this.statistics = statistics;
            return this;
        }

        // When returning false, the statistic will be removed from the player
        public Builder withExpiry(Function<SkyBlockPlayer, Boolean> expiry) {
            this.expiry = expiry;
            return this;
        }

        public TemporaryConditionalStatistic build() {
            return new TemporaryConditionalStatistic(statistics, expiry);
        }
    }
}

package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointAuctionStatistics extends Datapoint<DatapointAuctionStatistics.AuctionStatistics> {

    public DatapointAuctionStatistics(String key, DatapointAuctionStatistics.AuctionStatistics value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(DatapointAuctionStatistics.AuctionStatistics value) {
                StringBuilder sb = new StringBuilder();

                if (value.getStatistics().isEmpty())
                    return "{}";

                sb.append("{");
                for (Map.Entry<AuctionStatistics.AuctionStat, Integer> entry : value.getStatistics().entrySet()) {
                    sb.append("\"").append(entry.getKey().name()).append("\":").append(entry.getValue()).append(",");
                }

                sb.deleteCharAt(sb.length() - 1);
                sb.append("}");

                return sb.toString();
            }

            @Override
            public DatapointAuctionStatistics.AuctionStatistics deserialize(String json) {
                JSONObject obj = new JSONObject(json);
                Map<AuctionStatistics.AuctionStat, Integer> map = new HashMap<>();

                if (obj.keySet().isEmpty())
                    return new AuctionStatistics(map);

                for (String key : obj.keySet()) {
                    map.put(AuctionStatistics.AuctionStat.valueOf(key), obj.getInt(key));
                }

                return new AuctionStatistics(map);
            }

            @Override
            public DatapointAuctionStatistics.AuctionStatistics clone(DatapointAuctionStatistics.AuctionStatistics value) {
                return new AuctionStatistics(value.getStatistics());
            }
        });
    }

    public DatapointAuctionStatistics(String key) {
        this(key, new AuctionStatistics(new HashMap<>()));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class AuctionStatistics {
        private Map<AuctionStat, Integer> statistics;

        public Integer get(AuctionStat stat) {
            if (!statistics.containsKey(stat))
                return 0;
            return statistics.get(stat);
        }

        public void increment(AuctionStat stat) {
            statistics.put(stat, get(stat) + 1);
        }

        public void set(AuctionStat stat, int value) {
            statistics.put(stat, value);
        }

        public enum AuctionStat {
            // Seller Stats
            AUCTIONS_CREATED,
            AUCTIONS_COMPLETED_WITH_BIDS,
            AUCTIONS_COMPLETED_WITHOUT_BIDS,
            HIGHEST_AUCTION_HELD,
            TOTAL_COINS_EARNED,
            COINS_SPENT_ON_FEES,
            COMMON_SOLD,
            UNCOMMON_SOLD,
            RARE_SOLD,
            EPIC_SOLD,
            LEGENDARY_SOLD,
            MYTHIC_SOLD,
            SPECIAL_SOLD,
            ULTIMATE_SOLD,

            // Buyer Stats
            AUCTIONS_WON,
            TOTAL_BIDS,
            HIGHEST_BID_MADE,
            TOTAL_COINS_SPENT,
            COMMON_BOUGHT,
            UNCOMMON_BOUGHT,
            RARE_BOUGHT,
            EPIC_BOUGHT,
            LEGENDARY_BOUGHT,
            MYTHIC_BOUGHT,
            SPECIAL_BOUGHT,
            ULTIMATE_BOUGHT,
            ;
        }
    }
}

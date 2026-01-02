package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;
import org.json.JSONObject;

public class DatapointFriendSort extends Datapoint<DatapointFriendSort.FriendSortData> {
    private static final Serializer<FriendSortData> serializer = new Serializer<FriendSortData>() {
        @Override
        public String serialize(FriendSortData value) {
            JSONObject json = new JSONObject();
            json.put("sortType", value.sortType.name());
            json.put("reversed", value.reversed);
            return json.toString();
        }

        @Override
        public FriendSortData deserialize(String json) {
            JSONObject obj = new JSONObject(json);
            SortType sortType = SortType.valueOf(obj.getString("sortType"));
            boolean reversed = obj.getBoolean("reversed");
            return new FriendSortData(sortType, reversed);
        }

        @Override
        public FriendSortData clone(FriendSortData value) {
            return new FriendSortData(value.sortType, value.reversed);
        }
    };

    public DatapointFriendSort(String key, FriendSortData value) {
        super(key, value, serializer);
    }

    public DatapointFriendSort(String key) {
        super(key, null, serializer);
    }

    public static class FriendSortData {
        public SortType sortType;
        public boolean reversed;

        public FriendSortData(SortType sortType, boolean reversed) {
            this.sortType = sortType;
            this.reversed = reversed;
        }

        public void cycleSortType() {
            SortType[] values = SortType.values();
            int nextIndex = (sortType.ordinal() + 1) % values.length;
            this.sortType = values[nextIndex];
        }

        public void toggleReversed() {
            this.reversed = !this.reversed;
        }
    }

    public enum SortType {
        DEFAULT,      // Online first, then alphabetical
        ALPHABETICAL, // A-Z by name
        LAST_ONLINE   // Most recently online first
    }
}

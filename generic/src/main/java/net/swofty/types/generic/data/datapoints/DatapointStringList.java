package net.swofty.types.generic.data.datapoints;

import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatapointStringList extends Datapoint<List<String>> {

    public DatapointStringList(String key, ArrayList<String> value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(List<String> value) {
                ArrayList<String> list = new ArrayList<>(value.size());
                list.addAll(value);
                return String.join(",", list);
            }

            @Override
            public List<String> deserialize(String json) {
                String[] split = json.split(",");
                ArrayList<String> list = new ArrayList<>(split.length);
                list.addAll(Arrays.asList(split));
                return list;
            }

            @Override
            public List<String> clone(List<String> value) {
                return new ArrayList<>(value);
            }
        });
    }

    public DatapointStringList(String key) {
        this(key, new ArrayList<>());
    }
}

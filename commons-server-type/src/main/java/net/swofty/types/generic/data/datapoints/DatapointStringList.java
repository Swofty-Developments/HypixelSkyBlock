package net.swofty.types.generic.data.datapoints;

import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.serializer.Serializer;

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
        });
    }

    public DatapointStringList(String key) {
        this(key, new ArrayList<>());
    }

    public void add(String value) {
        List<String> current = getValue();
        current.add(value);
        setValue(current);
    }

    public void remove(String value) {
        List<String> current = getValue();
        current.remove(value);
        setValue(current);
    }

    public boolean has(String value) {
        return getValue().contains(value);
    }

    public boolean hasOrAdd(String value) {
        if (has(value))
            return false;
        add(value);
        return true;
    }
}

package net.swofty.types.generic.data.datapoints;

import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatapointUUIDList extends Datapoint<List<UUID>> {
    public DatapointUUIDList(String key, ArrayList<UUID> value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(List<UUID> value) {
                ArrayList<String> list = new ArrayList<>(value.size());
                for (UUID i : value)
                    list.add(i.toString());
                return String.join(",", list);
            }

            @Override
            public List<UUID> deserialize(String json) {
                if (json.isEmpty())
                    return new ArrayList<>();

                String[] split = json.split(",");
                ArrayList<UUID> list = new ArrayList<>(split.length);
                for (String s : split)
                    list.add(UUID.fromString(s));
                return list;
            }

            @Override
            public List<UUID> clone(List<UUID> value) {
                return new ArrayList<>(value);
            }
        });
    }

    public DatapointUUIDList(String key) {
        this(key, new ArrayList<>());
    }

    public void add(UUID value) {
        List<UUID> current = getValue();
        current.add(value);
        setValue(current);
    }

    public void remove(UUID value) {
        List<UUID> current = getValue();
        current.remove(value);
        setValue(current);
    }

    public boolean has(UUID value) {
        return getValue().contains(value);
    }
}

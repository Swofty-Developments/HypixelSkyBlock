package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointPresentYear extends Datapoint<DatapointPresentYear.YearData> {
    private static final JacksonSerializer<DatapointPresentYear.YearData> serializer =
            new JacksonSerializer<>((Class) DatapointPresentYear.YearData.class);

    public DatapointPresentYear(String key, YearData value) {
        super(key, value, serializer);
    }

    public DatapointPresentYear(String key) {
        super(key, new YearData(0, List.of()), serializer);
    }

    public record YearData(int year, List<Integer> value) {

    }
}

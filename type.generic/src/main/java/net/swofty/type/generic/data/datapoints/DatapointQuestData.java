package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.quest.QuestData;

public class DatapointQuestData extends Datapoint<QuestData> {
    private static final JacksonSerializer<QuestData> serializer = new JacksonSerializer<>(QuestData.class);

    public DatapointQuestData(String key, QuestData value) {
        super(key, value != null ? value : new QuestData(), serializer);
    }

    public DatapointQuestData(String key) {
        super(key, new QuestData(), serializer);
    }
}

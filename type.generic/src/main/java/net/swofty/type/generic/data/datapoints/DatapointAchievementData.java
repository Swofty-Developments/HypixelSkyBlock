package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.achievement.AchievementData;
import net.swofty.type.generic.data.Datapoint;

public class DatapointAchievementData extends Datapoint<AchievementData> {
    private static final JacksonSerializer<AchievementData> serializer = new JacksonSerializer<>(AchievementData.class);

    public DatapointAchievementData(String key, AchievementData value) {
        super(key, value != null ? value : new AchievementData(), serializer);
    }

    public DatapointAchievementData(String key) {
        super(key, new AchievementData(), serializer);
    }
}

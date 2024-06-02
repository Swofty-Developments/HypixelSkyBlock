package net.swofty.types.generic.data.datapoints;

import net.swofty.service.protocol.JacksonSerializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.skill.SkillCategories;

public class DatapointSkillCategory extends Datapoint<SkillCategories> {
    private static final JacksonSerializer<SkillCategories> serializer = new JacksonSerializer<>(SkillCategories.class);

    public DatapointSkillCategory(String key, SkillCategories value) {
        super(key, value, serializer);
    }

    public DatapointSkillCategory(String key) {
        super(key, null, serializer);
    }
}
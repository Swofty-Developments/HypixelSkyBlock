package net.swofty.type.generic.entity.npc.configuration;

import net.minestom.server.entity.EntityType;

public abstract class AnimalConfiguration implements NPCConfiguration {

    public abstract EntityType entityType();

    public abstract float hologramYOffset();

}

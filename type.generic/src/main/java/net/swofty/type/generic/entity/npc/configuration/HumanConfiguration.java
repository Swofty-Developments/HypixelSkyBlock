package net.swofty.type.generic.entity.npc.configuration;

import net.swofty.type.generic.user.HypixelPlayer;

public abstract class HumanConfiguration implements NPCConfiguration {

    public abstract String texture(HypixelPlayer player);

    public abstract String signature(HypixelPlayer player);

}

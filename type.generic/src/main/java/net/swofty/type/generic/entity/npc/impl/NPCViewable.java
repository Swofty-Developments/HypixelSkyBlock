package net.swofty.type.generic.entity.npc.impl;

import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;

public interface NPCViewable {

    List<HypixelPlayer> getInRangeOf();

    default void updateNPC() {

    }

}

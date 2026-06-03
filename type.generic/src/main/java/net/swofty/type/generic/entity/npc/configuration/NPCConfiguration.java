package net.swofty.type.generic.entity.npc.configuration;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityPose;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface NPCConfiguration {

    String[] holograms(HypixelPlayer player);

    Pos position(HypixelPlayer player);

    default boolean looking(HypixelPlayer player) {
        return false;
    }

    default boolean visible(HypixelPlayer player) {
        return true;
    }

    @Nullable
    default String chatName() {
        return null;
    }

    default Instance instance() {
        return HypixelConst.getInstanceContainer();
    }

    default EntityPose pose(HypixelPlayer player) {
        return EntityPose.STANDING;
    }

    default boolean shouldDisplayHolograms(HypixelPlayer player) {
        return true;
    }

    @Nullable
    default Map<EquipmentSlot, ItemStack> equipment(HypixelPlayer player) {
        return null;
    }
}

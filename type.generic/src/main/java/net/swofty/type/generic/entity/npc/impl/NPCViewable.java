package net.swofty.type.generic.entity.npc.impl;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.EntityEquipmentPacket;
import net.minestom.server.utils.validate.Check;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;
import java.util.Map;

public interface NPCViewable {

    List<HypixelPlayer> getInRangeOf();

    default void updateNPC() {

    }

    default void syncEntityEquipment(EquipmentSlot slot, ItemStack stack) {
        Check.stateCondition(!(this instanceof Entity), "Only accessible for Entity");

        Entity entity = (Entity) this;
        entity.sendPacketToViewers(new EntityEquipmentPacket(entity.getEntityId(), Map.of(slot, stack)));
    }

}

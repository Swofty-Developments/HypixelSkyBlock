package net.swofty.type.skyblockgeneric.event.actions.custom.skill;

import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributePetData;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.SkillUpdateEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.PetComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionSkillPetLevel implements HypixelEventClass {

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void run(SkillUpdateEvent event) {
        if (event.getNewValueRaw() <= event.getOldValueRaw()) return;
        double xp = event.getNewValueRaw() - event.getOldValueRaw();
        if (xp <= 0) return;

        SkyBlockPlayer player = event.getPlayer();
        SkyBlockItem enabledPet = player.getPetData().getEnabledPet();

        if (enabledPet == null) return;
        PetComponent pet = enabledPet.getComponent(PetComponent.class);
        if (pet.getSkillCategory() != event.getSkillCategory()) return;

        ItemAttributePetData.PetData petData = enabledPet.getAttributeHandler().getPetData();
        boolean hasLeveled = petData.addExperience(xp, enabledPet.getAttributeHandler().getRarity());

        if (hasLeveled) {
            player.sendMessage("§aYour " + enabledPet.getAttributeHandler().getRarity().getColor() + pet.getPetName() + " Pet §alevelled up to level §9" + petData.getAsLevel(enabledPet.getAttributeHandler().getRarity()) + "§a!");
        }
    }
}

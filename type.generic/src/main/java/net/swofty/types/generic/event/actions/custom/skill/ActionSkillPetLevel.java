package net.swofty.types.generic.event.actions.custom.skill;

import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.custom.SkillUpdateEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributePetData;
import net.swofty.types.generic.item.impl.Pet;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionSkillPetLevel implements SkyBlockEventClass {

    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = false)
    public void run(SkillUpdateEvent event) {
        if (event.getNewValueRaw() <= event.getOldValueRaw()) return;
        double xp = event.getNewValueRaw() - event.getOldValueRaw();
        if (xp <= 0) return;

        SkyBlockPlayer player = event.getPlayer();
        SkyBlockItem enabledPet = player.getPetData().getEnabledPet();

        if (enabledPet == null) return;
        Pet pet = (Pet) enabledPet.getGenericInstance();
        if (pet.getSkillCategory() != event.getSkillCategory()) return;

        ItemAttributePetData.PetData petData = enabledPet.getAttributeHandler().getPetData();
        boolean hasLeveled = petData.addExperience(xp, enabledPet.getAttributeHandler().getRarity());

        if (hasLeveled) {
            player.sendMessage("§aYour " + enabledPet.getAttributeHandler().getRarity().getColor() + pet.getPetName() + " Pet §alevelled up to level §9" + petData.getAsLevel(enabledPet.getAttributeHandler().getRarity()) + "§a!");
        }
    }
}

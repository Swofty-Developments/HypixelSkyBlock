package net.swofty.types.generic.event.actions.custom.skill;

import net.minestom.server.event.Event;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.SkillUpdateEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributePetData;
import net.swofty.types.generic.item.impl.Pet;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class ActionSkillPetLevel extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return SkillUpdateEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        SkillUpdateEvent event = (SkillUpdateEvent) tempEvent;
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

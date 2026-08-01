package net.swofty.type.skyblockgeneric.event.actions.custom.skill;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributePetData;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.event.custom.SkillUpdateEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.PetComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionSkillPetLevel implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
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
            player.sendMessage(
                    Component.text("Your ", NamedTextColor.GREEN)
                            .append(Component.text(
                                    pet.getPetName() + " Pet ",
                                    enabledPet.getAttributeHandler().getRarity().getColor()
                            ))
                            .append(Component.text("levelled up to level ", NamedTextColor.GREEN))
                            .append(Component.text(
                                    petData.getAsLevel(enabledPet.getAttributeHandler().getRarity()),
                                    NamedTextColor.BLUE
                            ))
                            .append(Component.text("!", NamedTextColor.GREEN))
            );
        }
    }
}

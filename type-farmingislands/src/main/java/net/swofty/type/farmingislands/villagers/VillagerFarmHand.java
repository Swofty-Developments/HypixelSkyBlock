package net.swofty.type.farmingislands.villagers;

import net.swofty.types.generic.entity.villager.NPCVillagerDialogue;
import net.swofty.types.generic.entity.villager.NPCVillagerParameters;

public class VillagerFarmHand extends NPCVillagerDialogue {
    protected VillagerFarmHand(NPCVillagerParameters parameters) {
        super(parameters);
    }

    @Override
    public DialogueSet[] getDialogueSets() {
        return new DialogueSet[0];
    }

    @Override
    public void onClick(PlayerClickVillagerNPCEvent e) {

    }
}

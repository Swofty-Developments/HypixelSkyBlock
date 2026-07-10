package net.swofty.type.skyblockgeneric.event.actions.custom.collection;

import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.phase.EventPhase;
import net.swofty.type.generic.event.phase.PhasedEvent;
import net.swofty.type.skyblockgeneric.collection.CollectionCategories;
import net.swofty.type.skyblockgeneric.collection.CollectionCategory;
import net.swofty.type.skyblockgeneric.event.custom.CollectionUpdateEvent;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelCause;

public class ActionCollectionHypixelLevel implements HypixelEventClass {


    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(CollectionUpdateEvent event) {
        if (CollectionCategories.getCategory(event.getItemType()) == null) return;

        CollectionCategory.ItemCollection collection = CollectionCategories.getCategory(event.getItemType()).getCollection(event.getItemType());
        int newValue = event.getPlayer().getCollection().get(event.getItemType());
        for (CollectionCategory.ItemCollectionReward reward : collection.rewards()) {
            if (event.getOldValue() < reward.requirement() && newValue >= reward.requirement()) {
                event.getPlayer().getSkyBlockExperience().addExperience(
                        SkyBlockLevelCause.getCollectionCause(event.getItemType(), collection.getPlacementOf(reward))
                );
            }
        }
    }
}

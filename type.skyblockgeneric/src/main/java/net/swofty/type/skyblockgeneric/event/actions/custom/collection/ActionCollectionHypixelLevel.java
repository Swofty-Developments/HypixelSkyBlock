package net.swofty.type.skyblockgeneric.event.actions.custom.collection;

import net.swofty.type.generic.collection.CollectionCategories;
import net.swofty.type.generic.collection.CollectionCategory;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.custom.CollectionUpdateEvent;
import net.swofty.type.generic.levels.SkyBlockLevelCause;

public class ActionCollectionHypixelLevel implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(CollectionUpdateEvent event) {
        if (CollectionCategories.getCategory(event.getItemType()) == null) return;

        CollectionCategory.ItemCollection collection = CollectionCategories.getCategory(event.getItemType()).getCollection(event.getItemType());
        CollectionCategory.ItemCollectionReward newReward = event.getPlayer().getCollection().getReward(collection);
        CollectionCategory.ItemCollectionReward oldReward = null;

        for (CollectionCategory.ItemCollectionReward reward : collection.rewards()) {
            if (event.getOldValue() <= reward.requirement()) {
                oldReward = reward;
                break;
            }
        }

        if (oldReward == newReward) return;

        if (newReward != null) {
            event.getPlayer().getSkyBlockExperience().addExperience(
                    SkyBlockLevelCause.getCollectionCause(event.getItemType(), collection.getPlacementOf(newReward))
            );
        }
    }
}

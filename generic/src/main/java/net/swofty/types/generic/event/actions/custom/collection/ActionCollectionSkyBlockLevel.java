package net.swofty.types.generic.event.actions.custom.collection;

import net.minestom.server.event.Event;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.custom.CollectionUpdateEvent;
import net.swofty.types.generic.levels.SkyBlockLevelCause;

@EventParameters(description = "Handles the adding of SkyBlock XP in relation to collections",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class ActionCollectionSkyBlockLevel extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return CollectionUpdateEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        CollectionUpdateEvent event = (CollectionUpdateEvent) tempEvent;

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

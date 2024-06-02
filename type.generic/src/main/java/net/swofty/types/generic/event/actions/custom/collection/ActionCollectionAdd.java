package net.swofty.types.generic.event.actions.custom.collection;

import net.swofty.proxyapi.ProxyPlayerSet;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockGenericLoader;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointCollection;
import net.swofty.types.generic.data.mongodb.CoopDatabase;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.CollectionUpdateEvent;
import net.swofty.types.generic.event.custom.CustomBlockBreakEvent;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;
import net.swofty.types.generic.utility.MathUtility;

public class ActionCollectionAdd implements SkyBlockEventClass {


    @SkyBlockEvent(node = EventNodes.CUSTOM , requireDataLoaded = true)
    public void run(CustomBlockBreakEvent event) {
        if (SkyBlockConst.isIslandServer()) return;

        SkyBlockPlayer player = event.getPlayer();
        ItemType type = ItemType.fromMaterial(event.getMaterial());

        if (type == null) return;
        int oldAmount = player.getCollection().get(type);
        player.getCollection().increase(type);

        SkyBlockEventHandler.callSkyBlockEvent(new CollectionUpdateEvent(player, type, oldAmount));

        player.getDataHandler().get(DataHandler.Data.COLLECTION, DatapointCollection.class).setValue(
                player.getCollection()
        );

        if (player.isCoop()) {
            CoopDatabase.Coop coop = player.getCoop();

            coop.getOnlineMembers().forEach(member -> {
                if (member.getUuid().equals(player.getUuid())) return;
                SkyBlockEventHandler.callSkyBlockEvent(new CollectionUpdateEvent(member, type, oldAmount));
            });

            coop.members().removeIf(
                    uuid -> SkyBlockGenericLoader.getFromUUID(uuid) != null
            );

            ProxyPlayerSet proxyPlayerSet = new ProxyPlayerSet(coop.members());
            proxyPlayerSet.asProxyPlayers().forEach(proxyPlayer -> {
                if (!proxyPlayer.isOnline().join()) return;

                proxyPlayer.runEvent(new CollectionUpdateEvent(null, type, oldAmount));
            });
        }

        CollectionCategory category = CollectionCategories.getCategory(type);
        if (category == null) return;
        CollectionCategory.ItemCollection collection = category.getCollection(type);

        MathUtility.delay(() -> {
            if (player.getDefenseDisplayReplacement() != null) {
                // Allow for skill display to override collection displays
                StatisticDisplayReplacement.Purpose purpose = player.getDefenseDisplayReplacement().getPurpose();
                if (purpose != null && purpose.equals(StatisticDisplayReplacement.Purpose.SKILL)) return;

                String addedAmountString = player.getDefenseDisplayReplacement().getDisplay();
                int addedAmount = 1;

                try {
                    addedAmount = Integer.parseInt(addedAmountString.substring(2, addedAmountString.indexOf(" "))) + 1;
                } catch (NumberFormatException ignored) {}

                player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                        .ticksToLast(20)
                        .purpose(StatisticDisplayReplacement.Purpose.COLLECTION)
                        .display(
                                "§2+" + addedAmount + " " + category.getName() +
                                        " §7(" + player.getCollection().get(type) +
                                        "/" +
                                        player.getCollection().getReward(collection).requirement() + ")")
                        .build(), StatisticDisplayReplacement.DisplayType.DEFENSE);
            } else {
                player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                        .ticksToLast(20)
                        .purpose(StatisticDisplayReplacement.Purpose.COLLECTION)
                        .display(
                                "§2+1 " + category.getName() +
                                        " §7(" + player.getCollection().get(type) +
                                        "/" +
                                        player.getCollection().getReward(collection).requirement() + ")")
                        .build(), StatisticDisplayReplacement.DisplayType.DEFENSE);
            }
        }, 5);
    }
}

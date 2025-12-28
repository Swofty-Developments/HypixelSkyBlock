package net.swofty.type.skyblockgeneric.event.actions.custom.collection;

import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.proxyapi.ProxyPlayerSet;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.utility.MathUtility;
import net.swofty.type.skyblockgeneric.SkyBlockGenericLoader;
import net.swofty.type.skyblockgeneric.collection.CollectionCategories;
import net.swofty.type.skyblockgeneric.collection.CollectionCategory;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointCollection;
import net.swofty.type.skyblockgeneric.data.monogdb.CoopDatabase;
import net.swofty.type.skyblockgeneric.event.custom.CollectionUpdateEvent;
import net.swofty.type.skyblockgeneric.event.custom.CustomBlockBreakEvent;
import net.swofty.type.skyblockgeneric.user.SkyBlockActionBar;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionCollectionAdd implements HypixelEventClass {


    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
    public void run(CustomBlockBreakEvent event) {
        if (event.getPlayerPlaced()) return;

        SkyBlockPlayer player = event.getPlayer();

        // Process each dropped item for collection
        for (net.swofty.type.skyblockgeneric.item.SkyBlockItem drop : event.getDrops()) {
            ItemType type = drop.getAttributeHandler().getPotentialType();
            if (type == null) continue;

            int oldAmount = player.getCollection().get(type);
            int dropAmount = drop.getAmount();
            player.getCollection().increase(type, dropAmount);

            HypixelEventHandler.callCustomEvent(new CollectionUpdateEvent(player, type, oldAmount));

            player.getSkyblockDataHandler().get(net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler.Data.COLLECTION, DatapointCollection.class).setValue(
                    player.getCollection()
            );

            if (player.isCoop()) {
                CoopDatabase.Coop coop = player.getCoop();

                coop.getOnlineMembers().forEach(member -> {
                    if (member.getUuid().equals(player.getUuid())) return;
                    HypixelEventHandler.callCustomEvent(new CollectionUpdateEvent(member, type, oldAmount));
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
            if (category == null) continue;
            CollectionCategory.ItemCollection collection = category.getCollection(type);

            final int finalDropAmount = dropAmount;
            final ItemType finalType = type;
            MathUtility.delay(() -> {
                SkyBlockActionBar bar = SkyBlockActionBar.getFor(player);
                int startingPriority = 5;
                int addedAmount = finalDropAmount;

                SkyBlockActionBar.DisplayReplacement existingReplacement = bar.getReplacement(SkyBlockActionBar.BarSection.DEFENSE);
                if (existingReplacement != null) {
                    startingPriority = existingReplacement.priority() + 1;
                    try {
                        addedAmount = Integer.parseInt(existingReplacement.display().substring(2, existingReplacement.display().indexOf(" "))) + finalDropAmount;
                    } catch (NumberFormatException ignored) {}
                }
                if (player.getCollection().getReward(collection) != null) {
                    bar.addReplacement(
                            SkyBlockActionBar.BarSection.DEFENSE,
                            new SkyBlockActionBar.DisplayReplacement(
                                    "§2+" + addedAmount + " " + finalType.getDisplayName() +
                                            " §7(" + StringUtility.commaify(player.getCollection().get(finalType)) +
                                            "/" +
                                            StringUtility.shortenNumber(player.getCollection().getReward(collection).requirement()) + ")",
                                    20,
                                    startingPriority
                            )
                    );
                } else { //if Collection is maxed
                    bar.addReplacement(
                            SkyBlockActionBar.BarSection.DEFENSE,
                            new SkyBlockActionBar.DisplayReplacement(
                                    "§2+" + addedAmount + " " + finalType.getDisplayName() +
                                            " §7(" + StringUtility.commaify(player.getCollection().get(finalType)) + ")",
                                    20,
                                    startingPriority
                            )
                    );
                }
            }, 5);
        }
    }
}

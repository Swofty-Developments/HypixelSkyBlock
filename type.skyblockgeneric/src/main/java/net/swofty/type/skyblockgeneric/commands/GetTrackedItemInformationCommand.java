package net.swofty.type.skyblockgeneric.commands;

import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.TrackedItem;
import net.swofty.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@CommandParameters(aliases = "itemtrackedinformation",
        description = "Gets the information of a tracked item in the players hand",
        usage = "/itemtrackedinformation",
        permission = Rank.STAFF,
        allowsConsole = false)
public class GetTrackedItemInformationCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockItem item = new SkyBlockItem(((SkyBlockPlayer) sender).getItemInMainHand());
            if (item.getAttributeHandler().getUniqueTrackedID() == null) {
                sender.sendMessage("§cThis item is not tracked.");
                return;
            }

            ProxyService service = new ProxyService(ServiceType.ITEM_TRACKER);
            if (!service.isOnline().join()) {
                sender.sendMessage("§cThe item tracker service is currently offline.");
                return;
            }
            long start = System.currentTimeMillis();

            CompletableFuture<TrackedItemRetrieveProtocolObject.TrackedItemResponse> trackedItemFuture = service.handleRequest(
                    new TrackedItemRetrieveProtocolObject.TrackedItemRetrieveMessage(
                            UUID.fromString(item.getAttributeHandler().getUniqueTrackedID())
            ));

            TrackedItem trackedItem = trackedItemFuture.join().trackedItem();

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.sendMessage("§aInformation for tracked item in hand:");
            player.sendMessage("§7- §eItem Type: §7" + trackedItem.getItemType());
            player.sendMessage("§7- §eItem UUID: §7" + trackedItem.getItemUUID());
            player.sendMessage("§7- §eNumber Made: §7" + trackedItem.getNumberMade());
            player.sendMessage("§7- §eMade: §7" + StringUtility.formatTimeAsAgo(trackedItem.getCreated()));
            player.sendMessage("§8Attached Players: §7" + trackedItem.attachedPlayers.size());

            trackedItem.attachedPlayers.forEach(log -> {
                player.sendMessage("§7- §ePlayer UUID: §7" + log.playerUUID());
                player.sendMessage("§7- §eProfile UUID: §7" + log.playerProfileUUID());
                player.sendMessage("§7- §eFirst Seen: §7" + StringUtility.formatTimeAsAgo(log.firstSeen()));
                player.sendMessage("§7- §eLast Seen: §7" + StringUtility.formatTimeAsAgo(log.lastSeen()));
            });

            player.sendMessage("§cTook " + (System.currentTimeMillis() - start) + "ms to get information.");
        });
    }
}

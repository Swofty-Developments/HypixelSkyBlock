package net.swofty.types.generic.command.commands;

import net.swofty.commons.ServiceType;
import net.swofty.commons.TrackedItem;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.protocol.ProtocolPingSpecification;
import net.swofty.types.generic.protocol.itemtracker.ProtocolGetTrackedItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.utility.StringUtility;

import java.util.Map;
import java.util.UUID;

@CommandParameters(aliases = "itemtrackedinformation",
        description = "Gets the information of a tracked item in the players hand",
        usage = "/itemtrackedinformation",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class GetTrackedItemInformationCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockItem item = new SkyBlockItem(((SkyBlockPlayer) sender).getItemInMainHand());
            if (item.getAttributeHandler().getUniqueTrackedID() == null) {
                sender.sendMessage("§cThis item is not tracked.");
                return;
            }

            ProxyService service = new ProxyService(ServiceType.ITEM_TRACKER);
            if (!service.isOnline(new ProtocolPingSpecification()).join()) {
                sender.sendMessage("§cThe item tracker service is currently offline.");
                return;
            }
            long start = System.currentTimeMillis();

            TrackedItem trackedItem = (TrackedItem) service.callEndpoint(new ProtocolGetTrackedItem(),
                    Map.of("item-uuid", UUID.fromString(item.getAttributeHandler().getUniqueTrackedID()))
            ).join().get("tracked-item");

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

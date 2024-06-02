package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentDouble;
import net.swofty.commons.Configuration;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeSandboxItem;
import net.swofty.types.generic.item.impl.DefaultSoulbound;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;

@CommandParameters(aliases = "updateitemstatistic",
        description = "Updates the statistic of a player's Sandbox item",
        usage = "/setitemstatistic <statistic> <amount>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class SetItemStatisticCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<ItemStatistic> statistic = new ArgumentEnum<>("statistic", ItemStatistic.class);
        ArgumentDouble amount = new ArgumentDouble("amount");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (Configuration.get("sandbox-mode").equals("false")) {
                sender.sendMessage("§cThis command is disabled on this server.");
                return;
            }

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            SkyBlockItem itemInHand = new SkyBlockItem(player.getItemInMainHand());
            ItemType type = itemInHand.getAttributeHandler().getItemTypeAsType();

            if (type != ItemType.SANDBOX_ITEM) {
                player.sendMessage("§cYou can only set the lore of sandbox items.");
                return;
            }

            Thread.startVirtualThread(() -> {
                ItemStatistic stat = context.get(statistic);
                double amt = context.get(amount);
                double cap = 0;

                if (amt < 1) {
                    player.sendMessage("§cThe amount must be greater than 0.");
                    return;
                }

                if (stat.getIsPercentage()) {
                    cap = 5;
                } else {
                    cap = 50;
                }

                player.sendMessage("§7Checking minimum requirements for statistic amount of §e" + stat.getDisplayName() + "§7...");

                for (SkyBlockItem item : player.getAllInventoryItems()) {
                    if (item.getGenericInstance() == null) continue;
                    if (item.getGenericInstance() instanceof DefaultSoulbound) {
                        if (item.getAttributeHandler().getStatistics().getOverall(stat) > cap) {
                            cap = item.getAttributeHandler().getStatistics().getOverall(stat);
                        }
                    }
                }

                if (amt > cap) {
                    player.sendMessage("§cThe amount of " + stat.getDisplayName() + " cannot exceed " + cap + ".");
                    player.sendMessage("§ePercentage-based statistics are capped at 5% and normal statistics at 50");
                    player.sendMessage("§eSoulbound items can increase this cap.");
                    return;
                }

                player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                    ItemAttributeSandboxItem.SandboxData data = item.getAttributeHandler().getSandboxData();
                    data.setStatistics(ItemStatistics.builder().withBase(stat, amt).build());
                    item.getAttributeHandler().setSandboxData(data);
                });

                player.sendMessage("§aUpdated the " + stat.getDisplayName() + " of the item in your hand to §e" + amt + "§a.");
            });

        }, statistic, amount);
    }
}

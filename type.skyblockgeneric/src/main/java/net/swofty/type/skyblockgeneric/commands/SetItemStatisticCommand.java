package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentDouble;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeSandboxItem;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.DefaultSoulboundComponent;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "updateitemstatistic",
        description = "Updates the statistic of a player's Sandbox item",
        usage = "/setitemstatistic <statistic> <amount>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class SetItemStatisticCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ItemStatistic> statistic = new ArgumentEnum<>("statistic", ItemStatistic.class);
        ArgumentDouble amount = new ArgumentDouble("amount");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!ConfigProvider.settings().isSandbox()) {
                sender.sendMessage("§cThis command is disabled on this server.");
                return;
            }

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            SkyBlockItem itemInHand = new SkyBlockItem(player.getItemInMainHand());
            ItemType type = itemInHand.getAttributeHandler().getPotentialType();

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
                    if (item.hasComponent(DefaultSoulboundComponent.class)) {
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

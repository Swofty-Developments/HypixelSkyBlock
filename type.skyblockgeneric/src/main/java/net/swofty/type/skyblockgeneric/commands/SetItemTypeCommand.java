package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.swofty.commons.config.ConfigProvider;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "settype",
        description = "Updates the type of a player's Sandbox item",
        usage = "/setitemtype <material>",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class SetItemTypeCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ItemType> material = new ArgumentEnum<>("material", ItemType.class);

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
                player.sendMessage("§cYou can only set the type of sandbox items.");
                return;
            }

            ItemType newType = context.get(material);

            player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                item.getAttributeHandler().getSandboxData().setMaterial(newType);
            });

            player.sendMessage("§aUpdated the type of the item in your hand to §e" + context.get(material).getDisplayName() + "§a.");
        }, material);
    }
}

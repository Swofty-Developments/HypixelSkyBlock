package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.gui.inventories.stash.GUIStashItem;
import net.swofty.type.skyblockgeneric.gui.inventories.stash.GUIStashMaterial;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(aliases = "viewstash",
        description = "View your stash",
        usage = "/viewstash [item|material]",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class ViewStashCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord typeArg = new ArgumentWord("type").from("item", "material");

        // No args - open item stash by default
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            new GUIStashItem().open(player);
        });

        // With type argument
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            String type = context.get(typeArg);

            if (type.equalsIgnoreCase("material")) {
                new GUIStashMaterial().open(player);
            } else {
                new GUIStashItem().open(player);
            }
        }, typeArg);
    }
}

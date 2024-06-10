package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.types.generic.item.ItemTypeLinker;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Opens up a recipe GUI",
        usage = "/viewrecipe <recipe>",
        permission = Rank.DEFAULT,
        aliases = "vr",
        allowsConsole = false)
public class ViewRecipeCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<ItemTypeLinker> itemArgument = ArgumentType.Enum("item", ItemTypeLinker.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final ItemTypeLinker item = context.get(itemArgument);
            new GUIRecipe(item, null).open((SkyBlockPlayer) sender);
        }, itemArgument);
    }
}

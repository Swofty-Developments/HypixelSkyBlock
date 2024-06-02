package net.swofty.types.generic.command.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.swofty.packer.NegativeSpace;
import net.swofty.packer.SkyBlockTexture;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Tests the black texture of the resource pack",
        usage = "/testblack",
        permission = Rank.ADMIN,
        aliases = "testblackresource",
        allowsConsole = false)
public class TestBlackCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            sender.showTitle(Title.title(
                    Component.text(SkyBlockTexture.VILLAGER_SPEAK_OUTLINE.toString()),
                    Component.text(NegativeSpace.getNegativeSpace(10) + "§7This is a test")
            ));
        });
    }
}

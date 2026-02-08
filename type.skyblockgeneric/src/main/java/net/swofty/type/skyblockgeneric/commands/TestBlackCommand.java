package net.swofty.type.skyblockgeneric.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.swofty.packer.NegativeSpace;
import net.swofty.packer.packs.TestingTexture;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(description = "Tests the black texture of the resource pack",
        usage = "/testblack",
        permission = Rank.STAFF,
        aliases = "testblackresource",
        allowsConsole = false)
public class TestBlackCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            sender.showTitle(Title.title(
                    Component.text(TestingTexture.VILLAGER_SPEAK_OUTLINE.toString()),
                    Component.text(NegativeSpace.getNegativeSpace(10) + "§7This is a test")
            ));
        });
    }
}

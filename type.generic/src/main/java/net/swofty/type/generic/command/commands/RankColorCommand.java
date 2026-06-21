package net.swofty.type.generic.command.commands;

import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.gui.impl.GUIRankColor;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(aliases = "rankcolour", description = "Change your MVP+ rank color",
    usage = "/rankcolor", permission = Rank.DEFAULT, allowsConsole = false)
public class RankColorCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> ((HypixelPlayer) sender).openView(new GUIRankColor()));
    }
}

package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.skills.GUISkillCategory;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.user.categories.Rank;

@CommandParameters(description = "Opens up a skills GUI",
        usage = "/viewskill <skill>",
        permission = Rank.DEFAULT,
        aliases = "vs",
        allowsConsole = false)
public class ViewSkillCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<SkillCategories> skillArgument = ArgumentType.Enum("skill", SkillCategories.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final SkillCategories skillCategory = context.get(skillArgument);
            ((SkyBlockPlayer) sender).openView(new GUISkillCategory(skillCategory, 0));
        }, skillArgument);
    }
}

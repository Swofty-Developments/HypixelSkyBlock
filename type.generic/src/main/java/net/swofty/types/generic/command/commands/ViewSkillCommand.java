package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.skills.GUISkillCategory;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(description = "Opens up a skills GUI",
        usage = "/viewskill <skill>",
        permission = Rank.DEFAULT,
        aliases = "vs",
        allowsConsole = false)
public class ViewSkillCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<SkillCategories> skillArgument = ArgumentType.Enum("skill", SkillCategories.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final SkillCategories skillCategory = context.get(skillArgument);
            new GUISkillCategory(skillCategory, 0).open((SkyBlockPlayer) sender);
        }, skillArgument);
    }
}

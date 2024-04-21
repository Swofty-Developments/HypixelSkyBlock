package net.swofty.types.generic.command.commands;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.entity.Player;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.data.datapoints.DatapointSkills;
import net.swofty.types.generic.skill.SkillCategories;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "giveskillxp",
        description = "Adds XP for a specific skill.",
        usage = "/addskillxp <Skill> <Amount>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class AddSkillXpCommand extends SkyBlockCommand {

    @Override
    public void run(MinestomCommand command) {
        ArgumentEnum<SkillCategories> skill = new ArgumentEnum<>("skill", SkillCategories.class);
        ArgumentInteger amount = new ArgumentInteger("amount");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            SkillCategories skillCategory = context.get(skill);
            int amountInt = context.get(amount);

            SkyBlockPlayer player = ((SkyBlockPlayer) sender);
            DatapointSkills.PlayerSkills skills = player.getSkills();

            skills.setRaw(player, skillCategory, skills.getRaw(skillCategory) + amountInt);
            sender.sendMessage("§fSuccessfully added §a" + amountInt + " §fworth of xp to the skill §3" + skillCategory.name());
        }, skill, amount);
    }
}

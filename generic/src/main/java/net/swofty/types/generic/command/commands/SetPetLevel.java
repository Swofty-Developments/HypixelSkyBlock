package net.swofty.types.generic.command.commands;

import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.types.generic.command.CommandParameters;
import net.swofty.types.generic.command.SkyBlockCommand;
import net.swofty.types.generic.item.Rarity;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributePetData;
import net.swofty.types.generic.item.updater.PlayerItemOrigin;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.categories.Rank;

@CommandParameters(aliases = "updatepetlevel",
        description = "Sets the level of the pet",
        usage = "/setpetlevel <level>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class SetPetLevel extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        ArgumentInteger level = new ArgumentInteger("level");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            int levelAsInt = context.get(level);

            player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                ItemAttributePetData.PetData data = item.getAttributeHandler().getPetData();
                data.setLevel(levelAsInt, Rarity.COMMON);
                item.getAttributeHandler().setPetData(data);
            });

            player.sendMessage("§aUpdated the level of the pet to §e" + levelAsInt + "§a.");
        }, level);
    }
}

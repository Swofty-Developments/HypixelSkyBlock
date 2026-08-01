package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.item.ItemAttributeHandler;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Set;

@CommandParameters(labels = "setpetrarity",
        description = "Sets the rarity of the pet in your hand",
        usage = "/setpetrarity <rarity>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class SetPetRarityCommand extends HypixelCommand {
    private static final Set<String> VALID_RARITIES =
            Set.of("COMMON", "UNCOMMON", "RARE", "EPIC", "LEGENDARY", "MYTHIC");

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord rarityArgument = new ArgumentWord("rarity");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String rawRarity = context.get(rarityArgument).toUpperCase();
            if (!VALID_RARITIES.contains(rawRarity)) {
                sender.sendMessage("§cInvalid rarity. Use COMMON/UNCOMMON/RARE/EPIC/LEGENDARY/MYTHIC.");
                return;
            }
            Rarity rarity = Rarity.valueOf(rawRarity);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                ItemAttributeHandler handler = item.getAttributeHandler();
                if (!handler.isPet()) {
                    sender.sendMessage("§cThe item in your hand is not a pet.");
                    return;
                }
                handler.setRarity(rarity);
                sender.sendMessage("§aSet rarity to " + rarity.getColor() + rarity + "§a.");
            });
        }, rarityArgument);
    }
}

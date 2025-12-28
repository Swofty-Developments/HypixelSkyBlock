package net.swofty.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.command.CommandParameters;
import net.swofty.type.generic.command.HypixelCommand;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointStash;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

@CommandParameters(aliases = "pickupstash",
        description = "Pickup items from your stash",
        usage = "/pickupstash [item|material]",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class PickupStashCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord typeArg = new ArgumentWord("type").from("item", "material");

        // No args - pick up from both stashes
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            pickupFromItemStash(player);
            pickupFromMaterialStash(player);
        });

        // With type argument
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            String type = context.get(typeArg);

            if (type.equalsIgnoreCase("material")) {
                pickupFromMaterialStash(player);
            } else {
                pickupFromItemStash(player);
            }
        }, typeArg);
    }

    private void pickupFromItemStash(SkyBlockPlayer player) {
        DatapointStash.PlayerStash stash = player.getStash();

        if (stash.getItemStashCount() == 0) {
            player.sendMessage("§cYour item stash is already empty!");
            return;
        }

        int pickedUp = 0;
        while (player.hasEmptySlots(1) && stash.getItemStashCount() > 0) {
            SkyBlockItem removed = stash.removeFromItemStash(0);
            if (removed != null) {
                player.addAndUpdateItem(removed);
                player.sendMessage("§aFrom stash: §f" + removed.getDisplayName());
                pickedUp++;
            }
        }

        if (pickedUp == 0) {
            player.sendMessage("§cCouldn't unstash your item stash! Your inventory is full!");
        } else if (stash.getItemStashCount() == 0) {
            player.sendMessage("§aYou picked up all items from your item stash!");
        } else {
            player.sendMessage("§eYou still have §c" + stash.getItemStashCount() + " §eitems in there!");
        }
    }

    private void pickupFromMaterialStash(SkyBlockPlayer player) {
        DatapointStash.PlayerStash stash = player.getStash();

        if (stash.getMaterialStashCount() == 0) {
            player.sendMessage("§cYour material stash is already empty!");
            return;
        }

        int pickedUp = 0;
        List<ItemType> types = new ArrayList<>(stash.getMaterialStash().keySet());

        for (ItemType type : types) {
            int amount = stash.getMaterialStash().getOrDefault(type, 0);
            int maxStackSize = type.material.maxStackSize();

            while (amount > 0 && player.hasEmptySlots(1)) {
                int toPickup = Math.min(amount, maxStackSize);
                int removed = stash.removeFromMaterialStash(type, toPickup);
                if (removed > 0) {
                    SkyBlockItem newItem = new SkyBlockItem(type);
                    newItem.setAmount(removed);
                    player.addAndUpdateItem(newItem);
                    player.sendMessage("§aFrom stash: §f" + type.getDisplayName() + " §7x" + removed);
                    pickedUp += removed;
                    amount -= removed;
                } else {
                    break;
                }
            }

            if (!player.hasEmptySlots(1)) break;
        }

        if (pickedUp == 0) {
            player.sendMessage("§cCouldn't unstash your material stash! Your inventory is full!");
        } else if (stash.getMaterialStashCount() == 0) {
            player.sendMessage("§aYou picked up all items from your material stash!");
        } else {
            player.sendMessage("§eYou still have §c" + stash.getMaterialStashCount() +
                    " §ematerials totalling §c" + stash.getMaterialTypeCount() +
                    " §etypes of materials in there!");
        }
    }
}

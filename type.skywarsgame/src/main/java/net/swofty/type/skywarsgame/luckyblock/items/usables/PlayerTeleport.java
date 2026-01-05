package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItem;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;
import java.util.Random;

public class PlayerTeleport implements LuckyBlockItem {

    private static final Random RANDOM = new Random();

    @Override
    public String getId() {
        return "player_teleport";
    }

    @Override
    public String getDisplayName() {
        return "Player Teleport";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.ENDER_PEARL)
                .customName(Component.text(getDisplayName(), NamedTextColor.DARK_PURPLE)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Teleport to a random", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("alive player!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to use!", NamedTextColor.YELLOW)
                                .decoration(TextDecoration.ITALIC, false)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public boolean onUse(SkywarsPlayer holder) {
        SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(holder);
        if (game == null) {
            holder.sendMessage(Component.text("You are not in a game!", NamedTextColor.RED));
            return false;
        }

        List<SkywarsPlayer> alivePlayers = game.getAlivePlayers().stream()
                .filter(p -> !p.getUuid().equals(holder.getUuid()))
                .toList();

        if (alivePlayers.isEmpty()) {
            holder.sendMessage(Component.text("No other players to teleport to!", NamedTextColor.RED));
            return false;
        }

        SkywarsPlayer target = alivePlayers.get(RANDOM.nextInt(alivePlayers.size()));
        holder.teleport(target.getPosition());
        holder.sendMessage(Component.text("Teleported to ", NamedTextColor.LIGHT_PURPLE)
                .append(Component.text(target.getUsername(), NamedTextColor.GOLD))
                .append(Component.text("!", NamedTextColor.LIGHT_PURPLE)));

        return true;
    }

    @Override
    public boolean hasUseEffect() {
        return true;
    }
}

package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class EnderTeleport implements LuckyBlockConsumable {

    @Override
    public String getId() {
        return "ender_teleport";
    }

    @Override
    public String getDisplayName() {
        return "Ender Teleport";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.ENDER_EYE)
                .amount(3)
                .customName(Component.text(getDisplayName(), NamedTextColor.DARK_PURPLE)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Teleport to the block you're", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("looking at (up to 100 blocks)!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to use!", NamedTextColor.YELLOW)
                                .decoration(TextDecoration.ITALIC, false)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public void onConsume(SkywarsPlayer player) {
        Point targetBlock = player.getTargetBlockPosition(100);
        if (targetBlock == null) {
            player.sendMessage(Component.text("No block in range!", NamedTextColor.RED));
            return;
        }

        if (!player.getInstance().getBlock(targetBlock.add(0, 1, 0)).isAir() ||
                !player.getInstance().getBlock(targetBlock.add(0, 2, 0)).isAir()) {
            player.sendMessage(Component.text("Not enough space to teleport!", NamedTextColor.RED));
            return;
        }

        player.teleport(new Pos(
                targetBlock.x() + 0.5,
                targetBlock.y() + 1,
                targetBlock.z() + 0.5,
                player.getPosition().yaw(),
                player.getPosition().pitch()
        ));
        player.sendMessage(Component.text("Whoosh!", NamedTextColor.DARK_PURPLE));
    }
}

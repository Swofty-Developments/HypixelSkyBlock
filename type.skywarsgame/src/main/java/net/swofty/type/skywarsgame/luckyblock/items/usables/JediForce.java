package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class JediForce implements LuckyBlockConsumable {

    private static final double PUSH_POWER = 100.0;
    private static final double MAX_RANGE = 10.0;

    @Override
    public String getId() {
        return "jedi_force";
    }

    @Override
    public String getDisplayName() {
        return "Jedi Force";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.BLAZE_ROD)
                .customName(Component.text(getDisplayName(), NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Use the Force to push", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("your enemies away!", NamedTextColor.GRAY)
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
        SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(player);
        if (game == null) return;

        Vec lookDirection = player.getPosition().direction();
        SkywarsPlayer target = null;
        double closestDistance = Double.MAX_VALUE;

        for (SkywarsPlayer other : game.getPlayers()) {
            if (other.equals(player) || other.isEliminated()) continue;

            Vec toOther = Vec.fromPoint(other.getPosition().sub(player.getPosition()));
            double distance = toOther.length();

            if (distance > MAX_RANGE) continue;

            Vec normalizedToOther = toOther.normalize();
            double dot = lookDirection.dot(normalizedToOther);

            if (dot > 0.7 && distance < closestDistance) {
                closestDistance = distance;
                target = other;
            }
        }

        if (target == null) {
            player.sendMessage(Component.text("No target in range!", NamedTextColor.RED));
            return;
        }

        Vec pushDirection = Vec.fromPoint(target.getPosition().sub(player.getPosition())).normalize();
        target.setVelocity(pushDirection.mul(PUSH_POWER));

        player.sendMessage(Component.text("The Force is with you!", NamedTextColor.AQUA));
        target.sendMessage(Component.text("You were pushed by the Force!", NamedTextColor.RED));
    }
}

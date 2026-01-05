package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.pvp.entity.projectile.ThrownEgg;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BridgeEggs implements LuckyBlockConsumable {

    private static final Block BRIDGE_BLOCK = Block.WHITE_WOOL;

    @Override
    public String getId() {
        return "bridge_eggs";
    }

    @Override
    public String getDisplayName() {
        return "Bridge Eggs";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.EGG)
                .amount(4)
                .customName(Component.text(getDisplayName(), NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Throw an egg that creates", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("a bridge as it flies!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to throw!", NamedTextColor.YELLOW)
                                .decoration(TextDecoration.ITALIC, false)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public void onConsume(SkywarsPlayer player) {
        SkywarsPlayerBridgeEgg egg = new SkywarsPlayerBridgeEgg(BRIDGE_BLOCK, player);
        egg.setInstance(player.getInstance(), player.getPosition().add(0, player.getEyeHeight(), 0));
        egg.setVelocity(player.getPosition().direction().mul(30));
    }

    private static class SkywarsPlayerBridgeEgg extends ThrownEgg {
        private final Block block;

        public SkywarsPlayerBridgeEgg(Block block, @Nullable Entity shooter) {
            super(shooter);
            this.block = block;
        }

        @Override
        public void tick(long time) {
            super.tick(time);

            if (this.instance != null && this.position != null) {
                Vec velocity = this.getVelocity();

                double length = Math.sqrt(velocity.x() * velocity.x() + velocity.z() * velocity.z());
                if (length > 0) {
                    double offsetX = -velocity.x() / length;
                    double offsetZ = -velocity.z() / length;

                    Point center = this.position.sub(0, 1, 0).add(offsetX, 0, offsetZ);

                    for (int x = -1; x <= 0; x++) {
                        for (int z = -1; z <= 0; z++) {
                            Point blockPos = center.add(x, 0, z);
                            if (this.instance.getBlock(blockPos).isAir()) {
                                this.instance.setBlock(blockPos, block);
                            }
                        }
                    }
                }
            }
        }
    }
}

package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItem;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class Jackhammer implements LuckyBlockItem {

    public static final String ID = "jackhammer";
    public static final Tag<Integer> USES_TAG = Tag.Integer("jackhammer_uses");
    private static final int MAX_USES = 10;
    private static final double MAX_REACH = 5.0;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Jackhammer";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.DIAMOND_PICKAXE)
                .customName(Component.text(getDisplayName(), NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Destroys a 3x3x3 cube", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("of blocks on use!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Uses: ", NamedTextColor.GRAY)
                                .append(Component.text(MAX_USES, NamedTextColor.GREEN))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click a block to use!", NamedTextColor.YELLOW)
                                .decoration(TextDecoration.ITALIC, false)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .set(USES_TAG, MAX_USES)
                .build();
    }

    @Override
    public boolean onUse(SkywarsPlayer holder) {
        Instance instance = holder.getInstance();
        if (instance == null) return false;

        Point targetBlock = getTargetBlock(holder, instance);
        if (targetBlock == null) {
            holder.sendMessage(Component.text("No block in range!", NamedTextColor.RED));
            return false;
        }

        SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(holder);
        int blocksDestroyed = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    Point blockPos = targetBlock.add(dx, dy, dz);
                    Block block = instance.getBlock(blockPos);
                    boolean isChest = game != null && game.getChestManager().isChestPosition(new net.minestom.server.coordinate.Pos(blockPos));

                    if (!block.isAir() && !block.compare(Block.BEDROCK) && !isChest) {
                        instance.setBlock(blockPos, Block.AIR);
                        blocksDestroyed++;
                    }
                }
            }
        }

        if (blocksDestroyed > 0) {
            holder.sendMessage(Component.text("Destroyed " + blocksDestroyed + " blocks!", NamedTextColor.AQUA));

            ItemStack currentItem = holder.getItemInMainHand();
            Integer uses = currentItem.getTag(USES_TAG);
            int remainingUses = (uses != null ? uses : MAX_USES) - 1;

            if (remainingUses <= 0) {
                holder.setItemInMainHand(ItemStack.AIR);
                holder.sendMessage(Component.text("Your Jackhammer broke!", NamedTextColor.RED));
            } else {
                ItemStack updatedItem = currentItem
                        .with(builder -> builder.set(USES_TAG, remainingUses))
                        .withLore(List.of(
                                Component.text("Destroys a 3x3x3 cube", NamedTextColor.GRAY)
                                        .decoration(TextDecoration.ITALIC, false),
                                Component.text("of blocks on use!", NamedTextColor.GRAY)
                                        .decoration(TextDecoration.ITALIC, false),
                                Component.empty(),
                                Component.text("Uses: ", NamedTextColor.GRAY)
                                        .append(Component.text(remainingUses, NamedTextColor.GREEN))
                                        .decoration(TextDecoration.ITALIC, false),
                                Component.empty(),
                                Component.text("Right-click a block to use!", NamedTextColor.YELLOW)
                                        .decoration(TextDecoration.ITALIC, false)
                        ));
                holder.setItemInMainHand(updatedItem);
            }
            return true;
        }

        return false;
    }

    private Point getTargetBlock(SkywarsPlayer player, Instance instance) {
        Vec eyePos = player.getPosition().add(0, player.getEyeHeight(), 0).asVec();
        Vec direction = player.getPosition().direction();

        for (double d = 0; d <= MAX_REACH; d += 0.25) {
            Vec checkPos = eyePos.add(direction.mul(d));
            Block block = instance.getBlock(checkPos);
            if (!block.isAir()) {
                return checkPos;
            }
        }
        return null;
    }

    @Override
    public boolean hasUseEffect() {
        return true;
    }
}

package net.swofty.type.skywarsgame.luckyblock.items.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockArmor;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.time.Duration;
import java.util.List;

public class ChillyPants implements LuckyBlockArmor {

    public static final String ID = "chilly_pants";
    private static final int TRAIL_TICK_INTERVAL = 4;
    private static final Duration ICE_DURATION = Duration.ofSeconds(5);

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Chilly Pants";
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.LEGGINGS;
    }

    @Override
    public Material getBaseMaterial() {
        return Material.LEATHER_LEGGINGS;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.LEATHER_LEGGINGS)
                .customName(Component.text("Chilly Pants", NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("The ground beneath you ", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("turns to ", NamedTextColor.GRAY)
                                .append(Component.text("ice", NamedTextColor.AQUA))
                                .append(Component.text(" as you walk.", NamedTextColor.GRAY))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("LUCKY BLOCK ITEM", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false)
                                .decoration(TextDecoration.BOLD, true)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .build();
    }

    @Override
    public void onEquip(SkywarsPlayer player) {
        player.sendMessage(Component.text("You feel a chill...", NamedTextColor.AQUA));
    }

    @Override
    public void onWornTick(SkywarsPlayer player) {
        if (player.getAliveTicks() % TRAIL_TICK_INTERVAL != 0) {
            return;
        }

        if (!player.isOnGround()) {
            return;
        }

        Instance instance = player.getInstance();
        if (instance == null) {
            return;
        }

        Pos playerPos = player.getPosition();
        Pos blockBelow = new Pos(
                playerPos.blockX(),
                playerPos.blockY() - 1,
                playerPos.blockZ()
        );

        Block currentBlock = instance.getBlock(blockBelow);

        if (!currentBlock.isSolid() || currentBlock.compare(Block.PACKED_ICE) || currentBlock.compare(Block.BEDROCK)) {
            return;
        }

        instance.setBlock(blockBelow, Block.PACKED_ICE);

        player.scheduler().buildTask(() -> {
            Block currentAtPos = instance.getBlock(blockBelow);
            if (currentAtPos.compare(Block.PACKED_ICE)) {
                instance.setBlock(blockBelow, currentBlock);
            }
        }).delay(ICE_DURATION).schedule();
    }

    @Override
    public boolean hasTrailEffect() {
        return true;
    }
}

package net.swofty.type.skywarsgame.luckyblock.items.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockArmor;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;
import java.util.Random;

public class SplatcraftBoots implements LuckyBlockArmor {

    public static final String ID = "splatcraft_boots";
    private static final int TRAIL_TICK_INTERVAL = 3;
    private static final int SPEED_REFRESH_TICKS = 40;
    private static final Random RANDOM = new Random();

    private static final Block[] TERRACOTTA_COLORS = {
            Block.WHITE_TERRACOTTA,
            Block.ORANGE_TERRACOTTA,
            Block.MAGENTA_TERRACOTTA,
            Block.LIGHT_BLUE_TERRACOTTA,
            Block.YELLOW_TERRACOTTA,
            Block.LIME_TERRACOTTA,
            Block.PINK_TERRACOTTA,
            Block.CYAN_TERRACOTTA,
            Block.PURPLE_TERRACOTTA,
            Block.BLUE_TERRACOTTA,
            Block.GREEN_TERRACOTTA,
            Block.RED_TERRACOTTA
    };

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Splatcraft Boots";
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.BOOTS;
    }

    @Override
    public Material getBaseMaterial() {
        return Material.LEATHER_BOOTS;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.LEATHER_BOOTS)
                .customName(Component.text("Splatcraft Boots", NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Protection I", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Projectile Protection III", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Turns blocks to ", NamedTextColor.GRAY)
                                .append(Component.text("colored Terracotta", NamedTextColor.YELLOW))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Grants ", NamedTextColor.GRAY)
                                .append(Component.text("Speed I", NamedTextColor.AQUA))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("LUCKY BLOCK ITEM", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false)
                                .decoration(TextDecoration.BOLD, true)
                ))
                .set(DataComponents.DYED_COLOR, new Color(255, 165, 0))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .build();
    }

    @Override
    public void onEquip(SkywarsPlayer player) {
        player.sendMessage(Component.text("You're a squid now! Paint everything!", NamedTextColor.YELLOW));
        applySpeed(player);
    }

    @Override
    public void onUnequip(SkywarsPlayer player) {
        player.removeEffect(PotionEffect.SPEED);
    }

    @Override
    public void onWornTick(SkywarsPlayer player) {
        if (player.getAliveTicks() % SPEED_REFRESH_TICKS == 0) {
            applySpeed(player);
        }

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
        if (!currentBlock.isSolid() || currentBlock.compare(Block.BEDROCK)) {
            return;
        }

        Block randomTerracotta = TERRACOTTA_COLORS[RANDOM.nextInt(TERRACOTTA_COLORS.length)];
        instance.setBlock(blockBelow, randomTerracotta);
    }

    private void applySpeed(SkywarsPlayer player) {
        player.addEffect(new Potion(PotionEffect.SPEED, (byte) 0, 100));
    }

    @Override
    public boolean hasTrailEffect() {
        return true;
    }

    @Override
    public boolean hasPermanentBuff() {
        return true;
    }
}

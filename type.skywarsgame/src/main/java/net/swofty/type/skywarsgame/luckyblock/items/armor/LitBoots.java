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
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockArmor;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.time.Duration;
import java.util.List;

public class LitBoots implements LuckyBlockArmor {

    public static final String ID = "lit_boots";
    private static final int TRAIL_TICK_INTERVAL = 5;
    private static final Duration FIRE_DURATION = Duration.ofSeconds(3);
    private static final int FIRE_RESISTANCE_REFRESH_TICKS = 40;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Lit Boots";
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
                .customName(Component.text("Lit Boots", NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Leave a ", NamedTextColor.GRAY)
                                .append(Component.text("fire trail", NamedTextColor.RED))
                                .append(Component.text(" behind you.", NamedTextColor.GRAY))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Grants ", NamedTextColor.GRAY)
                                .append(Component.text("Fire Resistance", NamedTextColor.GOLD))
                                .append(Component.text(".", NamedTextColor.GRAY))
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
        player.sendMessage(Component.text("Your feet are on fire!", NamedTextColor.RED));
        applyFireResistance(player);
    }

    @Override
    public void onUnequip(SkywarsPlayer player) {
        player.removeEffect(PotionEffect.FIRE_RESISTANCE);
    }

    @Override
    public void onWornTick(SkywarsPlayer player) {
        if (player.getAliveTicks() % FIRE_RESISTANCE_REFRESH_TICKS == 0) {
            applyFireResistance(player);
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
        Pos blockAbove = new Pos(
                playerPos.blockX(),
                playerPos.blockY(),
                playerPos.blockZ()
        );

        Block currentBlock = instance.getBlock(blockAbove);

        if (!currentBlock.isAir()) {
            return;
        }

        Block blockBelow = instance.getBlock(blockAbove.add(0, -1, 0));
        if (!blockBelow.isSolid()) {
            return;
        }

        instance.setBlock(blockAbove, Block.FIRE);

        player.scheduler().buildTask(() -> {
            Block currentAtPos = instance.getBlock(blockAbove);
            if (currentAtPos.compare(Block.FIRE)) {
                instance.setBlock(blockAbove, Block.AIR);
            }
        }).delay(FIRE_DURATION).schedule();
    }

    private void applyFireResistance(SkywarsPlayer player) {
        player.addEffect(new Potion(PotionEffect.FIRE_RESISTANCE, (byte) 0, 100));
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

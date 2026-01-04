package net.swofty.type.skywarsgame.luckyblock.items.armor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockArmor;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class GreaserJacket implements LuckyBlockArmor {

    public static final String ID = "greaser_jacket";
    private static final Random RANDOM = new Random();
    private static final double TNT_DROP_CHANCE = 0.3;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Greaser Jacket";
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.CHESTPLATE;
    }

    @Override
    public Material getBaseMaterial() {
        return Material.LEATHER_CHESTPLATE;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.LEATHER_CHESTPLATE)
                .customName(Component.text("Greaser Jacket", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Protection I", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Blast Protection II", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Drops ", NamedTextColor.GRAY)
                                .append(Component.text("TNT", NamedTextColor.RED))
                                .append(Component.text(" when you take damage!", NamedTextColor.GRAY))
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("LUCKY BLOCK ITEM", NamedTextColor.GOLD)
                                .decoration(TextDecoration.ITALIC, false)
                                .decoration(TextDecoration.BOLD, true)
                ))
                .set(DataComponents.DYED_COLOR, new Color(30, 30, 30))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, ID)
                .build();
    }

    @Override
    public void onEquip(SkywarsPlayer player) {
        player.sendMessage(Component.text("Stay cool... but explosive!", NamedTextColor.DARK_GRAY));
    }

    @Override
    public void onDamaged(SkywarsPlayer player, Entity attacker, float damage) {
        if (RANDOM.nextDouble() > TNT_DROP_CHANCE) {
            return;
        }

        Instance instance = player.getInstance();
        if (instance == null) {
            return;
        }

        Pos tntPos = player.getPosition().add(
                RANDOM.nextDouble() * 2 - 1,
                0.5,
                RANDOM.nextDouble() * 2 - 1
        );

        Entity tnt = new Entity(EntityType.TNT);
        tnt.setInstance(instance, tntPos);

        tnt.scheduler().buildTask(() -> {
            Pos explosionPos = tnt.getPosition();
            tnt.remove();
            createExplosion(instance, explosionPos, player);
        }).delay(Duration.ofSeconds(3)).schedule();
    }

    private void createExplosion(Instance instance, Pos pos, SkywarsPlayer owner) {
        for (Entity entity : instance.getNearbyEntities(pos, 4)) {
            if (entity instanceof SkywarsPlayer target && target != owner) {
                double distance = target.getPosition().distance(pos);
                if (distance < 4) {
                    float damage = (float) (6 * (1 - distance / 4));
                    target.damage(net.minestom.server.entity.damage.Damage.fromEntity(owner, damage));
                }
            }
        }
    }

    @Override
    public boolean hasDamageEffect() {
        return true;
    }
}

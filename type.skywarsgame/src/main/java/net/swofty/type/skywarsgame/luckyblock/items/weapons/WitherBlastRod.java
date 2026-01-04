package net.swofty.type.skywarsgame.luckyblock.items.weapons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityProjectile;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockWeapon;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class WitherBlastRod implements LuckyBlockWeapon {

    public static final String ID = "wither_blast_rod";
    private static final double PROJECTILE_SPEED = 1.2;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDisplayName() {
        return "Wither Blast Rod";
    }

    @Override
    public Material getBaseMaterial() {
        return Material.BLAZE_ROD;
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.BLAZE_ROD)
                .amount(5)
                .customName(Component.text("Wither Blast Rod", NamedTextColor.DARK_PURPLE)
                        .decoration(TextDecoration.ITALIC, false))
                .lore(List.of(
                        Component.empty(),
                        Component.text("Right-click to fire a", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("Wither skull projectile!", NamedTextColor.DARK_GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Uses: ", NamedTextColor.GRAY)
                                .append(Component.text("5", NamedTextColor.GREEN))
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
    public boolean onUse(SkywarsPlayer holder) {
        Instance instance = holder.getInstance();
        if (instance == null) return false;

        Pos eyePos = holder.getPosition().add(0, holder.getEyeHeight(), 0);
        Vec direction = holder.getPosition().direction().mul(PROJECTILE_SPEED);

        EntityProjectile skull = new EntityProjectile(holder, EntityType.WITHER_SKULL);
        skull.setInstance(instance, eyePos);
        skull.setVelocity(direction);

        return true;
    }

    @Override
    public float onWeaponHit(SkywarsPlayer holder, Entity target, float damage) {
        return damage;
    }

    @Override
    public int getMaxUses() {
        return 5;
    }

    @Override
    public boolean hasUseEffect() {
        return true;
    }

    @Override
    public boolean hasOnHitEffect() {
        return false;
    }
}

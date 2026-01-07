package net.swofty.type.skywarsgame.luckyblock.items.usables;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.PrimedTntMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItem;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;

public class ThrowableTNT implements LuckyBlockItem {

    private static final int FUSE_TICKS = 60;

    @Override
    public String getId() {
        return "throwable_tnt";
    }

    @Override
    public String getDisplayName() {
        return "Throwable TNT";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.TNT)
                .customName(Component.text(getDisplayName(), NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Throw explosive TNT at", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("your enemies!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Explodes in 3 seconds!", NamedTextColor.RED)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Right-click to throw!", NamedTextColor.YELLOW)
                                .decoration(TextDecoration.ITALIC, false)
                ))
                .set(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG, getId())
                .build();
    }

    @Override
    public boolean onUse(SkywarsPlayer holder) {
        Instance instance = holder.getInstance();
        if (instance == null) return false;

        Vec direction = holder.getPosition().direction();
        Pos spawnPos = holder.getPosition().add(direction.mul(2)).add(0, 1.5, 0);

        Entity tnt = new Entity(EntityType.TNT);
        tnt.setInstance(instance, spawnPos);

        if (tnt.getEntityMeta() instanceof PrimedTntMeta tntMeta) {
            tntMeta.setFuseTime(FUSE_TICKS);
        }

        Vec velocity = direction.mul(20).add(0, 10, 0);
        tnt.setVelocity(velocity);

        holder.sendMessage(Component.text("TNT thrown!", NamedTextColor.RED));
        return true;
    }

    @Override
    public boolean hasUseEffect() {
        return true;
    }
}

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
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.tag.Tag;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockConsumable;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.List;
import java.util.UUID;

public class TNTLaunchPad implements LuckyBlockConsumable {

    public static final Tag<String> LAUNCH_PAD_OWNER = Tag.String("launch_pad_owner");
    private static final int FUSE_TICKS = 10;
    private static final int SLOW_FALLING_DURATION = 200;
    private static final double LAUNCH_POWER = 40.0;

    @Override
    public String getId() {
        return "tnt_launch_pad";
    }

    @Override
    public String getDisplayName() {
        return "TNT Launch Pad";
    }

    @Override
    public ItemStack createItemStack() {
        return ItemStack.builder(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
                .customName(Component.text(getDisplayName(), NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true))
                .lore(List.of(
                        Component.text("Spawns 4 TNT that launch", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("you into the sky!", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.empty(),
                        Component.text("Grants Slow Falling", NamedTextColor.GRAY)
                                .decoration(TextDecoration.ITALIC, false),
                        Component.text("after launch.", NamedTextColor.GRAY)
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
        Instance instance = player.getInstance();
        if (instance == null) return;

        Pos playerPos = player.getPosition();
        String ownerUuid = player.getUuid().toString();

        double[][] offsets = {
                {1.5, 0},
                {-1.5, 0},
                {0, 1.5},
                {0, -1.5}
        };

        for (double[] offset : offsets) {
            Pos tntPos = playerPos.add(offset[0], 0, offset[1]);

            Entity tnt = new Entity(EntityType.TNT);
            tnt.setTag(LAUNCH_PAD_OWNER, ownerUuid);
            tnt.setInstance(instance, tntPos);

            if (tnt.getEntityMeta() instanceof PrimedTntMeta tntMeta) {
                tntMeta.setFuseTime(FUSE_TICKS);
            }

            Vec toCenter = Vec.fromPoint(playerPos.sub(tntPos)).normalize();
            tnt.setVelocity(toCenter.mul(5).add(0, 5, 0));
        }

        player.scheduler().buildTask(() -> {
            player.setVelocity(new Vec(0, LAUNCH_POWER, 0));
            player.addEffect(new Potion(PotionEffect.SLOW_FALLING, (byte) 0, SLOW_FALLING_DURATION));
            player.sendMessage(Component.text("Launched!", NamedTextColor.RED));
        }).delay(java.time.Duration.ofMillis(FUSE_TICKS * 50L)).schedule();

        player.sendMessage(Component.text("TNT Launch Pad activated!", NamedTextColor.GOLD));
    }

    public static boolean isOwner(Entity tnt, SkywarsPlayer player) {
        String ownerUuid = tnt.getTag(LAUNCH_PAD_OWNER);
        return ownerUuid != null && ownerUuid.equals(player.getUuid().toString());
    }
}

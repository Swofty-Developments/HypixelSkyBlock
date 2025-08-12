package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.swofty.commons.item.reforge.ReforgeType;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.handlers.bow.BowRegistry;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.groups.EnchantItemGroups;

import java.util.List;
import java.util.function.BiConsumer;

@Getter
public class BowComponent extends SkyBlockItemComponent {
    private final BiConsumer<SkyBlockPlayer, SkyBlockItem> shootHandler;
    private final boolean shouldBeArrow;

    public BowComponent(BiConsumer<SkyBlockPlayer, SkyBlockItem> shootHandler, boolean shouldBeArrow) {
        this.shootHandler = shootHandler;
        this.shouldBeArrow = shouldBeArrow;
        addInheritedComponent(new ExtraRarityComponent("BOW"));
        addInheritedComponent(new QuiverDisplayComponent(shouldBeArrow));
        addInheritedComponent(new ReforgableComponent(ReforgeType.BOWS));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.BOW), true));
        addInheritedComponent(new RuneableComponent(RuneableComponent.RuneApplicableTo.BOWS));
        addInheritedComponent(new TrackedUniqueComponent());
    }

    public BowComponent(String handlerId, boolean shouldBeArrow) {
        this(BowRegistry.getHandler(handlerId).getShootHandler(), shouldBeArrow);
    }

    public void onBowShoot(SkyBlockPlayer player, SkyBlockItem item) {
        if (shootHandler != null) {
            shootHandler.accept(player, item);
        }
    }

    public Vec calculateArrowVelocity(float playerPitch, float playerYaw) {
        double normalizedDrawback = (double) 4 / 5.0;
        double arrowSpeed = 3.0 * normalizedDrawback;
        double pitchRadians = Math.toRadians(playerPitch);
        double yawRadians = Math.toRadians(playerYaw);

        double velocityX = -Math.sin(yawRadians) * arrowSpeed;
        double velocityY = -Math.sin(pitchRadians) * arrowSpeed;
        double velocityZ = Math.cos(yawRadians) * Math.cos(pitchRadians) * arrowSpeed;

        return new Vec(velocityX, velocityY, velocityZ).mul(20, 20, 20);
    }

    public Pos calculateArrowSpawnPosition(SkyBlockPlayer player) {
        return player.getPosition().add(0, player.getEyeHeight(), 0);
    }
}
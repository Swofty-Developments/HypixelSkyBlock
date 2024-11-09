package net.swofty.types.generic.item.components;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.swofty.commons.item.ReforgeType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.SkyBlockItemComponent;
import net.swofty.types.generic.item.handlers.bow.BowRegistry;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;

import java.util.List;
import java.util.function.BiConsumer;

public class BowComponent extends SkyBlockItemComponent {
    private final BiConsumer<SkyBlockPlayer, SkyBlockItem> shootHandler;

    public BowComponent(BiConsumer<SkyBlockPlayer, SkyBlockItem> shootHandler) {
        this.shootHandler = shootHandler;
        addInheritedComponent(new ExtraRarityComponent("BOW"));
        addInheritedComponent(new QuiverDisplayComponent(false));
        addInheritedComponent(new ReforgableComponent(ReforgeType.BOWS));
        addInheritedComponent(new EnchantableComponent(List.of(EnchantItemGroups.BOW), true));
        addInheritedComponent(new RuneableComponent(RuneableComponent.RuneApplicableTo.BOWS));
    }

    public BowComponent(String handlerId) {
        this(BowRegistry.getHandler(handlerId).getShootHandler());
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
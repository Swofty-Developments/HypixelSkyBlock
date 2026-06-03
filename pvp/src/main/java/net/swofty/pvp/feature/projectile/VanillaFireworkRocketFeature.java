package net.swofty.pvp.feature.projectile;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.event.trait.EntityInstanceEvent;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.pvp.entity.projectile.FireworkRocketEntity;
import net.swofty.pvp.feature.FeatureType;
import net.swofty.pvp.feature.RegistrableFeature;
import net.swofty.pvp.feature.block.BlockFeature;
import net.swofty.pvp.feature.config.DefinedFeature;
import net.swofty.pvp.feature.config.FeatureConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * Vanilla implementation of {@link FireworkRocketFeature}
 */
public class VanillaFireworkRocketFeature implements FireworkRocketFeature, RegistrableFeature {

    private final FeatureConfiguration configuration;
    private BlockFeature blockFeature;

    public static final DefinedFeature<VanillaFireworkRocketFeature> DEFINED = new DefinedFeature<>(
        FeatureType.FIREWORK, VanillaFireworkRocketFeature::new,
        FeatureType.BLOCK
    );

    public VanillaFireworkRocketFeature(FeatureConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void initDependencies() {
        this.blockFeature = configuration.get(FeatureType.BLOCK);
    }

    @Override
    public void init(EventNode<@NotNull EntityInstanceEvent> node) {
        node.addListener(PlayerUseItemOnBlockEvent.class, event -> {
            if (event.getItemStack().material() != Material.FIREWORK_ROCKET) return;

            Player player = event.getPlayer();
            ItemStack itemStack = event.getItemStack();

            var offsetX = event.getCursorPosition().x();
            var offsetZ = event.getCursorPosition().z();
            Point position = getBlockPlacement(
                event.getPosition().add(offsetX, 0, offsetZ),
                event.getBlockFace());

            FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(
                player,
                itemStack,
                false,
                blockFeature);

            fireworkRocketEntity.setInstance(event.getInstance(), position);

            if (player.getGameMode().equals(GameMode.CREATIVE)) return;
            player.setItemInHand(event.getHand(), itemStack.withAmount(itemStack.amount() - 1));
        });

        // TODO add plain PlayerUseItemEvent listener for elytra usage (need elytra first)
    }

    private static Point getBlockPlacement(@NotNull Point position, BlockFace face) {
        position = switch (face) {
            case BlockFace.TOP -> position.add(0, 1, 0);
            case BlockFace.BOTTOM -> position.add(0, -1, 0);
            case BlockFace.NORTH -> position.add(0, 0, -1);
            case BlockFace.SOUTH -> position.add(0, 0, 1);
            case BlockFace.WEST -> position.add(-1, 0, 0);
            case BlockFace.EAST -> position.add(1, 0, 0);
        };
        return position;
    }
}
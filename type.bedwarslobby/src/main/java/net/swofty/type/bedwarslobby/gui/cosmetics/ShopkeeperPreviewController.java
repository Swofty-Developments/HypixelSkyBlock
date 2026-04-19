package net.swofty.type.bedwarslobby.gui.cosmetics;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.collectibles.CollectibleDefinition;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.ScheduleUtility;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class ShopkeeperPreviewController {

    public static final Tag<Boolean> PREVIEW_LOCK_TAG = Tag.Boolean("bedwars_shopkeeper_preview_lock");

    private static final Pos PREVIEW_PLAYER_POSITION = new Pos(-4.5, 109.5, 53.5, 90F, 0F);
    private static final Pos PREVIEW_ENTITY_POSITION = new Pos(-3.5, 108.0, 53.5, -90F, 0F);
    private static final Map<UUID, PreviewState> ACTIVE_PREVIEWS = new ConcurrentHashMap<>();

    private ShopkeeperPreviewController() {
    }

    public static void startPreview(HypixelPlayer player, CollectibleDefinition definition) {
        stopPreview(player);
        if (player.getInstance() == null) {
            return;
        }

        LivingEntity entity = new LivingEntity(resolveEntityType(definition.id()));
        entity.setNoGravity(true);
        entity.setAutoViewable(false);
        entity.setInstance(player.getInstance(), PREVIEW_ENTITY_POSITION);
        entity.addViewer(player);

        PreviewState previewState = new PreviewState(player.getPosition(), player.getGameMode(), player.isFlying(), entity);
        ACTIVE_PREVIEWS.put(player.getUuid(), previewState);

        player.setTag(PREVIEW_LOCK_TAG, true);
        player.setGameMode(GameMode.SPECTATOR);
        player.setFlying(false);
        player.teleport(PREVIEW_PLAYER_POSITION);

        ScheduleUtility.delay(() -> ShopkeeperPreviewController.stopPreview(player), TaskSchedule.seconds(5));
    }

    public static void stopPreview(HypixelPlayer player) {
        player.removeTag(PREVIEW_LOCK_TAG);

        PreviewState previewState = ACTIVE_PREVIEWS.remove(player.getUuid());
        if (previewState == null) {
            return;
        }

        if (previewState.previewEntity() != null && !previewState.previewEntity().isRemoved()) {
            previewState.previewEntity().remove();
        }

        player.setGameMode(previewState.previousMode());
        player.teleport(previewState.previousPosition());
        player.setFlying(previewState.previousFlying());
    }

    public static boolean isLocked(HypixelPlayer player) {
        return Boolean.TRUE.equals(player.getTag(PREVIEW_LOCK_TAG));
    }

    private static EntityType resolveEntityType(String collectibleId) {
        return switch (collectibleId) {
            case "skeleton" -> EntityType.SKELETON;
            case "wither_skeleton" -> EntityType.WITHER_SKELETON;
            case "blaze" -> EntityType.BLAZE;
            case "zombie" -> EntityType.ZOMBIE;
            case "witch" -> EntityType.WITCH;
            case "creeper" -> EntityType.CREEPER;
            default -> EntityType.VILLAGER;
        };
    }

    private record PreviewState(Pos previousPosition, GameMode previousMode, boolean previousFlying,
                                LivingEntity previewEntity) {
    }
}

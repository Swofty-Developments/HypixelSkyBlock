package net.swofty.type.bedwarslobby.gui.cosmetics;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.metadata.avatar.MannequinMeta;
import net.minestom.server.network.player.ResolvableProfile;
import net.minestom.server.tag.Tag;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.collectibles.CollectibleDefinition;
import net.swofty.type.generic.collectibles.bedwars.BedWarsShopkeeperAppearanceService;
import net.swofty.type.generic.gui.v2.View;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.ScheduleUtility;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShopkeeperPreviewController {

    public static final Tag<Boolean> PREVIEW_LOCK_TAG = Tag.Boolean("bedwars_shopkeeper_preview_lock");

    private static final Pos PREVIEW_PLAYER_POSITION = new Pos(-4.5, 109.5, 53.5, -90F, 0F);
    private static final Pos PREVIEW_ENTITY_POSITION = new Pos(-1.5, 109.0, 53.5, 90F, 0F);
    private static final Map<UUID, PreviewState> ACTIVE_PREVIEWS = new ConcurrentHashMap<>();

    public static <S> void startPreview(HypixelPlayer player, CollectibleDefinition definition, View<S> view, S state) {
        player.closeInventory();
        stopPreview(player, view, state);
        if (player.getInstance() == null) {
            return;
        }

        BedWarsShopkeeperAppearanceService.ShopkeeperAppearance appearance =
            BedWarsShopkeeperAppearanceService.resolve(definition);

        Entity entity = createPreviewEntity(appearance);
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

        ScheduleUtility.delay(() -> ShopkeeperPreviewController.stopPreview(player, view, state), TaskSchedule.seconds(5));
    }

    public static <S> void stopPreview(HypixelPlayer player, View<S> view, S state) {
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

        if (view != null && state != null) {
            player.openView(view, state);
        }
    }

    public static boolean isLocked(HypixelPlayer player) {
        return Boolean.TRUE.equals(player.getTag(PREVIEW_LOCK_TAG));
    }

    private static Entity createPreviewEntity(BedWarsShopkeeperAppearanceService.ShopkeeperAppearance appearance) {
        if (appearance.isHuman()) {
            Entity mannequin = new Entity(EntityType.MANNEQUIN);
            mannequin.editEntityMeta(MannequinMeta.class, meta -> meta.setProfile(
                new ResolvableProfile(new PlayerSkin(appearance.textureValue(), appearance.textureSignature()))
            ));
            return mannequin;
        }

        return new LivingEntity(appearance.entityType());
    }

    private record PreviewState(Pos previousPosition, GameMode previousMode, boolean previousFlying,
                                Entity previewEntity) {
    }
}

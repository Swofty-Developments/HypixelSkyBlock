package net.swofty.type.skywarsgame.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.entity.EntityShootEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithBlockEvent;
import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skywarsgame.luckyblock.items.LuckyBlockItemRegistry;
import net.swofty.type.skywarsgame.luckyblock.items.weapons.ExplosiveBow;
import net.swofty.type.skywarsgame.luckyblock.items.weapons.Invisibow;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ActionLuckyBlockArrows implements HypixelEventClass {

    private static final Map<UUID, String> arrowBowMap = new ConcurrentHashMap<>();

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void onArrowShoot(EntityShootEvent event) {
        if (!(event.getEntity() instanceof SkywarsPlayer player)) return;

        ItemStack bow = player.getItemInMainHand();
        String bowId = bow.getTag(LuckyBlockItemRegistry.LUCKY_BLOCK_ITEM_TAG);

        if (bowId != null) {
            arrowBowMap.put(event.getProjectile().getUuid(), bowId);
        }
    }

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void onArrowHitBlock(ProjectileCollideWithBlockEvent event) {
        Entity projectile = event.getEntity();
        String bowId = arrowBowMap.remove(projectile.getUuid());

        if (bowId == null) return;

        handleArrowEffect(bowId, projectile, event.getCollisionPosition(), null);
    }

    @HypixelEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void onArrowHitEntity(ProjectileCollideWithEntityEvent event) {
        Entity projectile = event.getEntity();
        String bowId = arrowBowMap.get(projectile.getUuid());

        if (bowId == null) return;

        Entity target = event.getTarget();
        handleArrowEffect(bowId, projectile, event.getCollisionPosition(), target);

        arrowBowMap.remove(projectile.getUuid());
    }

    private void handleArrowEffect(String bowId, Entity projectile, Pos hitPos, Entity target) {
        Instance instance = projectile.getInstance();
        if (instance == null) return;

        switch (bowId) {
            case ExplosiveBow.ID -> {
                double radius = ExplosiveBow.EXPLOSION_RADIUS;
                for (Entity entity : instance.getNearbyEntities(hitPos, radius)) {
                    if (entity instanceof LivingEntity living && entity != projectile) {
                        Vec direction = entity.getPosition().sub(hitPos).asVec().normalize();
                        double distance = entity.getPosition().distance(hitPos);
                        double force = (1.0 - distance / radius) * 20;
                        living.setVelocity(direction.mul(force).withY(force * 0.5));
                    }
                }
            }

            case Invisibow.ID -> {
                if (target instanceof LivingEntity living) {
                    living.addEffect(new Potion(
                            PotionEffect.INVISIBILITY,
                            (byte) 0,
                            Invisibow.INVISIBILITY_DURATION_TICKS
                    ));
                }
            }
        }
    }
}

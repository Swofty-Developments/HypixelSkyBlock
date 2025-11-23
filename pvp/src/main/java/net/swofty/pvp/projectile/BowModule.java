package net.swofty.pvp.projectile;

import net.swofty.pvp.projectile.entities.ArrowProjectile;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import net.minestom.server.event.item.PlayerCancelItemUseEvent;
import net.minestom.server.event.trait.EntityEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

// https://gist.github.com/hapily04/82fe9bdd6ddd757664701ba2f45ac474
public class BowModule {
    private static final Tag<Long> CHARGE_SINCE_TAG = Tag.Long("bow_charge_since").defaultValue(Long.MAX_VALUE);
    private static final Tag<Double> BOW_POWER = Tag.Double("bow_power").defaultValue(0.0);

    public BowModule(@NotNull EventNode<EntityEvent> node, BiFunction<Player, ItemStack, ? extends AbstractProjectile> arrowSupplier) {
        node.addListener(PlayerBeginItemUseEvent.class, event -> {
            if (event.getItemStack().material() != Material.BOW) return;
            event.getPlayer().setTag(CHARGE_SINCE_TAG, System.currentTimeMillis());
        });
        node.addListener(PlayerCancelItemUseEvent.class, event -> {
            if (event.getItemStack().material() != Material.BOW) return;
            Player player = event.getPlayer();
            long duration = System.currentTimeMillis()-player.getTag(CHARGE_SINCE_TAG);
            double power = getPower(duration);

            if (power < 0.1) return;
            event.getPlayer().setTag(BOW_POWER, power);

            var projectile = arrowSupplier.apply(player, event.getItemStack());
            if (projectile instanceof ArrowProjectile arrow && power == 1) arrow.setCritical(true);

            Pos shootPosition = player.getPosition().add(0, player.getEyeHeight()-0.1, 0);
            projectile.shoot(shootPosition.asVec(), power*3, 1f); // this method already sets the instance
            player.getInstance().playSound(Sound.sound(SoundEvent.ENTITY_ARROW_SHOOT, Sound.Source.PLAYER, 1f, getRandomPitchFromPower(power)), player);
        });
    }

    private double getPower(long duration) {
        double secs = duration/1000.0;
        double pow = (secs * secs + secs * 2.0) / 3.0;
        if (pow > 1) {
            pow = 1;
        }
        return pow;
    }

    private float getRandomPitchFromPower(double power) {
        return (float) (1.0f / (ThreadLocalRandom.current().nextFloat() * 0.4f + 1.2f) + power * 0.5f);
    }
}
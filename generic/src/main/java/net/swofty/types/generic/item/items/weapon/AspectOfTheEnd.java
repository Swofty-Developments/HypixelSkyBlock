package net.swofty.types.generic.item.items.weapon;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.sound.SoundEvent;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.CustomSkyBlockAbility;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.GemstoneItem;
import net.swofty.types.generic.item.impl.StandardItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.user.statistics.TemporaryStatistic;

import java.util.List;

public class AspectOfTheEnd implements CustomSkyBlockItem, CustomSkyBlockAbility, StandardItem, GemstoneItem {
    @Override
    public String getAbilityName() {
        return "Instant Transmission";
    }

    @Override
    public String getAbilityDescription() {
        return "Teleports you §a8 blocks §7ahead and gain §a+50 §f✦ Speed §7for §a3 seconds.";
    }

    @Override
    public void onAbilityUse(SkyBlockPlayer player, SkyBlockItem sItem) {
        Point targetPoint = player.getTargetBlockPosition(8);
        Pos playerPos = player.getPosition();
        Vec playerDirection = player.getPosition().direction();

        Pos toTeleportTo;

        if (targetPoint == null) {
            // Teleport 8 blocks in direction player is facing
            toTeleportTo = playerPos.add(playerDirection.mul(8));
        } else {
            // Move 1 block back to make sure we don't clip into the block
            toTeleportTo = new Pos(targetPoint).add(playerDirection.mul(-1));
        }

        player.teleport(toTeleportTo.add(0, 0.5, 0));
        player.playSound(Sound.sound(SoundEvent.ENTITY_ENDERMAN_TELEPORT, Sound.Source.PLAYER, 1, 1));
        player.getStatistics().boostStatistic(TemporaryStatistic.builder()
                .withStatistics(ItemStatistics.builder().withBase(ItemStatistic.SPEED, 50D).build())
                .withExpirationInTicks(3 * 20)
                .build());
    }

    @Override
    public int getManaCost() {
        return 50;
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 0;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.RIGHT_CLICK;
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockItem instance) {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.DAMAGE, 100D)
                .withBase(ItemStatistic.STRENGTH, 100D)
                .build();
    }

    @Override
    public StandardItemType getStandardItemType() {
        return StandardItemType.SWORD;
    }

    @Override
    public List<GemstoneItemSlot> getGemstoneSlots() {
        return List.of(
                new GemstoneItemSlot(Gemstone.Slots.SAPPHIRE, 50000)
        );
    }
}

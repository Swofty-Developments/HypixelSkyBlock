package net.swofty.types.generic.item.items.weapon;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.sound.SoundEvent;
import net.swofty.types.generic.gems.Gemstone;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.*;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.user.statistics.TemporaryStatistic;
import net.swofty.types.generic.utility.ChatColor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AspectOfTheEnd implements CustomSkyBlockItem, CustomSkyBlockAbility, StandardItem,
                                       DefaultCraftable, GemstoneItem {
    @Override
    public SkyBlockRecipe<?> getRecipe() {
        Map<Character, MaterialQuantifiable> ingredientMap = new HashMap<>();
        ingredientMap.put('A', new MaterialQuantifiable(ItemType.ENCHANTED_EYE_OF_ENDER, 16));
        ingredientMap.put('B', new MaterialQuantifiable(ItemType.ENCHANTED_EYE_OF_ENDER, 16));
        ingredientMap.put('C', new MaterialQuantifiable(ItemType.ENCHANTED_DIAMOND, 1));
        List<String> pattern = List.of(
                "A",
                "A",
                "B");

        return new ShapedRecipe(SkyBlockRecipe.RecipeType.NONE, new SkyBlockItem(ItemType.ASPECT_OF_THE_END), ingredientMap, pattern);
    }

    @Override
    public String getAbilityName() {
        return "Instant Transmission";
    }

    @Override
    public String getAbilityDescription() {
        return "Teleports you " + ChatColor.GREEN + "8 blocks " + ChatColor.GRAY + "ahead and gain " + ChatColor.GREEN + "+50 " + ChatColor.WHITE + "✦ " + ChatColor.WHITE + "Speed " + ChatColor.GRAY + "for " + ChatColor.GREEN + "3 seconds.";
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
                .withStatistic(ItemStatistic.SPEED)
                .withValue(50D)
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
                .with(ItemStatistic.DAMAGE, 100D)
                .with(ItemStatistic.STRENGTH, 100D)
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

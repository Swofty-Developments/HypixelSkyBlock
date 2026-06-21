package net.swofty.type.bedwarsgame.item.impl;

import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.bedwarsgame.TypeBedWarsGameLoader;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class LuckyEquipmentEffects {
    private static final Map<UUID, Long> CHICKEN_HAT_COOLDOWN = new HashMap<>();
    private static final Map<UUID, Long> SQUID_BOOTS_COOLDOWN = new HashMap<>();

    private LuckyEquipmentEffects() {
    }

    public static void tick(BedWarsPlayer player) {
        handleHelmet(player, player.getHelmet());
        handleBoots(player, player.getBoots());
    }

    public static ItemStack equipment(String name, Material material, String action, String... lore) {
        return new LuckySpecialItem().item(name, material, action, 0, lore);
    }

    private static void handleHelmet(BedWarsPlayer player, ItemStack helmet) {
        String action = LuckyCombatEffects.action(helmet);
        if (action == null) return;

        switch (action) {
            case "frog_helmet" -> {
                player.addEffect(new Potion(PotionEffect.JUMP_BOOST, (byte) 4, 40));
                player.addEffect(new Potion(PotionEffect.SLOW_FALLING, (byte) 0, 40));
            }
            case "hot_head" -> {
                player.addEffect(new Potion(PotionEffect.FIRE_RESISTANCE, (byte) 0, 60));
                player.addEffect(new Potion(PotionEffect.SPEED, (byte) 0, 40));
            }
            case "chicken_hat" -> chickenHat(player);
            case "exodus" -> player.addEffect(new Potion(PotionEffect.REGENERATION, (byte) 0, 40));
            default -> {
            }
        }
    }

    private static void handleBoots(BedWarsPlayer player, ItemStack boots) {
        String action = LuckyCombatEffects.action(boots);
        if (action == null) return;

        switch (action) {
            case "slime_boots" -> {
                player.addEffect(new Potion(PotionEffect.SLOW_FALLING, (byte) 0, 40));
                if (player.isOnGround() && player.getVelocity().y() < -0.1) {
                    player.setVelocity(player.getVelocity().withY(7));
                }
            }
            case "squid_boots" -> squidBoots(player);
            case "heat_boots" -> player.addEffect(new Potion(PotionEffect.FIRE_RESISTANCE, (byte) 0, 60));
            default -> {
            }
        }
    }

    private static void chickenHat(BedWarsPlayer player) {
        long now = System.currentTimeMillis();
        if (now - CHICKEN_HAT_COOLDOWN.getOrDefault(player.getUuid(), 0L) < 2500L) {
            return;
        }
        player.getGame().getPlayers().stream()
            .filter(other -> other != player && !other.getTeamKey().equals(player.getTeamKey()))
            .filter(other -> other.getPosition().distance(player.getPosition()) <= 16)
            .findFirst()
            .ifPresent(target -> {
                target.setVelocity(target.getVelocity().add(player.getPosition().direction().mul(5)).add(0, 2, 0));
                CHICKEN_HAT_COOLDOWN.put(player.getUuid(), now);
            });
    }

    private static void squidBoots(BedWarsPlayer player) {
        long now = System.currentTimeMillis();
        if (now - SQUID_BOOTS_COOLDOWN.getOrDefault(player.getUuid(), 0L) < 750L) {
            return;
        }
        var point = player.getPosition().sub(0, 1, 0);
        if (player.getInstance().getBlock(point).isAir()) {
            player.getInstance().setBlock(point, Block.WATER.withTag(TypeBedWarsGameLoader.PLAYER_PLACED_TAG, true));
            SQUID_BOOTS_COOLDOWN.put(player.getUuid(), now);
        }
    }
}

package net.swofty.type.skywarsgame.luckyblock.effects;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

import java.time.Duration;
import java.util.Random;
import java.util.function.BiConsumer;

@Getter
public enum LuckyBlockEnvironmentEffect {
    COBWEB_TRAP(
            "Cobweb Trap",
            "You're stuck in cobwebs!",
            NamedTextColor.WHITE,
            false,
            (player, pos) -> {
                Instance instance = player.getInstance();
                if (instance == null) return;

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        for (int dy = 0; dy <= 1; dy++) {
                            Pos webPos = pos.add(dx, dy, dz);
                            Block current = instance.getBlock(webPos);
                            if (current.isAir()) {
                                instance.setBlock(webPos, Block.COBWEB);
                            }
                        }
                    }
                }

                player.scheduler().buildTask(() -> {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            for (int dy = 0; dy <= 1; dy++) {
                                Pos webPos = pos.add(dx, dy, dz);
                                Block current = instance.getBlock(webPos);
                                if (current.compare(Block.COBWEB)) {
                                    instance.setBlock(webPos, Block.AIR);
                                }
                            }
                        }
                    }
                }).delay(Duration.ofSeconds(5)).schedule();
            }
    ),

    ANVIL_RAIN(
            "Anvil Rain",
            "Watch out above!",
            NamedTextColor.GRAY,
            false,
            (player, pos) -> {
                Instance instance = player.getInstance();
                if (instance == null) return;

                Random random = new Random();
                for (int i = 0; i < 5; i++) {
                    int delay = i * 200;
                    player.scheduler().buildTask(() -> {
                        double offsetX = random.nextDouble() * 4 - 2;
                        double offsetZ = random.nextDouble() * 4 - 2;
                        Pos anvilPos = player.getPosition().add(offsetX, 10, offsetZ);

                        Entity fallingAnvil = new Entity(EntityType.FALLING_BLOCK);
                        fallingAnvil.setInstance(instance, anvilPos);
                        fallingAnvil.setNoGravity(false);

                        fallingAnvil.scheduler().buildTask(fallingAnvil::remove)
                                .delay(Duration.ofSeconds(5))
                                .schedule();
                    }).delay(Duration.ofMillis(delay)).schedule();
                }
            }
    ),

    LAVA_PIT(
            "Lava Pit",
            "The floor is lava!",
            NamedTextColor.RED,
            false,
            (player, pos) -> {
                Instance instance = player.getInstance();
                if (instance == null) return;

                Pos underPos = pos.add(0, -1, 0);
                Block underBlock = instance.getBlock(underPos);

                if (underBlock.isSolid() && !underBlock.compare(Block.BEDROCK)) {
                    instance.setBlock(underPos, Block.LAVA);

                    player.scheduler().buildTask(() -> {
                        Block current = instance.getBlock(underPos);
                        if (current.compare(Block.LAVA)) {
                            instance.setBlock(underPos, Block.OBSIDIAN);
                        }
                    }).delay(Duration.ofSeconds(10)).schedule();
                }

                player.damage(Damage.fromEntity(null, 4.0f));
                player.setFireTicks(60);
            }
    ),

    LIGHTNING_STRIKE(
            "Lightning Strike",
            "ZAP!",
            NamedTextColor.YELLOW,
            false,
            (player, pos) -> {
                Instance instance = player.getInstance();
                if (instance == null) return;

                Entity lightning = new Entity(EntityType.LIGHTNING_BOLT);
                lightning.setInstance(instance, player.getPosition());

                lightning.scheduler().buildTask(lightning::remove)
                        .delay(Duration.ofMillis(300))
                        .schedule();

                player.damage(Damage.fromEntity(null, 5.0f));
                player.setFireTicks(20);

                player.addEffect(new Potion(PotionEffect.SLOWNESS, (byte) 1, 60));
            }
    ),

    TNT_SURROUND(
            "TNT Trap",
            "Run! TNT everywhere!",
            NamedTextColor.DARK_RED,
            false,
            (player, pos) -> {
                Instance instance = player.getInstance();
                if (instance == null) return;

                Random random = new Random();

                for (int i = 0; i < 4; i++) {
                    double angle = (Math.PI * 2 / 4) * i;
                    double offsetX = Math.cos(angle) * 3;
                    double offsetZ = Math.sin(angle) * 3;
                    Pos tntPos = pos.add(offsetX, 0, offsetZ);

                    Entity tnt = new Entity(EntityType.TNT);
                    tnt.setInstance(instance, tntPos);

                    if (tnt.getEntityMeta() instanceof net.minestom.server.entity.metadata.other.PrimedTntMeta tntMeta) {
                        tntMeta.setFuseTime(60 + random.nextInt(20));
                    }
                }
            }
    ),

    RANDOM_TELEPORT(
            "Random Teleport",
            "Whoooosh!",
            NamedTextColor.LIGHT_PURPLE,
            false,
            (player, pos) -> {
                Random random = new Random();

                double offsetX = random.nextDouble() * 20 - 10;
                double offsetZ = random.nextDouble() * 20 - 10;
                double offsetY = random.nextDouble() * 5;

                Pos newPos = player.getPosition().add(offsetX, offsetY, offsetZ);
                player.teleport(newPos);

                player.addEffect(new Potion(PotionEffect.RESISTANCE, (byte) 4, 40));
            }
    ),

    BRIDGE_BUILD(
            "Instant Bridge",
            "A bridge appears before you!",
            NamedTextColor.GOLD,
            true,
            (player, pos) -> {
                Instance instance = player.getInstance();
                if (instance == null) return;

                float yaw = player.getPosition().yaw();
                double radians = Math.toRadians(yaw);
                int dx = (int) Math.round(-Math.sin(radians));
                int dz = (int) Math.round(Math.cos(radians));

                Pos startPos = pos.add(0, -1, 0);
                for (int i = 1; i <= 10; i++) {
                    Pos bridgePos = startPos.add(dx * i, 0, dz * i);
                    Block current = instance.getBlock(bridgePos);
                    if (current.isAir()) {
                        instance.setBlock(bridgePos, Block.OAK_PLANKS);
                    }
                }
            }
    ),

    PROTECTIVE_WALL(
            "Protective Wall",
            "A wall rises to protect you!",
            NamedTextColor.GRAY,
            true,
            (player, pos) -> {
                Instance instance = player.getInstance();
                if (instance == null) return;

                int radius = 3;
                for (int angle = 0; angle < 360; angle += 15) {
                    double radians = Math.toRadians(angle);
                    int dx = (int) Math.round(Math.cos(radians) * radius);
                    int dz = (int) Math.round(Math.sin(radians) * radius);

                    for (int dy = 0; dy < 3; dy++) {
                        Pos wallPos = pos.add(dx, dy, dz);
                        Block current = instance.getBlock(wallPos);
                        if (current.isAir()) {
                            instance.setBlock(wallPos, Block.COBBLESTONE);
                        }
                    }
                }
            }
    ),

    LAUNCH_PAD(
            "Launch Pad",
            "WEEEEE!",
            NamedTextColor.GREEN,
            true,
            (player, pos) -> {
                player.setVelocity(player.getVelocity().add(0, 35, 0));

                player.addEffect(new Potion(PotionEffect.SLOW_FALLING, (byte) 0, 200));
            }
    ),

    ARROW_RAIN(
            "Arrow Rain",
            "Arrows from the sky!",
            NamedTextColor.DARK_GRAY,
            false,
            (player, pos) -> {
                Instance instance = player.getInstance();
                if (instance == null) return;

                Random random = new Random();

                for (int i = 0; i < 20; i++) {
                    int delay = i * 100;
                    player.scheduler().buildTask(() -> {
                        double offsetX = random.nextDouble() * 6 - 3;
                        double offsetZ = random.nextDouble() * 6 - 3;
                        Pos arrowPos = player.getPosition().add(offsetX, 15, offsetZ);

                        EntityProjectile arrow = new EntityProjectile(null, EntityType.ARROW);
                        arrow.setInstance(instance, arrowPos);
                        arrow.setVelocity(arrow.getVelocity().add(0, -30, 0));

                        arrow.scheduler().buildTask(arrow::remove)
                                .delay(Duration.ofSeconds(5))
                                .schedule();
                    }).delay(Duration.ofMillis(delay)).schedule();
                }
            }
    ),

    BLESSING(
            "Lucky Blessing",
            "You feel incredibly lucky!",
            NamedTextColor.LIGHT_PURPLE,
            true,
            (player, pos) -> {
                player.addEffect(new Potion(PotionEffect.SPEED, (byte) 1, 600));
                player.addEffect(new Potion(PotionEffect.STRENGTH, (byte) 0, 600));
                player.addEffect(new Potion(PotionEffect.JUMP_BOOST, (byte) 1, 600));
                player.addEffect(new Potion(PotionEffect.REGENERATION, (byte) 0, 200));
            }
    ),

    CURSE(
            "Cursed",
            "You feel incredibly unlucky!",
            NamedTextColor.DARK_PURPLE,
            false,
            (player, pos) -> {
                player.addEffect(new Potion(PotionEffect.SLOWNESS, (byte) 1, 200));
                player.addEffect(new Potion(PotionEffect.WEAKNESS, (byte) 0, 200));
                player.addEffect(new Potion(PotionEffect.MINING_FATIGUE, (byte) 1, 200));
                player.addEffect(new Potion(PotionEffect.HUNGER, (byte) 1, 200));
            }
    );

    private static final Random RANDOM = new Random();

    private final String displayName;
    private final String message;
    private final NamedTextColor color;
    private final boolean isGood;
    private final BiConsumer<SkywarsPlayer, Pos> effectAction;

    LuckyBlockEnvironmentEffect(String displayName, String message, NamedTextColor color,
                                boolean isGood, BiConsumer<SkywarsPlayer, Pos> effectAction) {
        this.displayName = displayName;
        this.message = message;
        this.color = color;
        this.isGood = isGood;
        this.effectAction = effectAction;
    }

    public void apply(SkywarsPlayer player, Pos position) {
        player.sendMessage(Component.text(message, color));
        effectAction.accept(player, position);
    }

    public Component getMessageComponent() {
        return Component.text(message, color);
    }

    public static LuckyBlockEnvironmentEffect randomGood() {
        LuckyBlockEnvironmentEffect[] goodEffects = java.util.Arrays.stream(values())
                .filter(e -> e.isGood)
                .toArray(LuckyBlockEnvironmentEffect[]::new);
        return goodEffects[RANDOM.nextInt(goodEffects.length)];
    }

    public static LuckyBlockEnvironmentEffect randomBad() {
        LuckyBlockEnvironmentEffect[] badEffects = java.util.Arrays.stream(values())
                .filter(e -> !e.isGood)
                .toArray(LuckyBlockEnvironmentEffect[]::new);
        return badEffects[RANDOM.nextInt(badEffects.length)];
    }

    public static LuckyBlockEnvironmentEffect random() {
        LuckyBlockEnvironmentEffect[] effects = values();
        return effects[RANDOM.nextInt(effects.length)];
    }
}

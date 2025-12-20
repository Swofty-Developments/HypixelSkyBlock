package net.swofty.type.hub.events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.tag.Tag;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.generic.utility.TimedActivityHandler;
import net.swofty.type.skyblockgeneric.targetpractice.PracticeTargets;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointArcheryPractice;
import net.swofty.type.skyblockgeneric.event.custom.ArrowHitBlockEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.BowComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockActionBar;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActionPlayerHandleTargetArchery implements HypixelEventClass {
    private static final Pos ARCHERY_POSITION = DatapointArcheryPractice.getArcheryPosition();
    private static final int TOTAL_TARGETS = 16;
    private static final Map<UUID, TimedActivityHandler> activeArcheryHandlers = new HashMap<>();

    public ActionPlayerHandleTargetArchery() {}

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true, isAsync = true)
    public void handleOnJoin(PlayerSpawnEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!event.isFirstSpawn()) return;

        player.getArcheryPracticeData().initializeHologram(player);
    }


    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, isAsync = true)
    public void handleOnArrowHit(ArrowHitBlockEvent event) {
        if (!event.isSkyBlockPlayer()) return;
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getShooter();
        if (!TimedActivityHandler.hasActiveHandler(player)) return;

        Block hitBlock = event.getHitBlock();
        Point hitPosition = event.getHitPosition();

        PracticeTargets target = PracticeTargets.getFromPosition(hitPosition.asPos());
        if (target == null) return;

        DatapointArcheryPractice.ArcheryPracticeData data = player.getArcheryPracticeData();
        if (data.hasHitTarget(target)) return;

        Block lantern = Block.LANTERN.withTag(Tag.Boolean("lit"), true);
        player.getInstance().setBlock(hitPosition, lantern);
        data.incrementTargetsHit(target);

        if (data.getTargetsHitList().size() >= TOTAL_TARGETS) {
            TimedActivityHandler handler = TimedActivityHandler.getActiveHandler(player);
            handler.complete();

            data.incrementLevel(player);
        }
    }

    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = true, isAsync = true)
    public void handleOnMove(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        boolean isNearArchery = event.getNewPosition().distance(ARCHERY_POSITION) < 0.5;
        boolean wasNearArchery = event.getPlayer().getPosition().distance(ARCHERY_POSITION) < 0.5;

        if (isNearArchery == wasNearArchery) return;

        if (isNearArchery) {
            Map<Integer, SkyBlockItem> bows = player.getAllOfComponentInInventory(BowComponent.class);
            if (bows.isEmpty()) {
                player.sendMessage("§cYou need a bow to shoot the targets! Purchase one from the Weaponsmith upstairs!");
                return;
            }

            DatapointArcheryPractice.ArcheryPracticeData data = player.getArcheryPracticeData();
            DatapointArcheryPractice.TargetPracticeLevels practiceLevel = data.getTargetPracticeLevel();

            if (practiceLevel == DatapointArcheryPractice.TargetPracticeLevels.CONCLUDED) {
                player.sendMessage("§cYou have already completed all Target Practice levels!");
                return;
            }

            startArcheryPractice(player, practiceLevel);
            return;
        }

        if (activeArcheryHandlers.containsKey(player.getUuid())) {
            TimedActivityHandler handler = activeArcheryHandlers.get(player.getUuid());
            handler.cancel();
            activeArcheryHandlers.remove(player.getUuid());
            return;
        }
    }

    private static void startArcheryPractice(SkyBlockPlayer player, DatapointArcheryPractice.TargetPracticeLevels practiceLevel) {
        TimedActivityHandler handler = TimedActivityHandler.<SkyBlockPlayer>builder(player)
                .countdownSeconds(3)
                .activityDurationSeconds(practiceLevel.getTimeLimitSeconds())
                .onCountdownTick((p, remaining) -> {
                    String color = switch (remaining) {
                        case 3 -> "§e";
                        case 2 -> "§6";
                        case 1 -> "§c";
                        default -> "§f";
                    };

                    p.showTitle(Title.title(
                            Component.text(color + remaining),
                            Component.text("§cShoot all the lanterns!"),
                            Title.Times.times(Duration.ZERO, Duration.ofMillis(1100), Duration.ZERO)
                    ));
                })
                .onActivityStart(p -> {
                    p.showTitle(Title.title(
                            Component.text("§aGO!"),
                            Component.empty(),
                            Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofMillis(250))
                    ));

                    updateActionBar(p, practiceLevel.getTimeLimitSeconds(), 0);
                })
                .onActivityTick((p, elapsed) -> {
                    int remaining = practiceLevel.getTimeLimitSeconds() - elapsed;
                    int targetsHit = p.getArcheryPracticeData().getTargetsHitList().size();
                    updateActionBar(p, remaining, targetsHit);
                })
                .onActivityComplete(p -> {
                    int targetsHit = p.getArcheryPracticeData().getTargetsHitList().size();
                    if (targetsHit >= TOTAL_TARGETS) {
                        p.sendMessage("§aYou've successfully completed Target Practice " + StringUtility.getAsRomanNumeral(practiceLevel.getLevelNumber()));
                    } else {
                        p.sendMessage("§cYou ran out of time!");
                    }
                    p.getArcheryPracticeData().resetTargetsHit();
                })
                .onActivityCancelled(p -> {
                    p.sendMessage("§cCancelled! You cannot leave the pressure plate!");
                    p.getArcheryPracticeData().resetTargetsHit();
                })
                .build();
        handler.start();
        activeArcheryHandlers.put(player.getUuid(), handler);
    }

    private static void updateActionBar(SkyBlockPlayer player, int timeRemaining, int targetsHit) {
        SkyBlockActionBar actionBar = SkyBlockActionBar.getFor(player);

        // Health section: "Time Left: Xs"
        actionBar.addReplacement(
                SkyBlockActionBar.BarSection.HEALTH,
                new SkyBlockActionBar.DisplayReplacement(
                        "§eTime Left: §f" + timeRemaining + "s",
                        25,
                        100
                )
        );

        // Defense section: empty
        actionBar.addReplacement(
                SkyBlockActionBar.BarSection.DEFENSE,
                new SkyBlockActionBar.DisplayReplacement(
                        "",
                        25,
                        100
                )
        );

        // Mana section: "Targets: X/16"
        actionBar.addReplacement(
                SkyBlockActionBar.BarSection.MANA,
                new SkyBlockActionBar.DisplayReplacement(
                        "§eTargets: §a" + targetsHit + " §e/ §a" + TOTAL_TARGETS,
                        25,
                        100
                )
        );
    }
}

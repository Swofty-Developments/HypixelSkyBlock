package net.swofty.type.replayviewer.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.swofty.type.generic.data.datapoints.DatapointReplaySettings;
import net.swofty.type.generic.data.handlers.ReplayDataHandler;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReplaySettingsUtil {

    private static final short[] FLY_SPEED_PRESETS = {1, 2, 3, 4, 5};

    public static DatapointReplaySettings.ReplaySettings getSettings(HypixelPlayer player) {
        ReplayDataHandler handler = ReplayDataHandler.getUser(player);
        if (handler == null) {
            return DatapointReplaySettings.ReplaySettings.createDefault();
        }

        DatapointReplaySettings datapoint = handler.get(
            ReplayDataHandler.Data.REPLAY_SETTINGS,
            DatapointReplaySettings.class
        );

        DatapointReplaySettings.ReplaySettings settings = datapoint.getValue();
        if (settings == null) {
            settings = DatapointReplaySettings.ReplaySettings.createDefault();
            datapoint.setValue(settings);
        }
        return settings;
    }

    public static boolean updateSettings(HypixelPlayer player, Consumer<DatapointReplaySettings.ReplaySettings> updater) {
        ReplayDataHandler handler = ReplayDataHandler.getUser(player);
        if (handler == null) {
            return false;
        }

        DatapointReplaySettings datapoint = handler.get(
            ReplayDataHandler.Data.REPLAY_SETTINGS,
            DatapointReplaySettings.class
        );

        DatapointReplaySettings.ReplaySettings settings = datapoint.getValue();
        if (settings == null) {
            settings = DatapointReplaySettings.ReplaySettings.createDefault();
        }

        updater.accept(settings);
        datapoint.setValue(settings);
        return true;
    }

    public static void applyVisualSettings(HypixelPlayer player) {
        applyVisualSettings(player, getSettings(player));
    }

    public static void applyVisualSettings(HypixelPlayer player, DatapointReplaySettings.ReplaySettings settings) {
        player.setFlyingSpeed(toMinestomFlySpeed(settings.getFlySpeed()));

        if (settings.isNightVision()) {
            player.addEffect(new Potion(PotionEffect.NIGHT_VISION, (byte) 0, -1));
        } else {
            player.removeEffect(PotionEffect.NIGHT_VISION);
        }
    }

    public static short cycleFlySpeed(short previous) {
        for (short preset : FLY_SPEED_PRESETS) {
            if (preset > previous) {
                return preset;
            }
        }
        return FLY_SPEED_PRESETS[0];
    }

    // I don't know how realistic this one is, almost could be moved to a more generic util than here
    // FlySpeedCommand#getRealMoveSpeed has a similar one to the Essentials implementation
    private static float toMinestomFlySpeed(short userSpeed) {
        return Math.clamp(userSpeed, 1, 10) * 0.1f;
    }
}
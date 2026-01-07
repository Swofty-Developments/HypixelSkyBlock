package net.swofty.type.bedwarslobby.hologram;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;
import net.swofty.commons.bedwars.BedwarsLeaderboardMode;
import net.swofty.commons.bedwars.BedwarsLeaderboardPeriod;
import net.swofty.commons.bedwars.BedwarsLeaderboardView;
import net.swofty.commons.bedwars.BedwarsTextAlignment;
import net.swofty.commons.bedwars.LeaderboardPreferences;
import net.swofty.type.generic.data.datapoints.DatapointLeaderboardPreferences;
import net.swofty.type.generic.data.handlers.BedWarsDataHandler;
import net.swofty.type.generic.entity.InteractionEntity;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;
import net.swofty.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LeaderboardHologramManager {
	private static final Map<UUID, PlayerLeaderboardState> playerStates = new ConcurrentHashMap<>();
	private static final List<InteractionEntity> clickZones = new ArrayList<>();
	private static boolean initialized = false;
	private static final Pos SETTINGS_HOLOGRAM_POS = new Pos(5, 68.2, -26);

	public record PlayerLeaderboardState(
			BedwarsLeaderboardPeriod period,
			BedwarsLeaderboardMode mode,
			BedwarsLeaderboardView view,
			BedwarsTextAlignment textAlignment
	) {
		public static PlayerLeaderboardState defaults() {
			return new PlayerLeaderboardState(
					BedwarsLeaderboardPeriod.LIFETIME,
					BedwarsLeaderboardMode.ALL,
					BedwarsLeaderboardView.TOP_10,
					BedwarsTextAlignment.CENTER
			);
		}
	}

	public static void initialize(Instance instance) {
		if (initialized) return;
		initialized = true;

		for (BedWarsLeaderboardHologram hologram : BedWarsLeaderboardHologram.values()) {
			createClickZone(instance, hologram);
		}
		createSettingsClickZone(instance);

		Logger.info("LeaderboardHologramManager initialized with {} holograms + settings", BedWarsLeaderboardHologram.values().length);
	}

	private static void createSettingsClickZone(Instance instance) {
		InteractionEntity clickZone = new InteractionEntity(2.5f, 3.0f, (player, event) -> {
			if (player instanceof HypixelPlayer hypixelPlayer) {
				new net.swofty.type.bedwarslobby.gui.GUILeaderboardSettings().open(hypixelPlayer);
			}
		});
		clickZone.setInstance(instance, SETTINGS_HOLOGRAM_POS.add(0, 0, 0));
		clickZones.add(clickZone);
	}

	private static void createClickZone(Instance instance, BedWarsLeaderboardHologram hologram) {
		Pos pos = hologram.getPosition();

		InteractionEntity clickZone = new InteractionEntity(3.5f, 9.0f, (player, event) -> {
			if (player instanceof HypixelPlayer hypixelPlayer) {
				handleClick(hypixelPlayer, hologram, event);
			}
		});
		clickZone.setInstance(instance, pos.add(0, 4, 0));
		clickZones.add(clickZone);
	}

	public static void handleClick(HypixelPlayer player, BedWarsLeaderboardHologram hologram, PlayerEntityInteractEvent event) {
		new net.swofty.type.bedwarslobby.gui.GUILeaderboardSettings().open(player);
	}

	public static PlayerLeaderboardState getState(UUID playerUuid) {
		return playerStates.getOrDefault(playerUuid, PlayerLeaderboardState.defaults());
	}

	public static void setState(UUID playerUuid, PlayerLeaderboardState state) {
		playerStates.put(playerUuid, state);
	}

	public static void saveStateToDataHandler(HypixelPlayer player, PlayerLeaderboardState state) {
		BedWarsDataHandler handler = BedWarsDataHandler.getUser(player);
		if (handler == null) return;

		LeaderboardPreferences prefs = new LeaderboardPreferences(
				state.period(), state.mode(), state.view(), state.textAlignment());
		handler.get(BedWarsDataHandler.Data.LEADERBOARD_PREFS, DatapointLeaderboardPreferences.class)
				.setValue(prefs);
	}

	public static void loadStateFromDataHandler(HypixelPlayer player) {
		BedWarsDataHandler handler = BedWarsDataHandler.getUser(player);
		if (handler == null) return;

		LeaderboardPreferences prefs = handler.get(BedWarsDataHandler.Data.LEADERBOARD_PREFS,
				DatapointLeaderboardPreferences.class).getValue();

		setState(player.getUuid(), new PlayerLeaderboardState(
				prefs.period(), prefs.mode(), prefs.view(), prefs.textAlignment()));
	}

	public static void spawnHologramsForPlayer(HypixelPlayer player) {
		loadStateFromDataHandler(player);

		for (BedWarsLeaderboardHologram hologram : BedWarsLeaderboardHologram.values()) {
			refreshHologramForPlayer(player, hologram);
		}
		refreshSettingsHologramForPlayer(player);

        for (InteractionEntity clickZone : clickZones) {
            clickZone.addViewer(player);
        }
	}

	public static void refreshHologramForPlayer(HypixelPlayer player, BedWarsLeaderboardHologram hologram) {
		PlayerLeaderboardState state = getState(player.getUuid());
		String[] lines = hologram.getHologramLines(player, state);

		PlayerHolograms.ExternalPlayerHologram externalHologram = PlayerHolograms.ExternalPlayerHologram.builder()
				.pos(hologram.getPosition())
				.text(lines)
				.player(player)
				.instance(player.getInstance())
				.spacing(0.42)
				.displayFunction(p -> {
					PlayerLeaderboardState s = getState(p.getUuid());
					return hologram.getHologramLines(p, s);
				})
				.build();

		PlayerHolograms.removeExternalPlayerHologramsAt(player, hologram.getPosition());
		PlayerHolograms.addExternalPlayerHologram(externalHologram);
	}

	public static void refreshSettingsHologramForPlayer(HypixelPlayer player) {
		PlayerLeaderboardState state = getState(player.getUuid());
		String[] lines = getSettingsHologramLines(state);

		PlayerHolograms.ExternalPlayerHologram externalHologram = PlayerHolograms.ExternalPlayerHologram.builder()
				.pos(SETTINGS_HOLOGRAM_POS)
				.text(lines)
				.player(player)
				.instance(player.getInstance())
				.spacing(0.42)
				.displayFunction(p -> {
					PlayerLeaderboardState s = getState(p.getUuid());
					return getSettingsHologramLines(s);
				})
				.build();

		PlayerHolograms.removeExternalPlayerHologramsAt(player, SETTINGS_HOLOGRAM_POS);
		PlayerHolograms.addExternalPlayerHologram(externalHologram);
	}

	private static String[] getSettingsHologramLines(PlayerLeaderboardState state) {
		String viewDisplay = state.view() == BedwarsLeaderboardView.TOP_10 ? "Top 10" : "Players Near";
		return new String[] {
				"§b§nLeaderboard Settings",
				"§7Mode: §a" + state.mode().getDisplayName(),
				"§7Time: §a" + state.period().getDisplayName(),
				"§7View: §a" + viewDisplay,
				"§7Players: §aAll",
				"§6Click to change settings!"
		};
	}

	public static void refreshAllHologramsForPlayer(HypixelPlayer player) {
		for (BedWarsLeaderboardHologram hologram : BedWarsLeaderboardHologram.values()) {
			refreshHologramForPlayer(player, hologram);
		}
		refreshSettingsHologramForPlayer(player);
	}

	public static void removeHologramsForPlayer(HypixelPlayer player) {
		for (BedWarsLeaderboardHologram hologram : BedWarsLeaderboardHologram.values()) {
			PlayerHolograms.removeExternalPlayerHologramsAt(player, hologram.getPosition());
		}
		PlayerHolograms.removeExternalPlayerHologramsAt(player, SETTINGS_HOLOGRAM_POS);
		playerStates.remove(player.getUuid());
	}

	public static void shutdown() {
		for (InteractionEntity clickZone : clickZones) {
			clickZone.remove();
		}
		clickZones.clear();
		playerStates.clear();
		initialized = false;
	}
}

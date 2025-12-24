package net.swofty.type.bedwarsgame.game;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.MapTeam;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import net.swofty.type.bedwarsgame.entity.TextDisplayEntity;
import org.intellij.lang.annotations.Subst;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.*;

public final class GeneratorManager implements GameEventManager.Listener {
	private final Game game;
	private final Map<TeamKey, List<Task>> teamGeneratorTasks = new EnumMap<>(TeamKey.class);
	private final Map<String, List<GeneratorDisplay>> generatorDisplays = new HashMap<>();
	private final Map<String, GeneratorLimits> generatorLimits = new HashMap<>();
	private Task globalTicker;
	private long lastDiamondDelay = -1;
	private long lastEmeraldDelay = -1;

	public GeneratorManager(Game game) {
		this.game = game;
		if (game.getEventManager() != null) {
			game.getEventManager().addListener(this);
		}
	}

	public void startTeamGenerators(Map<TeamKey, MapTeam> activeTeams) {
		BedWarsMapsConfig.MapEntry.MapConfiguration mapConfig = game.getMapEntry().getConfiguration();
		BedWarsMapsConfig.GeneratorSpeed generatorSpeed = mapConfig.getGeneratorSpeed();

		if (generatorSpeed == null) {
			Logger.warn("No generator speed configured for map");
			return;
		}

		activeTeams.forEach((teamKey, team) -> {
			BedWarsMapsConfig.Position genLocation = team.getGenerator();
			if (genLocation == null) return;

			Pos spawnPosition = new Pos(genLocation.x(), genLocation.y(), genLocation.z());

			// Start iron generator
			startTeamGenerator(teamKey, "iron", generatorSpeed.getIronAmount(),
					generatorSpeed.getIronDelaySeconds(), spawnPosition);

			// Start gold generator
			startTeamGenerator(teamKey, "gold", generatorSpeed.getGoldAmount(),
					generatorSpeed.getGoldDelaySeconds(), spawnPosition);
		});
	}

	private void startTeamGenerator(TeamKey teamKey, String materialType,
	                                int baseAmount, int baseDelay, Pos spawnPosition) {
		Material itemMaterial = getMaterialFromType(materialType);
		if (itemMaterial == null) {
			Logger.warn("Invalid material type: {} for team {}", materialType, teamKey.getName());
			return;
		}


		Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
			if (game.getGameStatus() != GameStatus.IN_PROGRESS) return;

			int forgeLevel = game.getTeamManager().getTeamUpgradeLevel(teamKey, "forge");
			double multiplier = calculateForgeMultiplier(itemMaterial, forgeLevel);

			int finalAmount = (int) Math.round(baseAmount * multiplier);
			if (finalAmount == 0 && baseAmount > 0 && multiplier > 1.0) finalAmount = 1;

			if (finalAmount > 0) {
				spawnItem(itemMaterial, finalAmount, spawnPosition, Duration.ofMillis(500));
			}
		}).delay(TaskSchedule.seconds(baseDelay)).repeat(TaskSchedule.seconds(baseDelay)).schedule();

		teamGeneratorTasks.computeIfAbsent(teamKey, k -> new ArrayList<>()).add(task);
	}

	public void startGlobalGenerators() {
		BedWarsMapsConfig.MapEntry.MapConfiguration mapConfig = game.getMapEntry().getConfiguration();
		if (mapConfig.getGlobal_generator() == null) return;

		mapConfig.getGlobal_generator().forEach(this::setupGlobalGenerator);

		if (globalTicker != null) globalTicker.cancel();
		globalTicker = MinecraftServer.getSchedulerManager().buildTask(() -> {
			if (game.getGameStatus() != GameStatus.IN_PROGRESS) return;
			updateGeneratorDisplays();
			tickGlobalGenerators();
		}).delay(TaskSchedule.seconds(1)).repeat(TaskSchedule.seconds(1)).schedule();
	}

	private void setupGlobalGenerator(String generatorType, BedWarsMapsConfig.MapEntry.MapConfiguration.GlobalGenerator config) {
		Material itemMaterial = Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, generatorType));
		if (itemMaterial == null) {
			Logger.warn("Invalid material for global generator: {}", generatorType);
			return;
		}

		List<BedWarsMapsConfig.Position> locations = config.getLocations();
		if (locations == null || locations.isEmpty()) return;

		if (generatorType.equals("diamond") || generatorType.equals("emerald")) {
			long delaySeconds = generatorType.equals("diamond")
					? game.getEventManager().getDiamondDelaySeconds()
					: game.getEventManager().getEmeraldDelaySeconds();

			setupGlobalGeneratorDisplays(generatorType, locations, (int) delaySeconds);
			generatorLimits.put(generatorType, new GeneratorLimits(
					itemMaterial, config.getAmount(), config.getMax(), locations));
		}
	}

	private void setupGlobalGeneratorDisplays(String generatorType, List<BedWarsMapsConfig.Position> locations, int delaySeconds) {
		NamedTextColor color = generatorType.equals("diamond") ? NamedTextColor.AQUA : NamedTextColor.GREEN;
		String capitalizedType = Character.toUpperCase(generatorType.charAt(0)) + generatorType.substring(1);

		for (BedWarsMapsConfig.Position location : locations) {
			double locY = location.y() + 4.0;

			TextDisplayEntity tierDisplay = new TextDisplayEntity(
					Component.text("Tier I").color(NamedTextColor.YELLOW));
			tierDisplay.setInstance(game.getInstanceContainer(), new Pos(location.x(), locY, location.z()));

			locY -= 0.3;
			TextDisplayEntity titleDisplay = new TextDisplayEntity(
					Component.text(capitalizedType).color(color).decorate(TextDecoration.BOLD));
			titleDisplay.setInstance(game.getInstanceContainer(), new Pos(location.x(), locY, location.z()));

			locY -= 0.3;
			TextDisplayEntity spawnDisplay = new TextDisplayEntity(
					MiniMessage.miniMessage().deserialize("<yellow>Spawns in <red>" + delaySeconds + "</red> seconds!</yellow>"));
			spawnDisplay.setInstance(game.getInstanceContainer(), new Pos(location.x(), locY, location.z()));

			generatorDisplays.computeIfAbsent(generatorType, k -> new ArrayList<>())
					.add(new GeneratorDisplay(tierDisplay, spawnDisplay, delaySeconds));
		}
	}

	private void tickGlobalGenerators() {
		long diamondDelay = game.getEventManager().getDiamondDelaySeconds();
		long emeraldDelay = game.getEventManager().getEmeraldDelaySeconds();
		resetDisplaysIfEventChanged((int) diamondDelay, (int) emeraldDelay);

		long now = System.currentTimeMillis() / 1000L;

		for (Map.Entry<String, GeneratorLimits> entry : generatorLimits.entrySet()) {
			String type = entry.getKey();
			GeneratorLimits limits = entry.getValue();
			long delay = type.equals("diamond") ? diamondDelay : emeraldDelay;

			if (delay <= 0 || (now % delay) != 0) continue;

			for (BedWarsMapsConfig.Position location : limits.locations) {
				Pos spawnPos = new Pos(location.x(), location.y(), location.z());
				long currentItemCount = game.getInstanceContainer().getNearbyEntities(spawnPos, 1.5)
						.stream()
						.filter(ItemEntity.class::isInstance)
						.map(ItemEntity.class::cast)
						.filter(entity -> entity.getItemStack().material() == limits.material)
						.mapToLong(entity -> entity.getItemStack().amount())
						.sum();

				if (currentItemCount < limits.maxAmount) {
					spawnItem(limits.material, limits.amount, spawnPos, Duration.ofSeconds(1));
				}
			}
		}
	}

	private void resetDisplaysIfEventChanged(int diamondDelay, int emeraldDelay) {
		if (lastDiamondDelay == diamondDelay && lastEmeraldDelay == emeraldDelay) return;

		for (Map.Entry<String, List<GeneratorDisplay>> e : generatorDisplays.entrySet()) {
			int max = e.getKey().equals("diamond") ? diamondDelay : emeraldDelay;
			for (GeneratorDisplay d : e.getValue()) {
				d.maxCountdown = max;
				d.countdown = max;
				d.tierDisplay.setText(Component.text(getTierLabelFor(e.getKey())).color(NamedTextColor.YELLOW));
			}
		}

		lastDiamondDelay = diamondDelay;
		lastEmeraldDelay = emeraldDelay;
	}

	private void updateGeneratorDisplays() {
		for (List<GeneratorDisplay> displays : generatorDisplays.values()) {
			for (GeneratorDisplay display : displays) {
				display.countdown--;
				if (display.countdown <= 0) display.countdown = display.maxCountdown;

				display.spawnDisplay.setText(MiniMessage.miniMessage().deserialize(
						"<yellow>Spawns in <red>" + display.countdown + "</red> seconds!</yellow>"));
			}
		}
	}

	private String getTierLabelFor(String type) {
		GameEventPosition e = game.getEventManager().getCurrentEvent();
		int tier = 1;

		if ("diamond".equals(type)) {
			if (e == GameEventPosition.DIAMOND_2 || e == GameEventPosition.EMERALD_2) tier = 2;
			if (e == GameEventPosition.DIAMOND_3 || e == GameEventPosition.EMERALD_3) tier = 3;
		} else if ("emerald".equals(type)) {
			if (e == GameEventPosition.EMERALD_2) tier = 2;
			if (e == GameEventPosition.EMERALD_3) tier = 3;
		}

		return switch (tier) {
			case 2 -> "Tier II";
			case 3 -> "Tier III";
			default -> "Tier I";
		};
	}

	private void spawnItem(Material material, int amount, Pos position, Duration pickupDelay) {
		ItemStack itemToSpawn = ItemStack.of(material, amount)
				.with(DataComponents.CUSTOM_DATA, new CustomData(
						CompoundBinaryTag.builder().putBoolean("generator", true).build()));

		ItemEntity itemEntity = new ItemEntity(itemToSpawn);
		itemEntity.setPickupDelay(pickupDelay);
		itemEntity.setInstance(game.getInstanceContainer(), position);

		if (pickupDelay.equals(Duration.ofSeconds(1))) {
			itemEntity.setVelocity(new Vec(0, 0.1, 0));
		}
	}

	private Material getMaterialFromType(String materialType) {
		@Subst("iron") String type = materialType.toLowerCase();
		return switch (type) {
			case "iron" -> Material.IRON_INGOT;
			case "gold" -> Material.GOLD_INGOT;
			default -> Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, type));
		};
	}

	private double calculateForgeMultiplier(Material material, int forgeLevel) {
		if (material != Material.IRON_INGOT && material != Material.GOLD_INGOT) return 1.0;

		return switch (forgeLevel) {
			case 1 -> 1.5;
			case 2 -> 2.0;
			case 3, 4 -> 3.0;
			default -> 1.0;
		};
	}

	public void stopAllGenerators() {
		teamGeneratorTasks.values().stream().flatMap(List::stream).forEach(Task::cancel);
		teamGeneratorTasks.clear();
		generatorDisplays.clear();
		generatorLimits.clear();

		if (globalTicker != null) {
			globalTicker.cancel();
			globalTicker = null;
		}
	}

	public void addTeamGeneratorTask(TeamKey teamKey, Task task) {
			teamGeneratorTasks.computeIfAbsent(teamKey, k -> new ArrayList<>()).add(task);
		}

	@Override
	public void onEventChange(GameEventPosition previous, GameEventPosition current) {
		resetDisplaysIfEventChanged(
				(int) game.getEventManager().getDiamondDelaySeconds(),
				(int) game.getEventManager().getEmeraldDelaySeconds());
	}

	private static class GeneratorDisplay {
		private final TextDisplayEntity tierDisplay;
		private final TextDisplayEntity spawnDisplay;
		private int maxCountdown;
		private int countdown;

		public GeneratorDisplay(TextDisplayEntity tierDisplay, TextDisplayEntity spawnDisplay, int delay) {
			this.tierDisplay = tierDisplay;
			this.spawnDisplay = spawnDisplay;
			this.maxCountdown = delay;
			this.countdown = delay;
		}
	}

	private record GeneratorLimits(Material material, int amount, int maxAmount,
	                               List<BedWarsMapsConfig.Position> locations) {
	}
}

package net.swofty.type.bedwarsgame.game;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgame.entity.TextDisplayEntity;
import net.swofty.type.bedwarsgame.map.MapsConfig;
import org.intellij.lang.annotations.Subst;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GeneratorManager {
	private final Game game;
	private final Map<String, List<Task>> teamGeneratorTasks = new HashMap<>();
	private final Map<String, List<GeneratorDisplay>> generatorDisplays = new HashMap<>();

	public GeneratorManager(Game game) {
		this.game = game;
	}

	public void startTeamGenerators(List<MapsConfig.MapEntry.MapConfiguration.MapTeam> activeTeams) {
		MapsConfig.MapEntry.MapConfiguration mapConfig = game.getMapEntry().getConfiguration();

		if (mapConfig.getGenerator() == null || mapConfig.getGenerator().isEmpty()) {
			return;
		}

		for (MapsConfig.MapEntry.MapConfiguration.MapTeam team : activeTeams) {
			MapsConfig.Position genLocation = team.getGenerator();
			if (genLocation == null) continue;

			Pos spawnPosition = new Pos(genLocation.x(), genLocation.y(), genLocation.z());

			for (Map.Entry<String, MapsConfig.MapEntry.MapConfiguration.TeamGeneratorConfig> genEntry :
					mapConfig.getGenerator().entrySet()) {

				startTeamGenerator(team, genEntry.getKey(), genEntry.getValue(), spawnPosition);
			}
		}
	}

	private void startTeamGenerator(MapsConfig.MapEntry.MapConfiguration.MapTeam team,
									String materialType,
									MapsConfig.MapEntry.MapConfiguration.TeamGeneratorConfig config,
									Pos spawnPosition) {
		Material itemMaterial = getMaterialFromType(materialType);
		if (itemMaterial == null) {
			Logger.warn("Invalid material type: {} for team {}", materialType, team.getName());
			return;
		}

		final int baseAmount = config.getAmount();
		final int baseDelay = config.getDelay();

		Task task = MinecraftServer.getSchedulerManager().buildTask(() -> {
					if (game.getGameStatus() != GameStatus.IN_PROGRESS) return;

					int forgeLevel = game.getTeamManager().getTeamUpgradeLevel(team.getName(), "forge");
					double multiplier = calculateForgeMultiplier(itemMaterial, forgeLevel);

					int finalAmount = (int) Math.round(baseAmount * multiplier);
					if (finalAmount == 0 && baseAmount > 0 && multiplier > 1.0) {
						finalAmount = 1;
					}

					if (finalAmount > 0) {
						spawnItem(itemMaterial, finalAmount, spawnPosition, Duration.ofMillis(500));
					}
				})
				.delay(TaskSchedule.seconds(baseDelay))
				.repeat(TaskSchedule.seconds(baseDelay))
				.schedule();

		addTeamGeneratorTask(team.getName(), task);
	}

	public void startGlobalGenerators() {
		MapsConfig.MapEntry.MapConfiguration mapConfig = game.getMapEntry().getConfiguration();

		if (mapConfig.getGlobal_generator() == null) {
			return;
		}

		for (Map.Entry<String, MapsConfig.MapEntry.MapConfiguration.GlobalGenerator> entry :
				mapConfig.getGlobal_generator().entrySet()) {

			startGlobalGenerator(entry.getKey(), entry.getValue());
		}
	}

	private void startGlobalGenerator(String generatorType,
									  MapsConfig.MapEntry.MapConfiguration.GlobalGenerator config) {
		Material itemMaterial = Material.fromKey(Key.key(Key.MINECRAFT_NAMESPACE, generatorType));
		if (itemMaterial == null) {
			Logger.warn("Invalid material for global generator: {}", generatorType);
			return;
		}

		List<MapsConfig.Position> locations = config.getLocations();
		if (locations == null || locations.isEmpty()) {
			return;
		}

		int delaySeconds = config.getDelay();
		int amount = config.getAmount();
		int maxAmount = config.getMax();

		if (generatorType.equals("diamond") || generatorType.equals("emerald")) {
			setupGlobalGeneratorDisplays(generatorType, locations, delaySeconds);

			for (MapsConfig.Position location : locations) {
				startGlobalGeneratorAtLocation(location, itemMaterial, amount, maxAmount, delaySeconds);
			}
		}
	}

	private void setupGlobalGeneratorDisplays(String generatorType,
											  List<MapsConfig.Position> locations,
											  int delaySeconds) {
		NamedTextColor color = generatorType.equals("diamond") ? NamedTextColor.AQUA : NamedTextColor.GREEN;
		String capitalizedType = Character.toUpperCase(generatorType.charAt(0)) + generatorType.substring(1);

		for (MapsConfig.Position location : locations) {
			Component generatorTitle = Component.text(capitalizedType)
					.color(color)
					.decorate(TextDecoration.BOLD);

			Component tierText = Component.text("Tier I")
					.color(NamedTextColor.YELLOW);

			Component spawnText = MiniMessage.miniMessage()
					.deserialize("<yellow>Spawns in <red>" + delaySeconds + "</red> seconds!</yellow>");

			// Create display entities
			TextDisplayEntity tierDisplay = new TextDisplayEntity(tierText);
			tierDisplay.setInstance(game.getInstanceContainer(),
					new Pos(location.x(), location.y() + 4.0, location.z()));

			TextDisplayEntity generatorDisplay = new TextDisplayEntity(generatorTitle);
			generatorDisplay.setInstance(game.getInstanceContainer(),
					new Pos(location.x(), location.y() + 3.7, location.z()));

			TextDisplayEntity spawnDisplay = new TextDisplayEntity(spawnText);
			spawnDisplay.setInstance(game.getInstanceContainer(),
					new Pos(location.x(), location.y() + 3.4, location.z()));

			GeneratorDisplay display = new GeneratorDisplay(spawnDisplay, delaySeconds);
			generatorDisplays.computeIfAbsent(generatorType, k -> new ArrayList<>()).add(display);
		}
	}

	private void startGlobalGeneratorAtLocation(MapsConfig.Position location,
												Material material,
												int amount,
												int maxAmount,
												int delay) {
		Pos spawnPos = new Pos(location.x(), location.y(), location.z());

		MinecraftServer.getSchedulerManager().buildTask(() -> {
					if (game.getGameStatus() != GameStatus.IN_PROGRESS) return;

					// Update display countdown
					updateGeneratorDisplays();

					// Check item count and spawn if needed
					long currentItemCount = game.getInstanceContainer()
							.getNearbyEntities(spawnPos, 1.5)
							.stream()
							.filter(ItemEntity.class::isInstance)
							.map(ItemEntity.class::cast)
							.filter(entity -> entity.getItemStack().material() == material)
							.mapToLong(entity -> entity.getItemStack().amount())
							.sum();

					if (currentItemCount < maxAmount) {
						spawnItem(material, amount, spawnPos, Duration.ofSeconds(1));
					}
				})
				.delay(TaskSchedule.seconds(1))
				.repeat(TaskSchedule.seconds(1))
				.schedule();
	}

	private void updateGeneratorDisplays() {
		for (List<GeneratorDisplay> displays : generatorDisplays.values()) {
			for (GeneratorDisplay display : displays) {
				display.countdown--;
				if (display.countdown <= 0) {
					display.countdown = display.maxCountdown;
				}

				Component updatedText = MiniMessage.miniMessage()
						.deserialize("<yellow>Spawns in <red>" + display.countdown + "</red> seconds!</yellow>");
				display.spawnDisplay.setText(updatedText);
			}
		}
	}

	private void spawnItem(Material material, int amount, Pos position, Duration pickupDelay) {
		ItemStack itemToSpawn = ItemStack.of(material, amount);
		ItemEntity itemEntity = new ItemEntity(itemToSpawn);
		itemEntity.setPickupDelay(pickupDelay);
		itemEntity.setInstance(game.getInstanceContainer(), position);

		// Add slight upward velocity for visual effect
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
		if (material != Material.IRON_INGOT && material != Material.GOLD_INGOT) {
			return 1.0;
		}

		return switch (forgeLevel) {
			case 1 -> 1.5;  // +50%
			case 2 -> 2.0;  // +100%
			case 3, 4 -> 3.0;  // +200%
			default -> 1.0;
		};
	}

	private void addTeamGeneratorTask(String teamName, Task task) {
		teamGeneratorTasks.computeIfAbsent(teamName, k -> new ArrayList<>()).add(task);
	}

	public void stopAllGenerators() {
		teamGeneratorTasks.values()
				.stream()
				.flatMap(List::stream)
				.forEach(Task::cancel);
		teamGeneratorTasks.clear();
		generatorDisplays.clear();
	}

	private static class GeneratorDisplay {
		private final TextDisplayEntity spawnDisplay;
		private final int maxCountdown;
		private int countdown;

		public GeneratorDisplay(TextDisplayEntity spawnDisplay, int delay) {
			this.spawnDisplay = spawnDisplay;
			this.maxCountdown = delay;
			this.countdown = delay;
		}
	}
}

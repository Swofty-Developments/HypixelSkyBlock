package net.swofty.type.dwarvenmines.commission;

import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointCommissions;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Commissions {
	public static List<Commission> CONSISTENT = List.of(

			new Commission("Mithril Miner", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.MITHRIL, 350, LocationSelector.any(), EventType.NONE),
					false
			),

			new Commission("Lava Springs Mithril", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.MITHRIL, 250, LocationSelector.of(RegionType.LAVA_SPRINGS), EventType.NONE),
					false
			),

			new Commission("Royal Mines Mithril", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.MITHRIL, 250, LocationSelector.of(RegionType.ROYAL_MINES), EventType.NONE),
					false
			),

			new Commission("Cliffside Veins Mithril", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.MITHRIL, 250, LocationSelector.of(RegionType.CLIFFSIDE_VEINS), EventType.NONE),
					false
			),

			new Commission("Rampart's Quarry Mithril", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.MITHRIL, 250, LocationSelector.of(RegionType.RAMPARTS_QUARRY), EventType.NONE),
					false
			),

			new Commission("Upper Mines Mithril", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.MITHRIL, 250, LocationSelector.of(RegionType.UPPER_MINES), EventType.NONE),
					false
			),

			new Commission("Titanium Miner", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.TITANIUM, 15, LocationSelector.any(), EventType.NONE),
					false
			),

			new Commission("Lava Springs Titanium", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.TITANIUM, 10, LocationSelector.of(RegionType.LAVA_SPRINGS), EventType.NONE),
					false
			),

			new Commission("Royal Mines Titanium", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.TITANIUM, 10, LocationSelector.of(RegionType.ROYAL_MINES), EventType.NONE),
					false
			),

			new Commission("Cliffside Veins Titanium", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.TITANIUM, 10, LocationSelector.of(RegionType.CLIFFSIDE_VEINS), EventType.NONE),
					false
			),

			new Commission("Rampart's Quarry Titanium", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.TITANIUM, 10, LocationSelector.of(RegionType.RAMPARTS_QUARRY), EventType.NONE),
					false
			),

			new Commission("Upper Mines Titanium", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.TITANIUM, 10, LocationSelector.of(RegionType.UPPER_MINES), EventType.NONE),
					false
			),

			new Commission("Goblin Slayer", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.SLAY, Objective.BlockTarget.GOBLIN, 100, LocationSelector.any(), EventType.NONE),
					false
			),

			new Commission("Glacite Walker Slayer", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.SLAY, Objective.BlockTarget.GLACITE_WALKER, 50, LocationSelector.any(), EventType.NONE),
					false
			),

			new Commission("Treasure Hoarder Puncher", CommissionCategory.CONSISTENT,
					new Objective(ObjectiveType.DAMAGE, Objective.BlockTarget.TREASURE_HOARDER, 10, LocationSelector.any(), EventType.NONE),
					false
			)
	);

	public static List<Commission> SITUATIONAL = List.of(
			new Commission("Goblin Raid Slayer", CommissionCategory.SITUATIONAL,
					new Objective(ObjectiveType.SLAY, Objective.BlockTarget.GOBLIN, 20, LocationSelector.any(), EventType.GOBLIN_RAID),
					false
			),

			new Commission("Goblin Raid", CommissionCategory.SITUATIONAL,
					new Objective(ObjectiveType.PARTICIPATE, Objective.BlockTarget.NONE, 1, LocationSelector.any(), EventType.GOBLIN_RAID),
					false
			),

			new Commission("Raffle", CommissionCategory.SITUATIONAL,
					new Objective(ObjectiveType.PARTICIPATE, Objective.BlockTarget.NONE, 1, LocationSelector.any(), EventType.RAFFLE),
					false
			),

			new Commission("Golden Goblin Slayer", CommissionCategory.SITUATIONAL,
					new Objective(ObjectiveType.SLAY, Objective.BlockTarget.GOLDEN_GOBLIN, 1, LocationSelector.any(), EventType.NONE),
					false
			),

			new Commission("Star Sentry Puncher", CommissionCategory.SITUATIONAL,
					new Objective(ObjectiveType.DAMAGE, Objective.BlockTarget.STAR_SENTRY, 10, LocationSelector.any(), EventType.NONE),
					false
			),

			new Commission("Lucky Raffle", CommissionCategory.SITUATIONAL,
					new Objective(ObjectiveType.DEPOSIT, Objective.BlockTarget.NONE, 20, LocationSelector.any(), EventType.RAFFLE),
					false
			),

			new Commission("2x Mithril Powder Collector", CommissionCategory.SITUATIONAL,
					new Objective(ObjectiveType.COLLECT, Objective.BlockTarget.MITHRIL, 500, LocationSelector.any(), EventType.DOUBLE_POWDER),
					false
			)
	);

	public static List<Commission> TUTORIAL = List.of(

			new Commission("Mithril Miner", CommissionCategory.TUTORIAL,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.MITHRIL, 50, LocationSelector.any(), EventType.NONE),
					true
			),

			new Commission("First Event", CommissionCategory.TUTORIAL,
					new Objective(ObjectiveType.PARTICIPATE,  Objective.BlockTarget.NONE, 1, LocationSelector.any(), EventType.NONE),
					true
			),

			new Commission("Titanium Miner", CommissionCategory.TUTORIAL,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.TITANIUM, 2, LocationSelector.any(), EventType.NONE),
					true
			),

			new Commission("Mithril Miner II", CommissionCategory.TUTORIAL,
					new Objective(ObjectiveType.MINE, Objective.BlockTarget.MITHRIL, 150, LocationSelector.any(), EventType.NONE),
					true
			)
	);

	public static int getCommissionSlots(SkyBlockPlayer player) {
		DatapointCommissions.PlayerCommissionData data = getCommissionData(player);
		return data.getCommissionSlots();
	}

	public static DatapointCommissions.PlayerCommissionData getCommissionData(SkyBlockPlayer player) {
		SkyBlockDataHandler dataHandler = player.getSkyblockDataHandler();
		return dataHandler.get(SkyBlockDataHandler.Data.COMMISSIONS, DatapointCommissions.class).getValue();
	}

	public static void saveCommissionData(SkyBlockPlayer player, DatapointCommissions.PlayerCommissionData data) {
		SkyBlockDataHandler dataHandler = player.getSkyblockDataHandler();
		dataHandler.get(SkyBlockDataHandler.Data.COMMISSIONS, DatapointCommissions.class).setValue(data);
	}

	public static void generateCommissions(SkyBlockPlayer player) {
		DatapointCommissions.PlayerCommissionData data = getCommissionData(player);

		data.getActiveCommissions().clear();
		int slots = data.getCommissionSlots();
		List<String> usedNames = new ArrayList<>();

		for (int i = 0; i < slots; i++) {
			Commission commission;

			if (i % 2 == 0) {
				commission = selectRandomCommission(CONSISTENT, usedNames);
			} else {
				commission = selectRandomCommission(SITUATIONAL, usedNames);
				if (commission == null) {
					commission = selectRandomCommission(CONSISTENT, usedNames);
				}
			}

			if (commission != null) {
				usedNames.add(commission.name);
				data.getActiveCommissions().add(new DatapointCommissions.ActiveCommission(commission.name));
			}
		}

		saveCommissionData(player, data);
	}

	private static Commission selectRandomCommission(List<Commission> pool, List<String> usedNames) {
		List<Commission> available = new ArrayList<>();
		for (Commission commission : pool) {
			if (!usedNames.contains(commission.name)) {
				available.add(commission);
			}
		}

		if (available.isEmpty()) {
			return null;
		}

		return available.get(ThreadLocalRandom.current().nextInt(available.size()));
	}

	public static Commission getCommissionByName(String name) {
		for (Commission c : CONSISTENT) {
			if (c.name.equals(name)) return c;
		}
		for (Commission c : SITUATIONAL) {
			if (c.name.equals(name)) return c;
		}
		for (Commission c : TUTORIAL) {
			if (c.name.equals(name)) return c;
		}
		return null;
	}

	public static boolean needsGeneration(SkyBlockPlayer player) {
		DatapointCommissions.PlayerCommissionData data = getCommissionData(player);
		if (data.getActiveCommissions().isEmpty()) {
			return true;
		}

		boolean allCompleted = true;
		for (DatapointCommissions.ActiveCommission ac : data.getActiveCommissions()) {
			if (!ac.isCompleted()) {
				allCompleted = false;
				break;
			}
		}

		return allCompleted;
	}

	public static int[] getGuiSlots(int slotCount) {
		return switch (slotCount) {
			case 2 -> new int[]{12, 14};
			case 3 -> new int[]{11, 13, 15};
			case 4 -> new int[]{11, 12, 14, 15};
			default -> new int[]{};
		};
	}

	@Deprecated
	public static int getCommissionLevel(SkyBlockPlayer player) {
		return getCommissionSlots(player);
	}

	public static void replaceCommissionAt(SkyBlockPlayer player, int slotIndex) {
		DatapointCommissions.PlayerCommissionData data = getCommissionData(player);
		if (slotIndex < 0 || slotIndex >= data.getActiveCommissions().size()) return;

		List<String> usedNames = new ArrayList<>();
		for (DatapointCommissions.ActiveCommission ac : data.getActiveCommissions()) {
			if (ac != null && ac.getCommissionName() != null) usedNames.add(ac.getCommissionName());
		}

		Commission newComm;
		if (slotIndex % 2 == 0) {
			newComm = selectRandomCommission(CONSISTENT, usedNames);
		} else {
			newComm = selectRandomCommission(SITUATIONAL, usedNames);
			if (newComm == null) newComm = selectRandomCommission(CONSISTENT, usedNames);
		}

		if (newComm == null) {
			data.getActiveCommissions().remove(slotIndex);
		} else {
			data.getActiveCommissions().set(slotIndex, new DatapointCommissions.ActiveCommission(newComm.name));
		}

		saveCommissionData(player, data);
	}

}

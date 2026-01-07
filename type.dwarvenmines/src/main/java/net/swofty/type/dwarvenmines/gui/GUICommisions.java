package net.swofty.type.dwarvenmines.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.dwarvenmines.commission.*;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointCommissions;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointHOTM;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUICommisions extends HypixelInventoryGUI {

	public GUICommisions() {
		super("Commissions", InventoryType.CHEST_4_ROW);
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		fill(FILLER_ITEM);

		SkyBlockPlayer player = (SkyBlockPlayer) getPlayer();
		DatapointCommissions.PlayerCommissionData commissionData = Commissions.getCommissionData(player);
		commissionData.checkAndResetDaily();

		if (Commissions.needsGeneration(player)) {
			Commissions.generateCommissions(player);
			commissionData = Commissions.getCommissionData(player); // refresh
		}

		int slotCount = commissionData.getCommissionSlots();
		int[] slots = Commissions.getGuiSlots(slotCount);

		List<DatapointCommissions.ActiveCommission> activeCommissions = commissionData.getActiveCommissions();

		DatapointHOTM.PlayerHOTMData hotmData = player.getSkyblockDataHandler()
				.get(SkyBlockDataHandler.Data.HOTM, DatapointHOTM.class).getValue();
		int hotmTier = hotmData.getTier();
		boolean isHotmMaxed = hotmData.isMaxed();
		int remainingDailyBonus = commissionData.getRemainingDailyBonus();

		for (int i = 0; i < slots.length && i < activeCommissions.size(); i++) {
			int index = i;
			DatapointCommissions.ActiveCommission activeCommission = activeCommissions.get(i);
			Commission commission = Commissions.getCommissionByName(activeCommission.getCommissionName());
			boolean willGetDailyBonus = index < remainingDailyBonus;

			set(new GUIClickableItem(slots[i]) {
				@Override
				public void run(InventoryPreClickEvent e, HypixelPlayer p) {
					SkyBlockPlayer player = (SkyBlockPlayer) p;
					DatapointCommissions.PlayerCommissionData commissionData = Commissions.getCommissionData(player);
					if (activeCommission.isCompleted() && !activeCommission.isClaimed()) {
						SkyBlockDataHandler dataHandler = player.getSkyblockDataHandler();

						DatapointHOTM.PlayerHOTMData hotmData =
								dataHandler.get(SkyBlockDataHandler.Data.HOTM, DatapointHOTM.class).getValue();

						int hotmTier = hotmData.getTier();
						boolean isHotmMaxed = hotmData.isMaxed();
						boolean isDailyBonus = commissionData.hasDailyBonus();

						CommissionReward reward = CommissionReward.calculate(hotmTier, isHotmMaxed, isDailyBonus);

						if (reward.hotmXp() > 0) {
							hotmData.addExperience(reward.hotmXp());
							dataHandler.get(SkyBlockDataHandler.Data.HOTM, DatapointHOTM.class).setValue(hotmData);
						}

						if (reward.mithrilPowder() > 0) {
							hotmData.addMithrilPowder(reward.mithrilPowder());
							dataHandler.get(SkyBlockDataHandler.Data.HOTM, DatapointHOTM.class).setValue(hotmData);
						}

						if (reward.miningXp() > 0) {
							player.getSkills().increase(player, SkillCategories.MINING, (double) reward.miningXp());
						}

						activeCommission.setClaimed(true);
						commissionData.completeCommission();
						Commissions.saveCommissionData(player, commissionData);
						Commissions.replaceCommissionAt(player, index);
						new GUICommisions().open(player);
					}
				}

				@Override
				public ItemStack.Builder getItem(HypixelPlayer player) {
					List<String> lore = new ArrayList<>();
					lore.add("§7Commissions are tasks given directly");
					lore.add("§7by the king which give bountiful");
					lore.add("§7rewards.");
					lore.add(" ");

					if (commission != null) {
						lore.add("§9" + commission.name);
						lore.add("§7" + commission.generateDescription());
					} else {
						lore.add("§9Unknown Commission");
						lore.add("§7Complete this task for rewards.");
					}

					lore.add("");
					lore.add("§9Rewards");

					String[] rewardLines = CommissionReward.getRewardLore(hotmTier, isHotmMaxed, willGetDailyBonus);
					for (String line : rewardLines) {
						lore.add(line);
					}

					lore.add(" ");

					if (activeCommission.isCompleted()) {
						lore.add("§a§lCOMPLETED!");
						lore.add("");
						lore.add("§eClick to claim rewards!");
					} else {
						lore.add("§9Progress");
						int progress = activeCommission.getProgress();
						int target = commission != null ? commission.objective.amount : 100;
						int percentage = Math.min(100, (int) ((progress / (double) target) * 100));
						String progressBar = buildProgressBar(percentage);
						lore.add(progressBar + " §9" + percentage + "%");
					}

					return ItemStackCreator.getStack("§6Commission #" + (index + 1), Material.WRITABLE_BOOK, 1, lore);
				}
			});
		}

		set(new GUIClickableItem(30) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				new GUICommissionMilestones().open(player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				DatapointCommissions.PlayerCommissionData data = Commissions.getCommissionData((SkyBlockPlayer) player);
				int totalCompleted = data.getTotalCompleted();

				List<String> lore = new ArrayList<>();
				lore.add("§7View milestone progress and rewards!");
				lore.add(" ");

				CommissionMilestone nextMilestone = CommissionMilestone.getNextMilestone(totalCompleted);
				if (nextMilestone != null) {
					double progress = data.getMilestoneProgress(nextMilestone.getTier());
					String progressBar = buildMilestoneProgressBar(progress);

					lore.add("§7Progress to milestone " + StringUtility.getAsRomanNumeral(nextMilestone.getTier()) + ": §e" + String.format("%.1f", progress) + "§6%");
					lore.add(progressBar + " §e" + totalCompleted + "§6/§e" + nextMilestone.getCommissionsRequired());
					lore.add(" ");
					lore.add("§7Tier " + StringUtility.getAsRomanNumeral(nextMilestone.getTier()) + " Rewards:");
					for (String reward : nextMilestone.getRewardDescriptions()) {
						lore.add("  " + reward.replace("§7- ", ""));
					}
				} else {
					lore.add("§a§lALL MILESTONES COMPLETED!");
				}

				lore.add(" ");
				lore.add("§eClick to view!");

				return ItemStackCreator.getStack("§eCommission Milestones", Material.FILLED_MAP, 1, lore);
			}
		});

		set(GUIClickableItem.getCloseItem(31));
		updateItemStacks(getInventory(), getPlayer());
	}

	private String buildProgressBar(int percentage) {
		int filled = percentage / 5; // 20 segments total
		int empty = 20 - filled;

		StringBuilder bar = new StringBuilder("§f§l§m");
		for (int i = 0; i < filled; i++) {
			bar.append(" ");
		}
		bar.append("§7§l§m");
		for (int i = 0; i < empty; i++) {
			bar.append(" ");
		}
		return bar.toString();
	}

	private String buildMilestoneProgressBar(double percentage) {
		int filled = (int) (percentage / 5); // 20 segments total
		int empty = 20 - filled;
		return "§2§l§m" + " ".repeat(Math.max(0, filled)) + "§f§l§m" + " ".repeat(Math.max(0, empty));
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {

	}
}

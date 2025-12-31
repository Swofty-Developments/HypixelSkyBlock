package net.swofty.type.dwarvenmines.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.dwarvenmines.commission.CommissionMilestone;
import net.swofty.type.dwarvenmines.commission.Commissions;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointCommissions;
import net.swofty.type.skyblockgeneric.skill.SkillCategories;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUICommissionMilestones extends HypixelInventoryGUI {

	private static final int[] MILESTONE_SLOTS = {19, 20, 21, 23, 24, 25};

	public GUICommissionMilestones() {
		super("§eCommission Milestones", InventoryType.CHEST_6_ROW);
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent e) {
		fill(FILLER_ITEM);

		SkyBlockPlayer player = (SkyBlockPlayer) getPlayer();
		DatapointCommissions.PlayerCommissionData commissionData = Commissions.getCommissionData(player);
		int totalCompleted = commissionData.getTotalCompleted();
		int currentTier = commissionData.getMilestoneTier();

		set(new GUIItem(4) {
			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				List<String> lore = new ArrayList<>();
				lore.add("§7View milestone progress and rewards!");
				lore.add("");

				CommissionMilestone nextMilestone = null;
				for (CommissionMilestone m : CommissionMilestone.values()) {
					if (totalCompleted < m.getCommissionsRequired()) {
						nextMilestone = m;
						break;
					}
				}

				if (nextMilestone != null) {
					double progress = commissionData.getMilestoneProgress(nextMilestone.getTier());
					String progressBar = buildProgressBar(progress);
					lore.add("§7Progress to milestone " + StringUtility.getAsRomanNumeral(nextMilestone.getTier()) + ": §e" + String.format("%.1f", progress) + "§6%");
					lore.add(progressBar + " §e" + totalCompleted + "§6/§e" + nextMilestone.getCommissionsRequired());
					lore.add("");
					lore.add("§7Tier " + StringUtility.getAsRomanNumeral(nextMilestone.getTier()) + " Rewards:");
					for (String reward : nextMilestone.getRewardDescriptions()) {
						lore.add("  " + reward.replace("§7- ", ""));
					}
				} else {
					lore.add("§a§lALL MILESTONES COMPLETED!");
					lore.add("");
					lore.add("§7Total Commissions: §e" + totalCompleted);
				}

				return ItemStackCreator.getStack(
						"§eCommission Milestones",
						Material.FILLED_MAP,
						1,
						lore
				);
			}
		});

		for (int i = 0; i < CommissionMilestone.values().length; i++) {
			CommissionMilestone milestone = CommissionMilestone.values()[i];
			int slot = MILESTONE_SLOTS[i];

			set(new GUIClickableItem(slot) {
				@Override
				public void run(InventoryPreClickEvent e, HypixelPlayer player) {
					SkyBlockPlayer sbPlayer = (SkyBlockPlayer) player;
					DatapointCommissions.PlayerCommissionData data = Commissions.getCommissionData(sbPlayer);

					if (data.getTotalCompleted() >= milestone.getCommissionsRequired()
							&& !data.isMilestoneClaimed(milestone.getTier())) {
						claimMilestone(sbPlayer, milestone, data);
						new GUICommissionMilestones().open(sbPlayer);
					}
				}

				@Override
				public ItemStack.Builder getItem(HypixelPlayer player) {
					SkyBlockPlayer sbPlayer = (SkyBlockPlayer) player;
					DatapointCommissions.PlayerCommissionData data = Commissions.getCommissionData(sbPlayer);

					boolean reached = data.getTotalCompleted() >= milestone.getCommissionsRequired();
					boolean claimed = data.isMilestoneClaimed(milestone.getTier());
					double progress = data.getMilestoneProgress(milestone.getTier());

					List<String> lore = new ArrayList<>();
					lore.add("");
					lore.add("§7Progress: " + (reached ? "§a100%" : "§e" + String.format("%.1f", progress) + "§6%"));

					String progressBar = buildProgressBar(progress);
					lore.add(progressBar + " §e" + data.getTotalCompleted() + "§6/§e" + milestone.getCommissionsRequired());
					lore.add("");
					lore.add("§7Rewards:");

					String[] rewards = getMilestoneRewardLines(milestone);
					for (String reward : rewards) {
						if (claimed) {
							lore.add("§a ✔ " + reward);
						} else {
							lore.add("  " + reward);
						}
					}

					lore.add("");
					if (claimed) {
						lore.add("§a§lCLAIMED");
					} else if (reached) {
						lore.add("§e§lCLICK TO CLAIM!");
					}

					Material material;
					String titleColor;
					if (claimed) {
						material = Material.LIME_STAINED_GLASS_PANE;
						titleColor = "§a";
					} else if (reached) {
						material = Material.YELLOW_STAINED_GLASS_PANE;
						titleColor = "§e";
					} else {
						material = Material.RED_STAINED_GLASS_PANE;
						titleColor = "§c";
					}

					return ItemStackCreator.getStack(
							titleColor + "Milestone " + StringUtility.getAsRomanNumeral(milestone.getTier()) + " Rewards",
							material,
							milestone.getTier(),
							lore
					);
				}
			});
		}

		set(new GUIClickableItem(48) {
			@Override
			public void run(InventoryPreClickEvent e, HypixelPlayer player) {
				new GUICommisions().open((SkyBlockPlayer) player);
			}

			@Override
			public ItemStack.Builder getItem(HypixelPlayer player) {
				return ItemStackCreator.getStack(
						"§aGo Back",
						Material.ARROW,
						1,
						"§7To Commissions"
				);
			}
		});

		set(GUIClickableItem.getCloseItem(49));
		updateItemStacks(getInventory(), getPlayer());
	}

	private void claimMilestone(SkyBlockPlayer player, CommissionMilestone milestone,
								DatapointCommissions.PlayerCommissionData data) {
		data.claimMilestone(milestone.getTier());
		Commissions.saveCommissionData(player, data);

		player.getSkills().increase(player, SkillCategories.MINING, (double) milestone.getMiningXpReward());
		player.getExperienceHandler().addExperience(milestone.getSkyBlockXpReward());


		player.sendMessage("§6§l MILESTONE CLAIMED! §r§eTier " + StringUtility.getAsRomanNumeral(milestone.getTier()));
		player.sendMessage("§7Rewards:");
		player.sendMessage("§7 - §3+" + String.format("%,d", milestone.getMiningXpReward()) + " Mining XP");
		player.sendMessage("§7 - §b+" + milestone.getSkyBlockXpReward() + " SkyBlock XP");

		if (milestone.isUnlocksEmissaries()) {
			player.addAndUpdateItem(ItemType.ROYAL_COMPASS);
		}
		if (milestone.isUnlocksExtraSlot()) {
		}
		if (milestone.isUnlocksDwarvenMinesScroll()) {
			// TODO: Give Travel Scroll item
		}
		if (milestone.isUnlocksRoyalPigeon()) {
			player.addAndUpdateItem(ItemType.ROYAL_PIGEON);
		}
		if (milestone.isUnlocksCrystalNucleusScroll()) {
			// TODO: Give Travel Scroll item
		}
	}

	private String[] getMilestoneRewardLines(CommissionMilestone milestone) {
		List<String> rewards = new ArrayList<>();

		if (milestone.isUnlocksEmissaries()) {
			rewards.add("§6Emissaries");
			rewards.add("§9Royal Compass");
		}
		if (milestone.isUnlocksExtraSlot()) {
			rewards.add("§a+1 Commission Slot");
		}
		if (milestone.isUnlocksDwarvenMinesScroll()) {
			rewards.add("§9Travel Scroll to Dwarven Mines");
		}
		if (milestone.isUnlocksRoyalPigeon()) {
			rewards.add("§6Royal Pigeon");
		}
		if (milestone.isUnlocksCrystalNucleusScroll()) {
			rewards.add("§9Travel Scroll to the Crystal Nucleus");
		}

		rewards.add("§8+§3" + String.format("%,d", milestone.getMiningXpReward()) + " §7Mining Experience");
		rewards.add("§8+§b" + milestone.getSkyBlockXpReward() + " SkyBlock XP");

		return rewards.toArray(new String[0]);
	}

	private String buildProgressBar(double percentage) {
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
		e.setCancelled(true);
	}
}

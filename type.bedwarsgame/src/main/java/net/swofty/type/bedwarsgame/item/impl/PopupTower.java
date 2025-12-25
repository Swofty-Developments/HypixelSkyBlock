package net.swofty.type.bedwarsgame.item.impl;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.commons.bedwars.map.BedWarsMapsConfig;
import net.swofty.type.bedwarsgame.item.SimpleInteractableItem;
import net.swofty.type.bedwarsgame.user.BedWarsPlayer;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

import java.util.concurrent.atomic.AtomicInteger;

public class PopupTower extends SimpleInteractableItem {

	public PopupTower() {
		super("popup_tower");
	}

	private static Block mapTeamToBlock(BedWarsMapsConfig.TeamKey teamKey) {
		return switch (teamKey) {
			case RED -> Block.RED_WOOL;
			case BLUE -> Block.BLUE_WOOL;
			case GREEN -> Block.LIME_WOOL;
			case YELLOW -> Block.YELLOW_WOOL;
			case AQUA -> Block.LIGHT_BLUE_WOOL;
			case PINK -> Block.PINK_WOOL;
			case WHITE -> Block.WHITE_WOOL;
			case GRAY -> Block.GRAY_WOOL;
		};
	}

	@Override
	public ItemStack getBlandItem() {
		return ItemStackCreator.createNamedItemStack(Material.CHEST, "§aPop-Up Tower").build();
	}

	@Override
	public void onBlockPlace(PlayerBlockPlaceEvent event) {
		event.setBlock(Block.AIR);

		Instance instance = event.getInstance();
		Pos basePos = event.getBlockPosition().asPos();

		try {
			instance.loadChunk(basePos).join();
		} catch (Exception ignored) {
		}

		final int LAYERS = 4;
		final int RISE_TICK_INTERVAL = 2;

		int[][] bottomPattern = new int[][]{
				{0, 1, 1, 1, 0},
				{1, 0, 5, 0, 1},
				{1, 0, 0, 0, 1},
				{0, 1, 0, 1, 0}
		};

		int[][] upperPattern = new int[][]{
				{0, 1, 1, 1, 0},
				{1, 0, 5, 0, 1},
				{1, 0, 0, 0, 1},
				{0, 1, 1, 1, 0}
		};

		AtomicInteger layerCounter = new AtomicInteger(0);

		BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
		Pos playerPos = player.getPosition();
		double dx = playerPos.x() - basePos.x();
		double dz = playerPos.z() - basePos.z();
		String front;
		if (Math.abs(dx) > Math.abs(dz)) {
			front = dx > 0 ? "east" : "west";
		} else {
			front = dz > 0 ? "south" : "north";
		}

		MinecraftServer.getSchedulerManager().scheduleTask(() -> {
			int layerIndex = layerCounter.getAndIncrement();
			if (layerIndex >= LAYERS) return;

			int[][] pattern = (layerIndex <= 1) ? bottomPattern : upperPattern;
			int rows = pattern.length; // 4
			int cols = pattern[0].length; //5

			final int pivotCol = 2;
			final int pivotRow = 1;
			for (int rz = 0; rz < rows; rz++) {
				for (int cx = 0; cx < cols; cx++) {
					int val = pattern[rz][cx];
					if (val == 0) continue;

					int localX = cx - pivotCol; // left/right
					int localZ = rz - pivotRow; // forward/back

					int rotX = 0, rotZ = 0;
					String ladderFacing = "north";
					switch (front) {
						case "south" -> {
							rotX = localX;
							rotZ = localZ;
							ladderFacing = "south";
						}
						case "north" -> {
							rotX = -localX;
							rotZ = -localZ;
							ladderFacing = "north";
						}
						case "east" -> {
							rotX = localZ;
							rotZ = -localX;
							ladderFacing = "east";
						}
						case "west" -> {
							rotX = -localZ;
							rotZ = localX;
							ladderFacing = "west";
						}
					}

					double worldX = basePos.x() + rotX;
					double worldZ = basePos.z() + rotZ;

					Pos target = new Pos(worldX, basePos.y() + layerIndex - 1, worldZ);
					try {
						if (val == 1) {
							if (instance.getBlock(target).isAir()) {
								instance.setBlock(target, mapTeamToBlock(player.getTeamKey()));
							}
						} else if (val == 5) {
							try {
								instance.setBlock(target, Block.LADDER.withProperty("facing", ladderFacing));
							} catch (IllegalArgumentException ignored) {
								instance.setBlock(target, Block.LADDER);
							}
						}
					} catch (IllegalArgumentException e) {
						if (val == 1) {
							instance.setBlock(target, Block.WHITE_WOOL);
						}
					} catch (Exception ignored) {
					}
				}
			}
		}, TaskSchedule.tick(RISE_TICK_INTERVAL), TaskSchedule.tick(RISE_TICK_INTERVAL));
	}

}
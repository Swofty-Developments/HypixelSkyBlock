package net.swofty.type.bedwarsgame.item.impl;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.bedwarsgeneric.item.SimpleInteractableItem;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;

import java.util.concurrent.atomic.AtomicInteger;

public class PopupTower extends SimpleInteractableItem {

	public PopupTower() {
		super("popup_tower");
	}

	private static Block mapColorToWool(String color) {
		if (color == null) return Block.WHITE_WOOL;
		return switch (color.toLowerCase()) {
			case "white" -> Block.WHITE_WOOL;
			case "orange" -> Block.ORANGE_WOOL;
			case "magenta" -> Block.MAGENTA_WOOL;
			case "light_blue", "lightblue" -> Block.LIGHT_BLUE_WOOL;
			case "yellow" -> Block.YELLOW_WOOL;
			case "lime" -> Block.LIME_WOOL;
			case "pink" -> Block.PINK_WOOL;
			case "gray" -> Block.GRAY_WOOL;
			case "light_gray", "lightgray" -> Block.LIGHT_GRAY_WOOL;
			case "cyan" -> Block.CYAN_WOOL;
			case "purple" -> Block.PURPLE_WOOL;
			case "blue" -> Block.BLUE_WOOL;
			case "brown" -> Block.BROWN_WOOL;
			case "green" -> Block.GREEN_WOOL;
			case "red" -> Block.RED_WOOL;
			case "black" -> Block.BLACK_WOOL;
			default -> Block.WHITE_WOOL;
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

		String teamColor = event.getPlayer().getTag(net.minestom.server.tag.Tag.String("teamColor"));
		Block wool = mapColorToWool(teamColor);

		AtomicInteger layerCounter = new AtomicInteger(0);

		var player = event.getPlayer();
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
								instance.setBlock(target, wool);
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
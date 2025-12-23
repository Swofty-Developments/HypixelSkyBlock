package net.swofty.pvp.utils;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.List;

public class FluidUtil {
	public static int getLevel(Block block) {
		String levelStr = block.getProperty("level");
		if (levelStr == null) return 8;
		int level = Integer.parseInt(levelStr);
		if (level >= 8) return 8; // Falling water
		return 8 - level;
	}

	public static double getHeight(Block block) {
		int level = getLevel(block);
		return switch (level) {
			case 1 -> 0.25;
			case 2 -> 0.375;
			case 3 -> 0.5;
			case 4 -> 0.625;
			case 5 -> 0.75;
			default -> 1;
		};
	}

	public static boolean isTouchingWater(Player player, Block block, int blockY) {
		if (!block.compare(Block.WATER)) return false;
		if (player.getPosition().y() + player.getBoundingBox().height() < blockY) return false;
		if (player.getPosition().y() > (blockY + getHeight(block))) return false;
		return true;
	}

	record PairXZ(int x, int z) {}

	public static boolean isTouchingWater(Player player) {
		Pos position = player.getPosition();
		double x = position.x();
		int blockX = position.blockX();
		double z = position.z();
		int blockZ = position.blockZ();
		double y = position.y();
		int blockY = position.blockY();

		List<PairXZ> points = new ArrayList<>();
		points.add(new PairXZ(blockX, blockZ));

		if (x - blockX > 0.7) {
			if (z - blockZ > 0.7) {
				points.add(new PairXZ(blockX + 1, blockZ + 1));
			}
			points.add(new PairXZ(blockX + 1, blockZ));
		} else if (x - blockX < 0.2) {
			if (z - blockZ < 0.2) {
				points.add(new PairXZ(blockX - 1, blockZ - 1));
			}
			points.add(new PairXZ(blockX - 1, blockZ));
		}
		if (z - blockZ > 0.7) {
			if (x - blockX < 0.2) {
				points.add(new PairXZ(blockX - 1, blockZ + 1));
			}
			points.add(new PairXZ(blockX, blockZ + 1));
		} else if (z - blockZ < 0.2) {
			if (x - blockX > 0.7) {
				points.add(new PairXZ(blockX + 1, blockZ - 1));
			}
			points.add(new PairXZ(blockX, blockZ - 1));
		}

		Instance instance = player.getInstance();
		assert instance != null;

		for (PairXZ pair : points) {
			Block block = instance.getBlock(pair.x(), blockY, pair.z());
			if (isTouchingWater(player, block, blockY)) return true;
			block = instance.getBlock(pair.x(), blockY + 1, pair.z());
			if (isTouchingWater(player, block, blockY + 1)) return true;

			if (y - blockY >= 2 - player.getBoundingBox().height()) {
				block = instance.getBlock(pair.x(), blockY + 2, pair.z());
				if (isTouchingWater(player, block, blockY + 2)) return true;
			}
		}

		return false;
	}
}
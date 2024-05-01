package net.swofty.types.generic.utility;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public class ChestUtility {

    public static Point[] getDoubleChestPositions(Instance instance, Point point) {
        Point[] positions = new Point[2];
        Point[] offsets = {new Pos(0, 0, 1), new Pos(0, 0, -1), new Pos(1, 0, 0), new Pos(-1, 0, 0)};
        for (Point offset : offsets) {
            Point adjacentPos = point.add(offset);
            Block adjacentBlock = instance.getBlock(adjacentPos);
            if (adjacentBlock.name().equals("minecraft:chest")) {
                positions[0] = offset.equals(new Pos(1, 0, 0)) || offset.equals(new Pos(0, 0, -1)) ? adjacentPos : point;
                positions[1] = offset.equals(new Pos(0, 0, 1)) || offset.equals(new Pos(-1, 0, 0)) ? adjacentPos : point;
                break;
            }
        }

        return positions;
    }

}

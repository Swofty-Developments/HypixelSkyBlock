package net.swofty.utility;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;

public record PositionedBlock(Block block, Pos pos)
{
}

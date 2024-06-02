package net.swofty.types.generic.user;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

public record AntiCheatHandler(SkyBlockPlayer player) {
    public Double getDistanceFromClosestBlockBelow() {
        Instance instance = player.getInstance();

        if (instance == null) return null;

        double toReturn = 1.0;

        for (int i = 0; i < 256; i++) {
            Block block = instance.getBlock(player.getPosition().sub(0, i, 0));
            if (block != Block.AIR || block == Block.WATER || block == Block.LAVA) {
                toReturn = i;
                break;
            }
        }

        double x = player.getPosition().x();
        double z = player.getPosition().z();
        int blockX = player.getPosition().blockX();
        int blockZ = player.getPosition().blockZ();

        double xRemainder = x % 1;
        double zRemainder = z % 1;
        if (toReturn != 1.0 && (xRemainder < -0.7 || xRemainder > -0.3 || zRemainder < -0.3 || zRemainder > -0.7)) {

            // Get the possible side block coordinates
            int sideX = (xRemainder > -0.3) ? (int) (double) (blockX + 1) :
                    (xRemainder < -0.7) ? (int) (double) (blockX - 1) : blockX;
            int sideZ = (zRemainder > -0.3) ? (int) (double) (blockZ + 1) :
                    (zRemainder < -0.7) ? (int) (double) (blockZ - 1) : blockZ;

            // Check if the side block on which player is overlapping is not air
            Block sideBlock = instance.getBlock(new Pos(sideX, player.getPosition().y() - 1, sideZ));
            Block above1 = instance.getBlock(new Pos(sideX, player.getPosition().y(), sideZ));
            Block above2 = instance.getBlock(new Pos(sideX, player.getPosition().y() + 1, sideZ));

            // Check if the two blocks above the side block are air
            if (sideBlock != Block.AIR && above1 == Block.AIR && above2 == Block.AIR) {
                toReturn = 1.0; // The player is on the edge of the block
            }
        }

        return toReturn;
    }

}

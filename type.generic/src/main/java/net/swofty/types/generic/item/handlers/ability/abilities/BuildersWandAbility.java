package net.swofty.types.generic.item.handlers.ability.abilities;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.item.handlers.ability.RegisteredAbility;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class BuildersWandAbility extends RegisteredAbility {

    @SuppressWarnings("preview")
    public BuildersWandAbility() {
        super("GRAND_ARCHITECT", "Grand Architect",
                "Right-click the face of a block to extend all connected block faces.",
                AbilityActivation.RIGHT_CLICK_BLOCK, 0, new RegisteredAbility.NoAbilityCost(),
                (player, item, origin, face) -> {
                    fillConnectedFaces(player, new Pos(origin), face);
                });
    }

    private static void fillConnectedFaces(SkyBlockPlayer player, Pos origin, BlockFace face) {
        Material fillMaterial = Material.fromNamespaceId(player.getInstance().getBlock(origin).namespace());
        int blocksInInventory = player.countItem(ItemType.fromMaterial(fillMaterial));
        int blockLimit = 164;
        if (blocksInInventory < blockLimit)
            blockLimit = blocksInInventory;

        List<Pos> blocks = new ArrayList<>();
        List<Pos> blocksForUndo = new ArrayList<>(); // NOTE: To be used later
        blocks.add(origin);
        Instance w = player.getInstance();
        Vec[] check = null;
        Vec translate = null;
        int blocksPlaced = 0;

        check = switch (face) {
            case NORTH, SOUTH ->
                    new Vec[]{new Vec(-1, -1, 0), new Vec(-1, 0, 0), new Vec(-1, 1, 0), new Vec(0, -1, 0), new Vec(0, 1, 0), new Vec(1, -1, 0), new Vec(1, 0, 0), new Vec(1, 1, 0)};
            case EAST, WEST ->
                    new Vec[]{new Vec(0, -1, -1), new Vec(0, -1, 0), new Vec(0, -1, 1), new Vec(0, 0, -1), new Vec(0, 0, 1), new Vec(0, 1, -1), new Vec(0, 1, 0), new Vec(0, 1, 1)};
            case TOP, BOTTOM ->
                    new Vec[]{new Vec(-1, 0, -1), new Vec(-1, 0, 0), new Vec(-1, 0, 1), new Vec(0, 0, -1), new Vec(0, 0, 1), new Vec(1, 0, -1), new Vec(1, 0, 0), new Vec(1, 0, 1)};
        };
        translate = switch (face) {
            case NORTH -> new Vec(0, 0, -1);
            case SOUTH -> new Vec(0, 0, 1);
            case EAST -> new Vec(1, 0, 0);
            case WEST -> new Vec(-1, 0, 0);
            case TOP -> new Vec(0, 1, 0);
            case BOTTOM -> new Vec(0, -1, 0);
        };
        while (!blocks.isEmpty() && blockLimit > 0) {
            Pos l = (blocks.get(0));
            for (Vec vector : check) {
                if (w.getBlock(l.add(vector)).namespace().equals(fillMaterial.namespace()) && w
                        .getBlock(l.add(vector).add(translate)).namespace().equals(Material.AIR.namespace())) {
                    blocks.add(l.add(vector));
                }
            }
            Pos fillBlock = l.add(translate);
            // TODO: make sure player is on island
            if (player.isOnIsland()) {
                blocks.removeIf(blocks.get(0)::equals);
                if (!player.getInstance().getBlock(fillBlock).namespace().equals(fillMaterial.namespace())) {
                    player.getInstance().setBlock(fillBlock, Block.fromNamespaceId(fillMaterial.namespace()));

                    blockLimit--;
                    blocksPlaced++;
                    blocksForUndo.add(fillBlock);
                }
                if (blocksPlaced == blockLimit)
                    break;
                continue;
            }
            blocks.removeIf(blocks.get(0)::equals);
            blockLimit--;
        }
        if(blocksPlaced == 0) {
            player.sendMessage("§cYou cannot place any blocks! You do not have enough blocks to place with your Builder's wand!");
        }
        if (blocksPlaced != 0) {
            player.takeItem(ItemType.fromMaterial(fillMaterial), blocksPlaced);
            player.sendMessage("&eYou built &a" + blocksPlaced + "&e blocks!");
        }
    }
}

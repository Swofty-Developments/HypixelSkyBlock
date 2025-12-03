package net.swofty.type.skyblockgeneric.item.handlers.orbs;

import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.swofty.type.skyblockgeneric.utility.groups.Groups;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ServerOrbRegistry {
    private static final Map<String, ServerOrbHandler> HANDLERS = new HashMap<>();

    static {
        registerHandler("WHEAT_CRYSTAL_ORB_HANDLER", new ServerOrbHandler(crystalImpl -> Block.WHEAT));
        registerHandler("FLOWER_CRYSTAL_ORB_HANDLER", new ServerOrbHandler(crystalImpl -> {
            List<Material> flowers = Groups.FLOWERS;
            Material randomMaterial = flowers.get(new Random().nextInt(flowers.size()));
            return randomMaterial.block();
        }));
        registerHandler("PUMPKIN_AND_MELON_CRYSTAL_ORB_HANDLER", new ServerOrbHandler(crystalImpl -> {
            boolean isPumpkin = new Random().nextBoolean();
            return isPumpkin ? Block.PUMPKIN : Block.MELON;
        }));
        registerHandler("POTATO_CRYSTAL_ORB_HANDLER", new ServerOrbHandler(crystalImpl -> Block.POTATOES));
        registerHandler("CARROT_CRYSTAL_ORB_HANDLER", new ServerOrbHandler(crystalImpl -> Block.CARROTS));
    }

    public static void registerHandler(String id, ServerOrbHandler handler) {
        HANDLERS.put(id, handler);
    }

    public static ServerOrbHandler getHandler(String id) {
        return HANDLERS.get(id);
    }
}
package net.swofty.type.hub;

import net.minestom.server.instance.SharedInstance;
import net.swofty.type.skyblockgeneric.structure.tree.SpawnableTree;

public class ForestTreePlacement {
    public static final long DEFAULT_BASE_SEED = 12345L;

    public static void placeTrees(SharedInstance instance) {
        placeTrees(instance, DEFAULT_BASE_SEED);
    }

    public static void placeTrees(SharedInstance instance, long baseSeed) {
        long seed = baseSeed;
        SpawnableTree.LARGE_OAK.createAndRegister(-114, 74, -42, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-124, 73, -46, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-100, 72, -41, seed++, instance);
        SpawnableTree.MEDIUM_OAK.createAndRegister(-122, 73, -20, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-133, 73, -16, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-129, 72, -4, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-130, 71, 7, seed++, instance);
        SpawnableTree.MEDIUM_OAK.createAndRegister(-141, 71, 3, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-142, 69, 15, seed++, instance);
        SpawnableTree.GIANT_OAK.createAndRegister(-149, 70, 9, seed++, instance);
        SpawnableTree.MEDIUM_OAK.createAndRegister(-157, 72, 0, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-143, 73, -7, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-160, 73, -13, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-168, 72, -1, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-175, 74, -13, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-185, 74, -8, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-196, 74, -6, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-203, 74, -14, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-213, 74, -11, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-214, 73, -24, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-200, 74, -29, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-186, 74, -16, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-190, 75, -29, seed++, instance);
        SpawnableTree.GIANT_OAK.createAndRegister(-198, 74, -40, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-191, 76, -39, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-179, 74, -27, seed++, instance);
        SpawnableTree.MEDIUM_OAK.createAndRegister(-167, 74, -27, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-162, 73, -33, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-164, 76, -45, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-173, 77, -40, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-182, 77, -44, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-193, 75, -56, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-203, 74, -74, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-193, 75, -68, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-179, 75, -70, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-182, 76, -57, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-169, 76, -54, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-165, 75, -66, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-158, 75, -57, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-155, 75, -70, seed++, instance);
        SpawnableTree.GIANT_OAK.createAndRegister(-143, 76, -64, seed++, instance);
        SpawnableTree.LARGE_OAK.createAndRegister(-147, 74, -52, seed, instance);
    }
}

package net.swofty.type.skyblockgeneric.structure.tree;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.instance.Instance;
import net.swofty.type.skyblockgeneric.structure.SkyBlockStructure;

import java.util.*;

@Getter
@Setter
public class SkyBlockTree extends SkyBlockStructure {
    private TreeType treeType;
    private TreeConfig config;
    private long seed;
    private Random random;

    private final Set<LogPosition> placedLogs = new HashSet<>();
    private final Set<LogPosition> placedLeaves = new HashSet<>();
    private final List<LogPosition> branchEndpoints = new ArrayList<>();

    // Calculated per-tree
    private int targetWidth;

    public SkyBlockTree(int rotation, int x, int y, int z, TreeType treeType) {
        super(rotation, x, y, z);
        this.treeType = treeType;
        this.config = treeType.getDefaultConfig();
        this.seed = System.currentTimeMillis();
        this.random = new Random(seed);
    }

    public SkyBlockTree(int rotation, int x, int y, int z, TreeType treeType, TreeConfig config) {
        super(rotation, x, y, z);
        this.treeType = treeType;
        this.config = config;
        this.seed = System.currentTimeMillis();
        this.random = new Random(seed);
    }

    public SkyBlockTree(int rotation, int x, int y, int z, TreeType treeType, TreeConfig config, long seed) {
        super(rotation, x, y, z);
        this.treeType = treeType;
        this.config = config;
        this.seed = seed;
        this.random = new Random(seed);
    }

    @Override
    public void setBlocks(Instance instance) {
        placedLogs.clear();
        placedLeaves.clear();
        branchEndpoints.clear();
        random = new Random(seed);

        int trunkHeight = config.minHeight() +
                random.nextInt(config.maxHeight() - config.minHeight() + 1);

        // Calculate target width for this tree
        targetWidth = config.minWidth() +
                random.nextInt(config.maxWidth() - config.minWidth() + 1);

        // 1. Generate root flare at base for taller trees
        generateRootFlare(instance, trunkHeight);

        // 2. Grow the main trunk (1-wide) with subtle slanting
        growMainTrunk(instance, trunkHeight);

        // 3. Generate unified canopy from all branch endpoints
        generateUnifiedCanopy(instance);
    }

    private void generateRootFlare(Instance instance, int height) {
        // Always place center log at y=0
        placeLog(instance, 0, 0, 0);

        // For taller trees, add root flare
        if (height >= 8) {
            int numRoots = Math.min(4, 1 + (height - 8) / 4);

            List<int[]> rootOffsets = new ArrayList<>(Arrays.asList(
                new int[]{1, 0}, new int[]{-1, 0}, new int[]{0, 1}, new int[]{0, -1},
                new int[]{1, 1}, new int[]{-1, 1}, new int[]{1, -1}, new int[]{-1, -1}
            ));
            Collections.shuffle(rootOffsets, random);

            for (int i = 0; i < numRoots && i < rootOffsets.size(); i++) {
                int[] offset = rootOffsets.get(i);
                placeLog(instance, offset[0], 0, offset[1]);
            }
        }

        // For very tall trees, add a second layer of roots
        if (height >= 12) {
            placeLog(instance, 0, 1, 0);
            if (random.nextDouble() < 0.5) {
                int[] dirs = {-1, 0, 1};
                int dx = dirs[random.nextInt(3)];
                int dz = dirs[random.nextInt(3)];
                if (dx != 0 || dz != 0) {
                    placeLog(instance, dx, 1, dz);
                }
            }
        }
    }

    private void growMainTrunk(Instance instance, int height) {
        int currentX = 0;
        int currentZ = 0;
        int lastSlantY = -10;

        BranchDirection slantDir = null;
        if (random.nextDouble() < config.slantChance()) {
            slantDir = BranchDirection.randomCardinal();
        }

        int startY = (height >= 8) ? 1 : 0;

        // Track which directions we've spawned branches in to spread them out
        Set<BranchDirection> usedDirections = new HashSet<>();

        for (int y = startY; y < height; y++) {
            placeLog(instance, currentX, y, currentZ);

            double progress = (double) y / height;

            // Spawn branches in upper portion of tree
            // Increase branch chance and ensure we spawn enough to reach target width
            if (progress >= 0.4) {
                double adjustedBranchChance = config.branchChance() * (1.0 + targetWidth * 0.1);

                if (random.nextDouble() < adjustedBranchChance) {
                    // Try to pick an unused direction for better spread
                    BranchDirection branchDir = pickBranchDirection(usedDirections);
                    usedDirections.add(branchDir);

                    // Branch length scales with target width
                    int minBranchLen = Math.max(2, targetWidth - 2);
                    int maxBranchLen = targetWidth + 1;
                    int branchLength = minBranchLen + random.nextInt(maxBranchLen - minBranchLen + 1);

                    growBranch(instance, currentX, y, currentZ, branchDir, branchLength, height - y);
                }
            }

            // Apply subtle slanting
            if (slantDir != null && (y - lastSlantY) >= 3 && random.nextDouble() < 0.12) {
                currentX += slantDir.getXOffset();
                currentZ += slantDir.getZOffset();
                lastSlantY = y;

                if (random.nextDouble() < 0.08) {
                    slantDir = BranchDirection.randomCardinal();
                }
            }
        }

        // The trunk top is a branch endpoint
        branchEndpoints.add(new LogPosition(currentX, height, currentZ));
    }

    private BranchDirection pickBranchDirection(Set<BranchDirection> used) {
        // Prefer unused directions for even spread
        List<BranchDirection> available = new ArrayList<>();
        for (BranchDirection d : new BranchDirection[]{
                BranchDirection.NORTH, BranchDirection.SOUTH,
                BranchDirection.EAST, BranchDirection.WEST}) {
            if (!used.contains(d)) {
                available.add(d);
            }
        }
        if (!available.isEmpty()) {
            return available.get(random.nextInt(available.size()));
        }
        // All used, pick random
        return BranchDirection.randomCardinal();
    }

    private void growBranch(Instance instance, int startX, int startY, int startZ,
                            BranchDirection direction, int length, int heightRemaining) {
        int x = startX;
        int y = startY;
        int z = startZ;

        for (int i = 0; i < length; i++) {
            // Move outward
            x += direction.getXOffset();
            z += direction.getZOffset();

            // Branches angle upward - but less aggressively for wider trees
            double upChance = 0.5 - (targetWidth * 0.03); // Less up for wider trees
            upChance = Math.max(0.25, upChance);
            if (random.nextDouble() < upChance) {
                y++;
            }

            placeLog(instance, x, y, z);

            // Chance to spawn sub-branch
            if (i >= 1 && random.nextDouble() < 0.3) {
                BranchDirection subDir = getPerpendicularDirection(direction);
                int subLength = 1 + random.nextInt(Math.max(1, targetWidth / 2));
                growSubBranch(instance, x, y, z, subDir, subLength);
            }
        }

        branchEndpoints.add(new LogPosition(x, y, z));
    }

    private void growSubBranch(Instance instance, int startX, int startY, int startZ,
                               BranchDirection direction, int length) {
        int x = startX;
        int y = startY;
        int z = startZ;

        for (int i = 0; i < length; i++) {
            x += direction.getXOffset();
            z += direction.getZOffset();
            if (random.nextDouble() < 0.4) {
                y++;
            }
            placeLog(instance, x, y, z);
        }

        branchEndpoints.add(new LogPosition(x, y, z));
    }

    private BranchDirection getPerpendicularDirection(BranchDirection current) {
        if (current == BranchDirection.NORTH || current == BranchDirection.SOUTH) {
            return random.nextBoolean() ? BranchDirection.EAST : BranchDirection.WEST;
        } else {
            return random.nextBoolean() ? BranchDirection.NORTH : BranchDirection.SOUTH;
        }
    }

    private void generateUnifiedCanopy(Instance instance) {
        if (branchEndpoints.isEmpty()) {
            return;
        }

        // Find centroid of all endpoints
        int sumX = 0, sumY = 0, sumZ = 0;

        for (LogPosition pos : branchEndpoints) {
            sumX += pos.x();
            sumY += pos.y();
            sumZ += pos.z();
        }

        int centerX = sumX / branchEndpoints.size();
        int centerY = sumY / branchEndpoints.size();
        int centerZ = sumZ / branchEndpoints.size();

        // Calculate required radius based on furthest log from centroid
        // This ensures all logs are covered by leaves
        double maxLogDistance = 0;
        for (LogPosition pos : placedLogs) {
            double dx = pos.x() - centerX;
            double dz = pos.z() - centerZ;
            double dist = Math.sqrt(dx * dx + dz * dz);
            maxLogDistance = Math.max(maxLogDistance, dist);
        }

        // 20% of trees are "bulbous" with fuller leaves, 80% are sparser
        boolean isBulbous = random.nextDouble() < 0.2;

        // Radius buffer: bulbous trees get +2, sparse trees get +1
        int radiusBuffer = isBulbous ? 2 : 1;
        int radius = Math.max((int) Math.ceil(maxLogDistance) + radiusBuffer, config.leafRadius());

        // Extra skip chance for sparse trees
        double sparseSkipBonus = isBulbous ? 0.0 : 0.15;

        // Generate the unified canopy
        // Leaves don't hang as low: only go down 1-2 blocks max instead of radius/2
        int minDy = isBulbous ? -2 : -1;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = minDy; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    int leafX = centerX + dx;
                    int leafY = centerY + dy;
                    int leafZ = centerZ + dz;

                    double horizontalDist = Math.sqrt(dx * dx + dz * dz);
                    double verticalScale = 1.4;
                    double totalDist = Math.sqrt(dx * dx + (dy * verticalScale) * (dy * verticalScale) + dz * dz);

                    double threshold = radius - 0.3 + random.nextDouble() * 0.6;

                    if (totalDist <= threshold) {
                        LogPosition pos = new LogPosition(leafX, leafY, leafZ);
                        if (!placedLogs.contains(pos)) {
                            double skipChance = calculateLeafSkipChance(horizontalDist, dy, radius, totalDist) + sparseSkipBonus;
                            if (random.nextDouble() > skipChance) {
                                set(instance, leafX, leafY, leafZ, treeType.getLeavesBlock());
                                placedLeaves.add(pos);
                            }
                        }
                    }
                }
            }
        }
    }

    private double calculateLeafSkipChance(double horizontalDist, int dy, int radius, double totalDist) {
        double skipChance = 0.08;

        double innerRadius = radius * 0.35;
        if (horizontalDist < innerRadius) {
            skipChance += 0.45 * (1.0 - horizontalDist / innerRadius);
        }

        if (dy < 0) {
            skipChance += 0.3;
            if (dy < -1) {
                skipChance += 0.15;
            }
        }

        if (horizontalDist > radius * 0.7) {
            skipChance *= 0.4;
        }

        if (dy > radius * 0.5) {
            skipChance *= 0.6;
        }

        if (totalDist > radius - 1.2) {
            skipChance += random.nextDouble() * 0.25;
        }

        return Math.min(0.8, skipChance);
    }

    private void placeLog(Instance instance, int x, int y, int z) {
        LogPosition pos = new LogPosition(x, y, z);
        if (!placedLogs.contains(pos)) {
            set(instance, x, y, z, treeType.getLogBlock());
            placedLogs.add(pos);
        }
    }

    @Override
    public List<StructureHologram> getHolograms() {
        return List.of();
    }

    public Set<LogPosition> getPlacedLogs() {
        return Collections.unmodifiableSet(placedLogs);
    }

    public Set<LogPosition> getPlacedLeaves() {
        return Collections.unmodifiableSet(placedLeaves);
    }

    public record LogPosition(int x, int y, int z) {}
}

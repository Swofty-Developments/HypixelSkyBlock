package net.swofty.types.generic.utility;

import net.minestom.server.coordinate.Pos;

import java.util.ArrayList;
import java.util.List;

public record NumberVisualizer(Pos origin, int number, int width, int height) {
    private static final int[][][] NUMBER_PATTERNS = {
            {{1, 1, 1}, {1, 0, 1}, {1, 0, 1}, {1, 0, 1}, {1, 1, 1}}, // 0
            {{0, 1, 0}, {1, 1, 0}, {0, 1, 0}, {0, 1, 0}, {1, 1, 1}}, // 1
            {{1, 1, 1}, {0, 0, 1}, {1, 1, 1}, {1, 0, 0}, {1, 1, 1}}, // 2
            {{1, 1, 1}, {0, 0, 1}, {1, 1, 1}, {0, 0, 1}, {1, 1, 1}}, // 3
            {{1, 0, 1}, {1, 0, 1}, {1, 1, 1}, {0, 0, 1}, {0, 0, 1}}, // 4
            {{1, 1, 1}, {1, 0, 0}, {1, 1, 1}, {0, 0, 1}, {1, 1, 1}}, // 5
            {{1, 1, 1}, {1, 0, 0}, {1, 1, 1}, {1, 0, 1}, {1, 1, 1}}, // 6
            {{1, 1, 1}, {0, 0, 1}, {0, 0, 1}, {0, 0, 1}, {0, 0, 1}}, // 7
            {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}, {1, 0, 1}, {1, 1, 1}}, // 8
            {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}, {0, 0, 1}, {1, 1, 1}}  // 9
    };

    public NumberVisualizer(Pos origin, int number) {
        this(origin, number, 5, 7);
    }

    private int[][] scaleNumberData(int[][] basePattern) {
        int baseHeight = basePattern.length;
        int baseWidth = basePattern[0].length;
        int[][] scaledPattern = new int[height][width];

        // Calculate the scale factor for width and height
        double widthScale = (double) width / baseWidth;
        double heightScale = (double) height / baseHeight;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int originalX = (int)(x / widthScale);
                int originalY = (int)(y / heightScale);
                scaledPattern[y][x] = basePattern[originalY][originalX];
            }
        }

        return scaledPattern;
    }

    private int[][] flipPattern(int[][] pattern, boolean horizontalFlip, boolean verticalFlip) {
        int[][] flippedPattern = new int[pattern.length][pattern[0].length];

        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[y].length; x++) {
                int newX = horizontalFlip ? pattern[y].length - 1 - x : x;
                int newY = verticalFlip ? pattern.length - 1 - y : y;
                flippedPattern[newY][newX] = pattern[y][x];
            }
        }

        return flippedPattern;
    }

    public List<Pos> getVisualizedNumber(boolean horizontalFlip, boolean verticalFlip) {
        List<Pos> positions = new ArrayList<>();
        // Ensure the number is between 0 and 9
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Number must be between 0 and 9.");
        }

        // Get base pattern, scale it, and apply flips
        int[][] basePattern = NUMBER_PATTERNS[number];
        int[][] scaledPattern = scaleNumberData(basePattern);
        int[][] finalPattern = flipPattern(scaledPattern, horizontalFlip, verticalFlip);

        for (int y = 0; y < finalPattern.length; y++) {
            for (int x = 0; x < finalPattern[y].length; x++) {
                if (finalPattern[y][x] == 1) {
                    positions.add(origin.add(x, 0, -y)); // Adjusted for 3D coordinates
                }
            }
        }
        return positions;
    }
}

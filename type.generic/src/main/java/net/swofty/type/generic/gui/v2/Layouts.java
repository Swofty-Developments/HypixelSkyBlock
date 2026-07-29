package net.swofty.type.generic.gui.v2;

import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public final class Layouts {

    public static List<Integer> rectangle(int from, int to) {
        int startRow = from / 9;
        int endRow = to / 9;
        int startCol = from % 9;
        int endCol = to % 9;

        List<Integer> slots = new ArrayList<>();

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                slots.add(row * 9 + col);
            }
        }

        return slots;
    }

    public static List<Integer> border(@Range(from = 0, to = 53) int from, @Range(from = 0, to = 53) int to) {
        int startRow = from / 9;
        int endRow = to / 9;
        int startCol = from % 9;
        int endCol = to % 9;

        List<Integer> slots = new ArrayList<>();

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                if (row == startRow || row == endRow || col == startCol || col == endCol) {
                    slots.add(row * 9 + col);
                }
            }
        }

        return slots;
    }

    public static List<Integer> row(@Range(from = 0, to = 5) int row) {
        return IntStream.range(0, 9).map(i -> row * 9 + i).boxed().toList();
    }
}

package net.swofty.type.ravengarddungeon.game;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.util.List;
import java.util.Map;

public final class DungeonMapGlyphs {
    public record RoomArt(int cols, int rows, int w, int h, List<List<Integer>> tiles) {
    }

    private static Map<String, Map<String, RoomArt>> byRoom;

    private DungeonMapGlyphs() {
    }

    public static synchronized Map<String, Map<String, RoomArt>> load() {
        if (byRoom == null) {
            try (FileReader reader = new FileReader("./configuration/ravengard/dungeon_map_glyphs.json")) {
                byRoom = new Gson().fromJson(reader,
                        new TypeToken<Map<String, Map<String, RoomArt>>>() {}.getType());
            } catch (Exception exception) {
                org.tinylog.Logger.error(exception, "Failed to load dungeon map glyphs");
                byRoom = Map.of();
            }
        }
        return byRoom;
    }

    public static RoomArt artFor(String roomId, int rotationDegrees) {
        Map<String, RoomArt> rotations = load().get(roomId);
        return rotations == null ? null : rotations.get(String.valueOf(rotationDegrees));
    }
}

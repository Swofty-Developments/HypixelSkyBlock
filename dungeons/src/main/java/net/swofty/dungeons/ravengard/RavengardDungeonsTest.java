package net.swofty.dungeons.ravengard;

import java.nio.file.Path;

public class RavengardDungeonsTest {
    static void main(String[] args) {
        long seed = args.length > 0 ? Long.parseLong(args[0]) : System.currentTimeMillis();
        int rooms = args.length > 1 ? Integer.parseInt(args[1]) : 24;
        Path catalog = Path.of(args.length > 2 ? args[2] : "./configuration/ravengard/dungeon_rooms.json");

        long started = System.currentTimeMillis();
        RavengardDungeon layout = RavengardDungeon.generate(
                RavengardRoomCatalog.load(catalog), seed, rooms);

        if (args.length > 3 && args[3].equals("--json")) {
            StringBuilder json = new StringBuilder("{\"placements\":[");
            boolean first = true;
            for (var placement : layout.getPlacements()) {
                if (!first) json.append(',');
                first = false;
                json.append("{\"room\":\"").append(placement.room().getId())
                        .append("\",\"rotation\":").append(placement.rotation().getDegrees())
                        .append(",\"x\":").append(placement.originX())
                        .append(",\"z\":").append(placement.originZ()).append('}');
            }
            json.append("]}");
            System.out.println(json);
            return;
        }
        System.out.println("Generated dungeon: \n" + layout);
        System.out.println((System.currentTimeMillis() - started) + "ms");
    }
}

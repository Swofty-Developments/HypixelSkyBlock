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

        System.out.println("Generated dungeon: \n" + layout);
        System.out.println((System.currentTimeMillis() - started) + "ms");
    }
}

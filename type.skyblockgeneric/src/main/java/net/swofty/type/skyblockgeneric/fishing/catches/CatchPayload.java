package net.swofty.type.skyblockgeneric.fishing.catches;

import net.kyori.adventure.text.Component;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.skyblockgeneric.data.SkyBlockDataHandler;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointCollection;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointTrophyFish;
import net.swofty.type.skyblockgeneric.entity.mob.seacreature.SeaCreatureSpawner;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

/**
 * Sealed view of what the rod can pull out of the water. Each variant
 * carries exactly its own fields — no bag of nullable @Nullable strings —
 * and owns its own {@link #apply(CatchAwardContext)} so the FishingService
 * dispatches by polymorphism rather than switch-on-kind.
 */
public sealed interface CatchPayload {

    /** Fishing skill XP to award; sea creatures intentionally return 0 (their kill awards XP separately). */
    double skillXp();

    /** Short human label for telemetry / chat — variants pick the format. */
    String summary();

    /** Run the catch's side-effects on the player. */
    void apply(CatchAwardContext ctx);

    /* ------------------------------------------------------------------ */
    /*  Variants                                                          */
    /* ------------------------------------------------------------------ */

    record Item(String itemId, int amount, double skillXp, boolean fromTreasure) implements CatchPayload {
        @Override public String summary() { return (fromTreasure ? "treasure " : "") + amount + "x " + itemId; }

        @Override
        public void apply(CatchAwardContext ctx) {
            ItemType type = ItemType.valueOf(itemId);
            ctx.player().addAndUpdateItem(type, amount);
            ctx.player().getCollection().increase(type, amount);
            ctx.player().getSkyblockDataHandler()
                    .get(SkyBlockDataHandler.Data.COLLECTION, DatapointCollection.class)
                    .setValue(ctx.player().getCollection());

            String prefix = fromTreasure ? "§b§lTREASURE! §7" : "§b§lGOOD CATCH! §7";
            ctx.player().sendMessage(Component.text(prefix + "You found " + amount + "x §a" + itemId + "§7!"));
        }
    }

    record SeaCreature(String seaCreatureId, double skillXp) implements CatchPayload {
        @Override public String summary() { return "sea creature " + seaCreatureId; }

        @Override
        public void apply(CatchAwardContext ctx) {
            SeaCreatureSpawner.spawn(ctx.player(), seaCreatureId, ctx.hookPosition());
        }
    }

    record TrophyFish(String definitionId, TrophyTier tier, String itemId, double skillXp) implements CatchPayload {
        @Override public String summary() { return "trophy " + tier.name() + " " + definitionId; }

        @Override
        public void apply(CatchAwardContext ctx) {
            ItemType type = ItemType.valueOf(itemId);
            ctx.player().addAndUpdateItem(type, 1);

            DatapointTrophyFish.TrophyFishData data = ctx.player().getTrophyFishData();
            data.getProgress(definitionId).increment(tier.name());
            ctx.player().getSkyblockDataHandler()
                    .get(SkyBlockDataHandler.Data.TROPHY_FISH, DatapointTrophyFish.class)
                    .setValue(data);

            ctx.player().sendMessage(Component.text("§b§lTROPHY FISH! §7You reeled in a "
                    + tier.colour() + tier.displayName() + " §a" + definitionId + "§7!"));
        }
    }

    record Quest(String itemId, int amount, double skillXp, String message) implements CatchPayload {
        @Override public String summary() { return "quest " + itemId; }

        @Override
        public void apply(CatchAwardContext ctx) {
            ctx.player().addAndUpdateItem(ItemType.valueOf(itemId), amount);
            ctx.player().sendMessage(Component.text("§d§lQUEST CATCH! §7" + message));
        }
    }

    record Special(String itemId, int amount, double skillXp, boolean corrupted) implements CatchPayload {
        @Override public String summary() { return "special " + itemId; }

        @Override
        public void apply(CatchAwardContext ctx) {
            ItemType type = ItemType.valueOf(itemId);
            ctx.player().addAndUpdateItem(type, amount);
            ctx.player().getCollection().increase(type, amount);
            String prefix = corrupted ? "§5§lCORRUPTED! §7" : "§d§lWONDROUS CATCH! §7";
            ctx.player().sendMessage(Component.text(prefix + "You found §a" + itemId + "§7!"));
        }
    }
}

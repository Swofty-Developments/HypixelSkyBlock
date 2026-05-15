package net.swofty.dungeons.catacombs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.swofty.dungeons.GeneratorService;
import net.swofty.dungeons.SkyBlockDungeon;
import net.swofty.dungeons.catacombs.classes.DungeonClassDefinition;
import net.swofty.dungeons.catacombs.classes.DungeonClassRegistry;
import net.swofty.dungeons.catacombs.classes.DungeonClassType;
import net.swofty.dungeons.catacombs.generation.CatacombsGenerator;
import net.swofty.dungeons.catacombs.instance.CatacombsInstanceService;
import net.swofty.dungeons.catacombs.item.DungeonOrbProfile;
import net.swofty.dungeons.catacombs.kit.DungeonClassKit;
import net.swofty.dungeons.catacombs.kit.DungeonKitRegistry;
import net.swofty.dungeons.catacombs.map.DungeonMapRenderResult;
import net.swofty.dungeons.catacombs.map.DungeonMapRenderer;
import net.swofty.dungeons.catacombs.mob.DungeonMobDefinition;
import net.swofty.dungeons.catacombs.mob.DungeonMobRegistry;
import net.swofty.dungeons.catacombs.mob.DungeonMobRole;
import net.swofty.dungeons.catacombs.party.PartyFinderService;
import net.swofty.dungeons.catacombs.puzzle.CatacombsPuzzle;
import net.swofty.dungeons.catacombs.puzzle.CatacombsPuzzleFactory;
import net.swofty.dungeons.catacombs.puzzle.PuzzleController;
import net.swofty.dungeons.catacombs.run.CatacombsRunConfig;
import net.swofty.dungeons.catacombs.run.CatacombsRunState;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CatacombsAPI {
    private static final DungeonClassRegistry CLASS_REGISTRY = DungeonClassRegistry.defaults();
    private static final CatacombsRegistry CATACOMBS_REGISTRY = CatacombsRegistry.defaults();
    private static final DungeonMobRegistry MOB_REGISTRY = DungeonMobRegistry.defaults();
    private static final DungeonKitRegistry KIT_REGISTRY = DungeonKitRegistry.defaults();
    private static final DungeonMapRenderer MAP_RENDERER = new DungeonMapRenderer();

    public static CatacombsFloorDefinition floor(CatacombsFloor floor, CatacombsMode mode) {
        return CATACOMBS_REGISTRY.floor(floor, mode);
    }

    public static DungeonClassDefinition dungeonClass(DungeonClassType type) {
        return CLASS_REGISTRY.definition(type);
    }

    public static Map<DungeonClassType, DungeonClassDefinition> classes() {
        return CLASS_REGISTRY.definitions();
    }

    public static DungeonOrbProfile dungeonOrb(DungeonClassType type) {
        return DungeonOrbProfile.forClass(dungeonClass(type));
    }

    public static List<DungeonClassKit> kits(DungeonClassType type) {
        return KIT_REGISTRY.kits(type);
    }

    public static DungeonClassKit kit(String id) {
        return KIT_REGISTRY.kit(id);
    }

    public static PartyFinderService partyFinder() {
        return new PartyFinderService();
    }

    public static CatacombsInstanceService instances() {
        return new CatacombsInstanceService();
    }

    public static List<DungeonMobDefinition> mobs(CatacombsFloor floor, DungeonMobRole role) {
        return MOB_REGISTRY.spawnable(floor, role);
    }

    public static DungeonMobDefinition mob(String id) {
        return MOB_REGISTRY.mob(id);
    }

    public static PuzzleController puzzle(CatacombsPuzzle puzzle) {
        return CatacombsPuzzleFactory.create(puzzle);
    }

    public static GeneratorService generator(CatacombsFloorDefinition definition) {
        return CatacombsGenerator.generator(definition);
    }

    public static DungeonMapRenderResult renderMap(SkyBlockDungeon dungeon, Path outputPath) throws IOException {
        return MAP_RENDERER.renderPng(dungeon, outputPath);
    }

    public static CatacombsRunState startRun(CatacombsRunConfig config) {
        return CatacombsRunState.start(config);
    }
}

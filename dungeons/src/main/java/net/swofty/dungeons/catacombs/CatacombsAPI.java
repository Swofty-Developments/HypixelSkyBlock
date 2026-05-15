package net.swofty.dungeons.catacombs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.swofty.dungeons.GeneratorService;
import net.swofty.dungeons.catacombs.classes.DungeonClassDefinition;
import net.swofty.dungeons.catacombs.classes.DungeonClassRegistry;
import net.swofty.dungeons.catacombs.classes.DungeonClassType;
import net.swofty.dungeons.catacombs.generation.CatacombsGenerator;
import net.swofty.dungeons.catacombs.item.DungeonOrbProfile;
import net.swofty.dungeons.catacombs.run.CatacombsRunConfig;
import net.swofty.dungeons.catacombs.run.CatacombsRunState;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CatacombsAPI {
    private static final DungeonClassRegistry CLASS_REGISTRY = DungeonClassRegistry.defaults();
    private static final CatacombsRegistry CATACOMBS_REGISTRY = CatacombsRegistry.defaults();

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

    public static GeneratorService generator(CatacombsFloorDefinition definition) {
        return CatacombsGenerator.generator(definition);
    }

    public static CatacombsRunState startRun(CatacombsRunConfig config) {
        return CatacombsRunState.start(config);
    }
}

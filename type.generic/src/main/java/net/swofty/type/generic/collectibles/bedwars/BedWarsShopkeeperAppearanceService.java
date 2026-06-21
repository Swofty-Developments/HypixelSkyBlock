package net.swofty.type.generic.collectibles.bedwars;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.VillagerProfession;
import net.swofty.type.generic.collectibles.CollectibleCategory;
import net.swofty.type.generic.collectibles.CollectibleDefinition;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BedWarsShopkeeperAppearanceService {

    private static final String DEFAULT_SHOPKEEPER_ID = "blacksmith";

    public static ShopkeeperAppearance resolveSelected(HypixelPlayer player) {
        Optional<CollectibleDefinition> selected = BedWarsCollectibleStateService.resolveSelected(
            player,
            CollectibleCategory.SHOPKEEPER_SKINS
        );

        if (selected.isEmpty()) {
            selected = BedWarsCollectibleCatalog.findItemById(DEFAULT_SHOPKEEPER_ID);
        }

        return selected.map(BedWarsShopkeeperAppearanceService::resolve).orElse(defaultAppearance());
    }

    public static ShopkeeperAppearance resolve(CollectibleDefinition definition) {
        Map<String, String> customData = definition.customData();

        String textureValue = clean(customData.get("textures"));
        if (textureValue != null) {
            return ShopkeeperAppearance.human(textureValue, clean(customData.get("signature")));
        }

        EntityType entityType = resolveEntityType(clean(customData.get("entity")), definition.id());
        if (entityType == EntityType.VILLAGER) {
            VillagerProfession profession = resolveVillagerProfession(clean(customData.get("villager")));
            return ShopkeeperAppearance.villager(profession);
        }

        return ShopkeeperAppearance.mob(entityType);
    }

    public static ShopkeeperAppearance defaultAppearance() {
        return ShopkeeperAppearance.villager(VillagerProfession.WEAPONSMITH);
    }

    private static String clean(String value) {
        if (value == null) {
            return null;
        }

        String cleaned = value.trim();
        if (cleaned.isEmpty() || cleaned.equalsIgnoreCase("null")) {
            return null;
        }
        return cleaned;
    }

    private static VillagerProfession resolveVillagerProfession(String rawProfession) {
        if (rawProfession == null) {
            return VillagerProfession.WEAPONSMITH;
        }

        String normalized = normalizeKey(rawProfession);
        return switch (normalized) {
            case "BLACKSMITH", "WEAPONSMITH" -> VillagerProfession.WEAPONSMITH;
            case "BUTCHER" -> VillagerProfession.BUTCHER;
            case "LIBRARIAN" -> VillagerProfession.LIBRARIAN;
            case "NITWIT" -> VillagerProfession.NITWIT;
            case "NONE" -> VillagerProfession.NONE;
            case "TOOLSMITH" -> VillagerProfession.TOOLSMITH;
            default -> VillagerProfession.WEAPONSMITH;
        };
    }

    private static EntityType resolveEntityType(String rawEntityType, String definitionId) {
        if (rawEntityType != null) {
            return switch (normalizeKey(rawEntityType)) {
                case "VILLAGER" -> EntityType.VILLAGER;
                case "SKELETON" -> EntityType.SKELETON;
                case "WITHER_SKELETON" -> EntityType.WITHER_SKELETON;
                case "BLAZE" -> EntityType.BLAZE;
                case "ZOMBIE" -> EntityType.ZOMBIE;
                case "ZOMBIE_VILLAGER", "VILLAGER_ZOMBIE" -> EntityType.ZOMBIE_VILLAGER;
                case "WITCH" -> EntityType.WITCH;
                case "CREEPER" -> EntityType.CREEPER;
                case "ZOMBIFIED_PIGLIN", "ZOMBIE_PIGMAN", "PIG_ZOMBIE" -> EntityType.ZOMBIFIED_PIGLIN;
                default -> inferFromDefinitionId(definitionId);
            };
        }

        return inferFromDefinitionId(definitionId);
    }

    private static EntityType inferFromDefinitionId(String definitionId) {
        return switch (definitionId) {
            case "skeleton" -> EntityType.SKELETON;
            case "wither_skeleton" -> EntityType.WITHER_SKELETON;
            case "blaze" -> EntityType.BLAZE;
            case "zombie" -> EntityType.ZOMBIE;
            case "witch" -> EntityType.WITCH;
            case "creeper" -> EntityType.CREEPER;
            case "zombie_pigman" -> EntityType.ZOMBIFIED_PIGLIN;
            case "villager_zombie" -> EntityType.ZOMBIE_VILLAGER;
            default -> EntityType.VILLAGER;
        };
    }

    private static String normalizeKey(String value) {
        return value.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
    }

    public enum AppearanceKind {
        VILLAGER,
        MOB,
        HUMAN
    }

    public record ShopkeeperAppearance(
        AppearanceKind kind,
        EntityType entityType,
        VillagerProfession villagerProfession,
        String textureValue,
        String textureSignature
    ) {
        public static ShopkeeperAppearance villager(VillagerProfession profession) {
            VillagerProfession resolved = profession == null ? VillagerProfession.WEAPONSMITH : profession;
            return new ShopkeeperAppearance(AppearanceKind.VILLAGER, EntityType.VILLAGER, resolved, null, null);
        }

        public static ShopkeeperAppearance mob(EntityType entityType) {
            EntityType resolved = entityType == null ? EntityType.VILLAGER : entityType;
            if (resolved == EntityType.VILLAGER) {
                return villager(VillagerProfession.WEAPONSMITH);
            }
            return new ShopkeeperAppearance(AppearanceKind.MOB, resolved, null, null, null);
        }

        public static ShopkeeperAppearance human(String textureValue, String textureSignature) {
            if (textureValue == null || textureValue.isBlank()) {
                return villager(VillagerProfession.WEAPONSMITH);
            }
            return new ShopkeeperAppearance(
                AppearanceKind.HUMAN,
                EntityType.PLAYER,
                null,
                textureValue,
                textureSignature == null ? "" : textureSignature
            );
        }

        public boolean isHuman() {
            return kind == AppearanceKind.HUMAN;
        }

        public int replayEntityTypeId() {
            return entityType.id();
        }
    }
}

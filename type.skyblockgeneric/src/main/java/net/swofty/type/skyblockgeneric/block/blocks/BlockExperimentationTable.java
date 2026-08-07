package net.swofty.type.skyblockgeneric.block.blocks;

import lombok.NonNull;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.tag.Tag;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.impl.BlockBreakable;
import net.swofty.type.skyblockgeneric.block.impl.BlockInteractable;
import net.swofty.type.skyblockgeneric.block.impl.BlockPlaceable;
import net.swofty.type.skyblockgeneric.block.impl.CustomSkyBlockBlock;
import net.swofty.type.skyblockgeneric.furniture.Furniture;
import net.swofty.type.skyblockgeneric.gui.inventories.experiments.GUIExperiments;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class BlockExperimentationTable implements CustomSkyBlockBlock, BlockPlaceable, BlockInteractable, BlockBreakable {
    private static final Tag<String> TABLE_ID_TAG = Tag.String("experimentation_table_id");
    private static final Tag<String> ENTITY_TABLE_ID_TAG = Tag.String("experimentation_table");
    private static final Map<Placement, List<LivingEntity>> PLACED_TABLES = new ConcurrentHashMap<>();
    private static final Map<UUID, Placement> PLACEMENTS_BY_ID = new ConcurrentHashMap<>();

    @Override
    public @NonNull Block getDisplayMaterial() {
        return Block.ENCHANTING_TABLE;
    }

    @Override
    public @NonNull Boolean shouldPlace(SkyBlockPlayer player) {
        return HypixelConst.isIslandServer();
    }

    @Override
    public @NonNull Boolean shouldDestroy(SkyBlockPlayer player) {
        return HypixelConst.isIslandServer();
    }

    @Override
    public void onPlace(PlayerBlockPlaceEvent event, SkyBlockBlock block) {
        if (!HypixelConst.isIslandServer()) {
            event.setCancelled(true);
            return;
        }

        Point position = event.getBlockPosition();
        Placement placement = new Placement(event.getInstance(), position.x(), position.y(), position.z());
        remove(placement);

        UUID tableId = UUID.randomUUID();
        List<LivingEntity> entities = Furniture.load(
                "experimentation_table",
                event.getInstance(),
                position.asPos().add(0.5, 1.5, 0.5)
        );
        entities.forEach(entity -> entity.setTag(ENTITY_TABLE_ID_TAG, tableId.toString()));

        PLACED_TABLES.put(placement, entities);
        PLACEMENTS_BY_ID.put(tableId, placement);
        event.setBlock(block.toBlock().withTag(TABLE_ID_TAG, tableId.toString()));
    }

    @Override
    public void onInteract(PlayerBlockInteractEvent event, SkyBlockBlock block) {
        if (!HypixelConst.isIslandServer()) return;

        event.setBlockingItemUse(true);
        ((SkyBlockPlayer) event.getPlayer()).openView(new GUIExperiments());
    }

    @Override
    public void onBreak(PlayerBlockBreakEvent event, SkyBlockBlock block) {
        if (!HypixelConst.isIslandServer()) return;

        String tableId = event.getBlock().getTag(TABLE_ID_TAG);
        if (tableId != null) {
            try {
                if (!remove(UUID.fromString(tableId))) {
                    remove(new Placement(event.getInstance(), event.getBlockPosition().x(),
                            event.getBlockPosition().y(), event.getBlockPosition().z()));
                }
            } catch (IllegalArgumentException ignored) {
                remove(new Placement(event.getInstance(), event.getBlockPosition().x(),
                        event.getBlockPosition().y(), event.getBlockPosition().z()));
            }
        } else {
            remove(new Placement(event.getInstance(), event.getBlockPosition().x(), event.getBlockPosition().y(), event.getBlockPosition().z()));
        }

        event.setResultBlock(Block.AIR);
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            ((SkyBlockPlayer) event.getPlayer()).addAndUpdateItem(new SkyBlockItem(ItemType.ENCHANTING_TABLE));
        }
    }

    public static void interactWithPart(SkyBlockPlayer player, String tableId) {
        if (!HypixelConst.isIslandServer()) return;

        try {
            if (PLACEMENTS_BY_ID.containsKey(UUID.fromString(tableId))) {
                player.openView(new GUIExperiments());
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    public static void destroyFromPart(SkyBlockPlayer player, String tableId) {
        if (!HypixelConst.isIslandServer()) return;

        try {
            Placement placement = PLACEMENTS_BY_ID.get(UUID.fromString(tableId));
            if (placement == null) return;

            remove(UUID.fromString(tableId));
            placement.instance().setBlock((int) placement.x(), (int) placement.y(), (int) placement.z(), Block.AIR);
            if (player.getGameMode() != GameMode.CREATIVE) {
                player.addAndUpdateItem(new SkyBlockItem(ItemType.ENCHANTING_TABLE));
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    private static boolean remove(UUID tableId) {
        Placement placement = PLACEMENTS_BY_ID.remove(tableId);
        if (placement == null) return false;
        remove(placement);
        return true;
    }

    private static void remove(Placement placement) {
        List<LivingEntity> entities = PLACED_TABLES.remove(placement);
        if (entities == null) return;

        PLACEMENTS_BY_ID.values().removeIf(value -> value.equals(placement));
        Furniture.remove(entities);
    }

    private record Placement(Instance instance, double x, double y, double z) {
    }
}

package net.swofty.type.skyblockgeneric.block.blocks;

import lombok.NonNull;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.type.generic.HypixelConst;
import net.swofty.type.skyblockgeneric.block.SkyBlockBlock;
import net.swofty.type.skyblockgeneric.block.impl.BlockBreakable;
import net.swofty.type.skyblockgeneric.block.impl.BlockPlaceable;
import net.swofty.type.skyblockgeneric.block.impl.CustomSkyBlockBlock;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.SkullHeadComponent;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.generic.utility.BlockUtility;

public class BlockDecoration implements CustomSkyBlockBlock, BlockPlaceable, BlockBreakable {

    private final Tag<String> ITEM_TYPE_TAG = Tag.String("item_type");

    @Override
    public @NonNull Block getDisplayMaterial() {
        return Block.PLAYER_HEAD;
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
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.getAttributeHandler().getPotentialType() == null) return;
        if (!(item.hasComponent(SkullHeadComponent.class))) return;
        String texture = item.getComponent(SkullHeadComponent.class).getSkullTexture(item);

        Instance instance = event.getInstance();
        Point position = event.getBlockPosition();

        event.setCancelled(true);

        instance.setBlock(position,
                BlockUtility.applyTexture(block.toBlock(), texture).
                        withTag(ITEM_TYPE_TAG, item.getAttributeHandler().getTypeAsString())
        );
        ItemStack itemInHand = player.getItemInMainHand().withAmount(player.getItemInMainHand().amount() - 1);
        player.setItemInMainHand(itemInHand);
    }

    @Override
    public void onBreak(PlayerBlockBreakEvent event, SkyBlockBlock block) {
        String type = event.getBlock().getTag(ITEM_TYPE_TAG);

        if (type == null) return;

        event.setResultBlock(Block.AIR);

        SkyBlockItem skyBlockItem = new SkyBlockItem(ItemType.valueOf(type));

        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        player.addAndUpdateItem(skyBlockItem);
    }
}

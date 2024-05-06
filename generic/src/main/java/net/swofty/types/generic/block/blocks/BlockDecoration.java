package net.swofty.types.generic.block.blocks;

import lombok.NonNull;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.block.SkyBlockBlock;
import net.swofty.types.generic.block.impl.BlockBreakable;
import net.swofty.types.generic.block.impl.BlockInteractable;
import net.swofty.types.generic.block.impl.BlockPlaceable;
import net.swofty.types.generic.block.impl.CustomSkyBlockBlock;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.SkullHead;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.BlockUtility;

public class BlockDecoration implements CustomSkyBlockBlock, BlockPlaceable, BlockBreakable {

    private final Tag<String> ITEM_TYPE_TAG = Tag.String("item_type");

    @Override
    public @NonNull Block getDisplayMaterial() {
        return Block.PLAYER_HEAD;
    }

    @Override
    public @NonNull Boolean shouldPlace(SkyBlockPlayer player) {
        return SkyBlockConst.isIslandServer();
    }

    @Override
    public @NonNull Boolean shouldDestroy(SkyBlockPlayer player) {
        return SkyBlockConst.isIslandServer();
    }

    @Override
    public void onPlace(PlayerBlockPlaceEvent event, SkyBlockBlock block) {
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.getGenericInstance() == null) return;

        Object itemGenericInstance = item.getGenericInstance();
        if (!(itemGenericInstance instanceof SkullHead skullHead)) return;
        String texture = skullHead.getSkullTexture(player, item);

        Instance instance = event.getInstance();
        Point position = event.getBlockPosition();

        event.setCancelled(true);

        instance.setBlock(position,
                BlockUtility.applyTexture(block.toBlock(), texture).
                        withTag(ITEM_TYPE_TAG, item.getAttributeHandler().getItemType())
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

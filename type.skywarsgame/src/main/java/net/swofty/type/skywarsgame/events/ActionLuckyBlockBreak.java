package net.swofty.type.skywarsgame.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.block.Block;
import net.swofty.commons.skywars.SkywarsGameType;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skywarsgame.TypeSkywarsGameLoader;
import net.swofty.type.skywarsgame.game.SkywarsGame;
import net.swofty.type.skywarsgame.game.SkywarsGameStatus;
import net.swofty.type.skywarsgame.luckyblock.LuckyBlock;
import net.swofty.type.skywarsgame.user.SkywarsPlayer;

public class ActionLuckyBlockBreak implements HypixelEventClass {
    @HypixelEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void onBlockBreak(PlayerBlockBreakEvent event) {
        if (!(event.getPlayer() instanceof SkywarsPlayer player)) return;

        SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(player);
        if (game == null) return;

        if (game.getGameType() != SkywarsGameType.SOLO_LUCKY_BLOCK) return;

        if (game.getGameStatus() != SkywarsGameStatus.IN_PROGRESS) return;

        Block block = event.getBlock();
        LuckyBlock luckyBlockManager = game.getLuckyBlockManager();

        if (luckyBlockManager != null && luckyBlockManager.isLuckyBlockMaterial(block)) {
            Pos blockPos = new Pos(event.getBlockPosition());

            event.setCancelled(true);

            luckyBlockManager.breakLuckyBlock(player, blockPos);
        }
    }
}

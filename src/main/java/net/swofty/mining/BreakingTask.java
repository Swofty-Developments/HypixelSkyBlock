package net.swofty.mining;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.item.SkyBlockItem;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.PositionedBlock;

public class BreakingTask
{
      private int                   counter = 0;
      private final SkyBlockPlayer  player;
      private final PositionedBlock block;
      private final double          breakTime;
      private TaskSchedule          nextSchedule;

      public BreakingTask(SkyBlockPlayer player, PositionedBlock block, SkyBlockItem item) {
            this.player = player;
            this.block = block;

            double miningTime = player.getTimeToMine(item, block.block());
            if (miningTime < 1 && miningTime != -1) {
                  miningTime = 1;
            }

            this.breakTime = miningTime;
            this.nextSchedule = TaskSchedule.tick(1);
      }

      public TaskSchedule run() {
            Point newTarget = player.getTargetBlockPosition(5);

            if (newTarget == null || !block.pos().equals(Pos.fromPoint(newTarget)) || breakTime == -1) {
                  sendBlockBreak(-1);
                  return TaskSchedule.stop();
            }

            int dmg = (int) ((counter / (float) breakTime) * 10);
            if (dmg  != (counter == 0 ? -1 : (int) ((counter - 1) / (float) breakTime * 10))) {
                  sendBlockBreak(dmg);
            }
            counter++;

            if (counter >= breakTime) {
                  sendBlockBreak(-1);
                  MinecraftServer.getGlobalEventHandler().call(new PlayerBlockBreakEvent(player, block.block(), block.block(), block.pos()));
                  return TaskSchedule.stop();
            }

            return nextSchedule;
      }

      public void cancel() {
            this.nextSchedule = TaskSchedule.stop();
      }

      private void sendBlockBreak(int damage) {
            BlockBreakAnimationPacket breakAnim = new BlockBreakAnimationPacket(player.getEntityId(), block.pos(), (byte) damage);
            player.getPlayerConnection().sendPackets(breakAnim);
      }
}

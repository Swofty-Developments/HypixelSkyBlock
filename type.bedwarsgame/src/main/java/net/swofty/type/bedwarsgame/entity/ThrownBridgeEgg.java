package net.swofty.type.bedwarsgame.entity;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.block.Block;
import net.swofty.pvp.entity.projectile.ThrownEgg;
import org.jetbrains.annotations.Nullable;

public class ThrownBridgeEgg extends ThrownEgg {

	private final Block block;

	public ThrownBridgeEgg(Block block, @Nullable Entity shooter) {
		super(shooter);
		this.block = block;
	}

	@Override
	public void tick(long time) {
		super.tick(time);

		if (this.instance != null && this.position != null) {
			Vec velocity = this.getVelocity();

			double length = Math.sqrt(velocity.x() * velocity.x() + velocity.z() * velocity.z());
			if (length > 0) {
				double offsetX = -velocity.x() / length;
				double offsetZ = -velocity.z() / length;

				Point center = this.position.sub(0, 1, 0).add(offsetX, 0, offsetZ);

				for (int x = -1; x <= 0; x++) {
					for (int z = -1; z <= 0; z++) {
						Point blockPos = center.add(x, 0, z);
						this.instance.setBlock(blockPos, block);
					}
				}
			}
		}
	}
}

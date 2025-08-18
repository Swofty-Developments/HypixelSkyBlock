package net.swofty.type.bedwarsgame.shop;

import lombok.Getter;
import net.swofty.type.bedwarsgame.shop.traps.*;

import java.util.ArrayList;
import java.util.List;

public class TrapService {

	@Getter
	private final List<Trap> traps = new ArrayList<>();

	public TrapService() {
		traps.add(new BlindnessTrap());
		traps.add(new CounterOffensiveTrap());
		traps.add(new RevealTrap());
		traps.add(new MinerFatigueTrap());
	}

	public Trap getTrap(String key) {
		return traps.stream()
				.filter(trap -> trap.getKey().equalsIgnoreCase(key))
				.findFirst()
				.orElse(null);
	}

}

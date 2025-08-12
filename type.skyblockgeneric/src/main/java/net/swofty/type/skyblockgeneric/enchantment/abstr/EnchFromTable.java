package net.swofty.type.skyblockgeneric.enchantment.abstr;

import lombok.NonNull;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.Map;

public interface EnchFromTable {

    TableLevels getLevelsFromTableToApply(@NonNull HypixelPlayer player);

    int getRequiredBookshelfPower();

    record TableLevels(Map<Integer, Integer> levelsFromTableToApply) {
        public int get(int level) {
            return levelsFromTableToApply.get(level);
        }

        public int minimumLevel() {
            return levelsFromTableToApply.keySet().stream().min(Integer::compareTo).orElseThrow();
        }

        public int maximumLevel() {
            return levelsFromTableToApply.keySet().stream().max(Integer::compareTo).orElseThrow();
        }
    }
}

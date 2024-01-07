package net.swofty.commons.skyblock.enchantment.abstr;

import java.util.Map;

public interface EnchFromTable {

    TableLevels getLevelsFromTableToApply();

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

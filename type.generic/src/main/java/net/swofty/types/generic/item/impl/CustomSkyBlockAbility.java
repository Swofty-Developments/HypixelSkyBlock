package net.swofty.types.generic.item.impl;

import lombok.Getter;
import lombok.NonNull;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.StatisticDisplayReplacement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CustomSkyBlockAbility {
    List<Ability> getAbilities();

    default Ability getFromActivation(@NotNull AbilityActivation activation) {
        for (Ability ability : getAbilities()) {
            if (ability.getAbilityActivation() == activation) {
                return ability;
            }
        }
        return null;
    }

    interface Ability {
        @NonNull String getName();
        @NonNull String getDescription();
        @NonNull AbilityActivation getAbilityActivation();
        int getCooldownTicks();
        @NonNull AbilityCost getAbilityCost();
        void onUse(@NonNull SkyBlockPlayer player, @NonNull SkyBlockItem sItem);
    }

    @Getter
    enum AbilityActivation {
        RIGHT_CLICK("RIGHT CLICK"),
        LEFT_CLICK("LEFT CLICK"),
        ;

        private final @NonNull String display;

        AbilityActivation(@NotNull String display) {
            this.display = display;
        }
    }

    abstract class AbilityCost {
        public abstract boolean canUse(@NonNull SkyBlockPlayer player);
        public abstract void onUse(@NonNull SkyBlockPlayer player, @NonNull Ability ability);
        public abstract void onFail(@NonNull SkyBlockPlayer player);
        public abstract @Nullable String getLoreDisplay();
    }

    class AbilityManaCost extends AbilityCost {
        private final int cost;

        public AbilityManaCost(int cost) {
            this.cost = cost;
        }

        @Override
        public boolean canUse(@NotNull SkyBlockPlayer player) {
            return player.getMana() >= cost;
        }

        @Override
        public void onUse(@NonNull SkyBlockPlayer player, @NonNull Ability ability) {
            player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                    .display("§b-" + cost + " (§6" + ability.getName() + "§b)")
                    .ticksToLast(20 * 2)
                    .build(), StatisticDisplayReplacement.DisplayType.DEFENSE);
            player.setMana(player.getMana() - cost);
        }


        @Override
        public void onFail(@NonNull SkyBlockPlayer player) {
            player.setDisplayReplacement(StatisticDisplayReplacement.builder()
                    .display("§c§lNOT ENOUGH MANA")
                    .ticksToLast(20 * 2)
                    .build(), StatisticDisplayReplacement.DisplayType.MANA);
        }

        @Override
        public String getLoreDisplay() {
            return "§8Mana Cost: §3" + cost;
        }
    }

    class NoAbilityCost extends AbilityCost {
        @Override
        public boolean canUse(@NotNull SkyBlockPlayer player) {
            return true;
        }

        @Override
        public void onUse(@NonNull SkyBlockPlayer player, @NonNull Ability ability) {}

        @Override
        public void onFail(@NonNull SkyBlockPlayer player) {}

        @Override
        public String getLoreDisplay() {
            return null;
        }
    }
}

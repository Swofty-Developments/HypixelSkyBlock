package net.swofty.type.skyblockgeneric.item.handlers.ability;

import lombok.Getter;
import lombok.NonNull;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.block.BlockFace;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockActionBar;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class RegisteredAbility {
    private final String id;
    private final String name;
    private final String description;
    private final AbilityActivation activation;
    private final int cooldownTicks;
    private final AbilityCost cost;
    private final AbilityAction action;

    public RegisteredAbility(String id, String name, String description,
                             AbilityActivation activation, int cooldownTicks,
                             AbilityCost cost, AbilityAction action) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.activation = activation;
        this.cooldownTicks = cooldownTicks;
        this.cost = cost;
        this.action = action;
    }

    public void execute(SkyBlockPlayer player, SkyBlockItem item) {
        execute(player, item, null, null);
    }

    public void execute(SkyBlockPlayer player, SkyBlockItem item, Point targetedBlock, BlockFace blockFace) {
        if (!cost.canUse(player)) {
            cost.onFail(player);
            return;
        }

        cost.onUse(player, this);
        action.execute(player, item, targetedBlock, blockFace);
    }

    @FunctionalInterface
    public interface AbilityAction {
        void execute(SkyBlockPlayer player, SkyBlockItem item, Point targetedBlock, BlockFace blockFace);
    }

    @Getter
    public enum AbilityActivation {
        RIGHT_CLICK("RIGHT CLICK"),
        LEFT_CLICK("LEFT CLICK"),
        LEFT_CLICK_BLOCK("LEFT CLICK"),
        RIGHT_CLICK_BLOCK("RIGHT CLICK"),
        ;

        private final @NonNull String display;

        AbilityActivation(@NotNull String display) {
            this.display = display;
        }
    }

    public static abstract class AbilityCost {
        public abstract boolean canUse(@NonNull SkyBlockPlayer player);
        public abstract void onUse(@NonNull SkyBlockPlayer player, @NonNull RegisteredAbility ability);
        public abstract void onFail(@NonNull SkyBlockPlayer player);
        public abstract @Nullable String getLoreDisplay();
    }

    public static class AbilityManaCost extends AbilityCost {
        private final int cost;

        public AbilityManaCost(int cost) {
            this.cost = cost;
        }

        @Override
        public boolean canUse(@NotNull SkyBlockPlayer player) {
            return player.getMana() >= cost;
        }

        @Override
        public void onUse(@NonNull SkyBlockPlayer player, @NonNull RegisteredAbility ability) {
            SkyBlockActionBar.getFor(player).addReplacement(
                    SkyBlockActionBar.BarSection.MANA,
                    new SkyBlockActionBar.DisplayReplacement(
                            "§b-" + cost + " (§6" + ability.getName() + "§b)",
                            20,
                            2
                    )
            );
            player.setMana(player.getMana() - cost);
        }

        @Override
        public void onFail(@NonNull SkyBlockPlayer player) {
            SkyBlockActionBar.getFor(player).addReplacement(
                    SkyBlockActionBar.BarSection.MANA,
                    new SkyBlockActionBar.DisplayReplacement(
                            "§c§lNOT ENOUGH MANA",
                            20 * 2,
                            2
                    )
            );
        }

        @Override
        public String getLoreDisplay() {
            return "§8Mana Cost: §3" + cost;
        }
    }

    public static class NoAbilityCost extends AbilityCost {
        @Override
        public boolean canUse(@NotNull SkyBlockPlayer player) {
            return true;
        }

        @Override
        public void onUse(@NonNull SkyBlockPlayer player, @NonNull RegisteredAbility ability) {}

        @Override
        public void onFail(@NonNull SkyBlockPlayer player) {}

        @Override
        public String getLoreDisplay() {
            return null;
        }
    }
}
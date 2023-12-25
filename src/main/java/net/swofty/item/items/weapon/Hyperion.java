package net.swofty.item.items.weapon;

import net.swofty.item.ReforgeType;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.impl.*;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.ItemStatistic;
import net.swofty.user.statistics.ItemStatistics;
import net.swofty.utility.ItemGroups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hyperion implements CustomSkyBlockItem, CustomSkyBlockAbility, Reforgable, Enchantable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 100)
                .with(ItemStatistic.HEALTH, 20)
                .with(ItemStatistic.DEFENSE, 30)
                .build();
    }

    @Override
    public ArrayList<String> getLore(SkyBlockPlayer player, SkyBlockItem item) {
        return new ArrayList<>(Arrays.asList("This item literally comes", "out of your mum and", "says §aHELLO §7lmao."));
    }

    @Override
    public String getAbilityName() {
        return "Wither Impact";
    }

    @Override
    public String getAbilityDescription() {
        return "§7Teleports §a10 Blocks §7ahead of you. Then implode dealing §c10000 §7damage to nearby enemies. Also applies the wither shield scroll ability reducing damage taken and granting an absorption shield for §e5 §7seconds.";
    }

    @Override
    public void onAbilityUse(SkyBlockPlayer player, SkyBlockItem sItem) {
        player.sendMessage("Hey");
    }

    @Override
    public int getManaCost() {
        return 25;
    }

    @Override
    public int getAbilityCooldownTicks() {
        return 60;
    }

    @Override
    public AbilityActivation getAbilityActivation() {
        return AbilityActivation.RIGHT_CLICK;
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.SWORDS;
    }

    @Override
    public boolean showEnchantLores() {
        return true;
    }

    @Override
    public List<ItemGroups> getItemGroups() {
        return List.of(ItemGroups.SWORD);
    }
}

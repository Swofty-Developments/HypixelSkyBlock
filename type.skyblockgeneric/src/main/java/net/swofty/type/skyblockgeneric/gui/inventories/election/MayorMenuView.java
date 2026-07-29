package net.swofty.type.skyblockgeneric.gui.inventories.election;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.skyblockgeneric.elections.ElectionData;
import net.swofty.type.skyblockgeneric.elections.ElectionManager;
import net.swofty.type.skyblockgeneric.elections.SkyBlockMayor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MayorMenuView extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
        if (mayor == null) {
            return ViewConfiguration.translatable("gui_election.mayor.title_fallback", InventoryType.CHEST_4_ROW);
        }
        return ViewConfiguration.withString(
                (s, ctx) -> I18n.string("gui_election.mayor.title", ctx.player().getLocale(),
                    Component.text(mayor.getDisplayName())),
                InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 31);

        SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
        if (mayor == null) return;

        ElectionData data = ElectionManager.getElectionData();
        String mayorColor = data.getCurrentMayorColor();
        List<SkyBlockMayor.Perk> activePerks = data.getCurrentMayorPerkEnums();

        layout.slot(11, (s, c) -> {
            Locale l = c.player().getLocale();
            List<String> lore = new ArrayList<>();
            lore.add(I18n.string("gui_election.mayor.perks_label", l));
            lore.add("");
            lore.add("§8§m--------------------------");

            for (SkyBlockMayor.Perk perk : activePerks) {
                lore.add(mayorColor + perk.getDisplayName());
                for (String line : StringUtility.splitByWordAndLengthKeepLegacyColor(perk.getDescription(), 50)) {
                    lore.add("§7" + line);
                }
                lore.add("");
            }

            lore.add("§8§m--------------------------");
            lore.add("");
            lore.add(I18n.string("gui_election.mayor.perks_footer_1", l));
            lore.add(I18n.string("gui_election.mayor.perks_footer_2", l));
            lore.add(I18n.string("gui_election.mayor.perks_footer_3", l));

            return ItemStackCreator.getStackHead(
                    mayorColor + "Mayor " + mayor.getDisplayName(),
                    new PlayerSkin(mayor.getTexture(), mayor.getSignature()),
                    1,
                    lore
            );
        });

        layout.slot(15, (s, c) -> {
            Locale l = c.player().getLocale();
            ElectionData.ElectionResult lastResult = data.getLastElectionResult();
            if (lastResult == null) {
                return ItemStackCreator.getStack(
                        I18n.string("gui_election.mayor.results_title", l),
                        Material.JUKEBOX,
                        1,
                        I18n.string("gui_election.mayor.results_no_data_1", l),
                        I18n.string("gui_election.mayor.results_no_data_2", l)
                );
            }

            List<String> resultLore = new ArrayList<>();
            resultLore.add(I18n.string("gui_election.mayor.results_year", l,
                Component.text(String.valueOf(lastResult.getYear()))));
            resultLore.add("");

            List<ElectionData.CandidateResult> results = lastResult.getCandidateResults();
            for (int i = 0; i < results.size(); i++) {
                ElectionData.CandidateResult cr = results.get(i);
                String clr = ElectionData.colorForIndex(i);
                SkyBlockMayor m = null;
                try { m = SkyBlockMayor.valueOf(cr.getMayorName()); } catch (IllegalArgumentException ignored) {}
                String name = m != null ? m.getDisplayName() : cr.getMayorName();
                resultLore.add(clr + String.format("%.1f%%", cr.getPercentage())
                        + "§8 ○ " + clr + String.format("%,d", cr.getVotes())
                        + " votes§8 | " + clr + name);
            }

            resultLore.add("");
            resultLore.add(I18n.string("gui_election.mayor.results_footer_1", l));
            resultLore.add(I18n.string("gui_election.mayor.results_footer_2", l,
                Component.text(mayor.getDisplayName())));
            resultLore.add(I18n.string("gui_election.mayor.results_footer_3", l));

            return ItemStackCreator.getStack(I18n.string("gui_election.mayor.results_title", l),
                    Material.JUKEBOX, 1, resultLore.toArray(new String[0]));
        });
    }
}

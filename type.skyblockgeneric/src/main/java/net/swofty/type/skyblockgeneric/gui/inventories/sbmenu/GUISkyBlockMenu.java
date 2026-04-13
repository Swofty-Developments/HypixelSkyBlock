package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.generic.gui.inventory.TranslatableItemStackCreator;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.calendar.CalendarEvent;
import net.swofty.type.skyblockgeneric.calendar.SkyBlockCalendar;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.bags.GUIYourBags;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.calendar.GUICalendar;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.collection.GUICollections;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.fasttravel.GUIFastTravel;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.GUISkyBlockLevels;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles.GUIProfileManagement;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.questlog.GUIMissionLog;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIRecipeBook;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.skills.GUISkills;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.storage.GUIStorage;
import net.swofty.type.skyblockgeneric.item.crafting.SkyBlockRecipe;
import net.swofty.type.skyblockgeneric.levels.SkyBlockLevelRequirement;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.PlayerStatistics;
import org.jspecify.annotations.NonNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GUISkyBlockMenu extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.main.title", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);

        layout.slot(13, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            Locale l = player.getLocale();
            PlayerStatistics statistics = player.getStatistics();
            StringBuilder statsDisplay = new StringBuilder();
            List<String> statNames = new ArrayList<>(List.of("Health", "Defense", "Speed", "Strength", "Intelligence",
                    "Crit Chance", "Crit Damage", "Swing Range"
            ));
            statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                if (!value.equals(statistic.getBaseAdditiveValue()) || statNames.contains(statistic.getDisplayName())) {
                    if (statsDisplay.length() > 0) statsDisplay.append("\n");
                    statsDisplay.append(" ").append(statistic.getFullDisplayName()).append(" §f")
                            .append(StringUtility.decimalify(value, 2)).append(statistic.getSuffix());
                }
            });


            return TranslatableItemStackCreator.getStackHead("gui_sbmenu.main.your_profile",
                    player.getPlayerSkin(), 1,
                I18n.lore("gui_sbmenu.main.your_profile.lore", l,
                    Component.text(statsDisplay.toString()))
            );
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            player.openView(new GUISkyBlockProfile());
        });

        layout.slot(22, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockLevelRequirement levelRequirement = player.getSkyBlockExperience().getLevel();
            SkyBlockLevelRequirement nextLevel = levelRequirement.getNextLevel();

            return TranslatableItemStackCreator.getStackHead("gui_sbmenu.main.skyblock_leveling",
                    "3255327dd8e90afad681a19231665bea2bd06065a09d77ac1408837f9e0b242", 1,
                "gui_sbmenu.main.skyblock_leveling.lore",
                Component.text(levelRequirement.getColor() + String.valueOf(levelRequirement)),
                Component.text(nextLevel == null ? "§cMAX" : String.valueOf(nextLevel)),
                Component.text(player.getSkyBlockExperience().getNextLevelDisplay())
            );
        }, (click, c) -> c.push(new GUISkyBlockLevels()));

        layout.slot(29, (s, c) -> TranslatableItemStackCreator.getStackHead("gui_sbmenu.main.your_bags",
                "961a918c0c49ba8d053e522cb91abc74689367b4d8aa06bfc1ba9154730985ff", 1,
                "gui_sbmenu.main.your_bags.lore"
        ), (click, c) -> {
            c.push(new GUIYourBags());
        });

        layout.slot(30, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            String selectedPet = player.getPetData().getEnabledPet() == null ? "§cNone" : player.getPetData().getEnabledPet().getDisplayName();
            return TranslatableItemStackCreator.getStack("gui_sbmenu.main.pets", Material.BONE, 1,
                "gui_sbmenu.main.pets.lore", Component.text(selectedPet)
            );
        }, (click, c) -> c.push(new GUIPets(), GUIPets.createInitialState((SkyBlockPlayer) c.player())));

        layout.slot(21, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            List<String> missionDisplay = new ArrayList<>();
            SkyBlockRecipe.getMissionDisplay(missionDisplay, player.getUuid());
            StringBuilder missionDisplayStr = new StringBuilder();
            for (String line : missionDisplay) {
                if (missionDisplayStr.length() > 0) missionDisplayStr.append("\n");
                missionDisplayStr.append(line);
            }

            return TranslatableItemStackCreator.getStack("gui_sbmenu.main.recipe_book", Material.BOOK, 1,
                "gui_sbmenu.main.recipe_book.lore", Component.text(missionDisplayStr.toString()));
        }, (click, c) -> {
            c.push(new GUIRecipeBook());
        });

        layout.slot(25, (s, c) -> TranslatableItemStackCreator.getStack("gui_sbmenu.main.storage", Material.CHEST, 1,
                "gui_sbmenu.main.storage.lore"
        ), (click, c) -> c.push(new GUIStorage()));

        layout.slot(23, (s, c) -> TranslatableItemStackCreator.getStack("gui_sbmenu.main.quests", Material.WRITABLE_BOOK, 1,
                "gui_sbmenu.main.quests.lore"
        ), (click, c) -> c.push(new GUIMissionLog()));

        layout.autoUpdating(24, (s, c) -> TranslatableItemStackCreator.getStack("gui_sbmenu.main.calendar", Material.CLOCK, 1, getCalendarLore(ctx)),
                (click, c) -> c.push(new GUICalendar()), Duration.ofSeconds(1));

        layout.slot(19, (s, c) -> TranslatableItemStackCreator.getStack("gui_sbmenu.main.skills", Material.DIAMOND_SWORD, 1,
                "gui_sbmenu.main.skills.lore"
        ), (click, c) -> c.push(new GUISkills()));

        layout.slot(20, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            List<String> collectionDisplay = new ArrayList<>();
            player.getCollection().getDisplay(collectionDisplay);
            StringBuilder collectionDisplayStr = new StringBuilder();
            for (String line : collectionDisplay) {
                if (collectionDisplayStr.length() > 0) collectionDisplayStr.append("\n");
                collectionDisplayStr.append(line);
            }

            return TranslatableItemStackCreator.getStack("gui_sbmenu.main.collections", Material.PAINTING, 1,
                "gui_sbmenu.main.collections.lore", Component.text(collectionDisplayStr.toString()));
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            player.openView(new GUICollections());
        });

        layout.slot(31, (s, c) -> TranslatableItemStackCreator.getStack("gui_sbmenu.main.crafting_table", Material.CRAFTING_TABLE, 1,
                "gui_sbmenu.main.crafting_table.lore"
        ), (click, c) -> c.push(new GUICrafting()));

        layout.slot(47, (s, c) -> TranslatableItemStackCreator.getStackHead("gui_sbmenu.main.fast_travel",
                "f151cffdaf303673531a7651b36637cad912ba485643158e548d59b2ead5011", 1,
                "gui_sbmenu.main.fast_travel.lore"
        ), (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            if (click.click() instanceof Click.Right) {
                player.closeInventory();
                player.sendTo(ServerType.SKYBLOCK_ISLAND);
                return;
            }
            player.openView(new GUIFastTravel());
        });

        layout.slot(48, (s, c) -> {
            HypixelPlayer player = c.player();
            return TranslatableItemStackCreator.getStack("gui_sbmenu.main.profile_management", Material.NAME_TAG, 1,
                "gui_sbmenu.main.profile_management.lore",
                Component.text(String.valueOf(((SkyBlockPlayer) player).getProfiles().getProfiles().size()))
            );
        }, (click, c) -> c.push(new GUIProfileManagement()));
    }

    private static @NonNull List<String> getCalendarLore(ViewContext ctx) {
        Locale l = ctx.player().getLocale();
        List<CalendarEvent> currentEvents = SkyBlockCalendar.getCurrentEvents();
        boolean multipleEvents = currentEvents.size() > 1;

        String date = StringUtility.ntify(SkyBlockCalendar.getDay()) + " " + SkyBlockCalendar.getMonthName() + " " + SkyBlockCalendar.getYear();
        List<String> lore = new ArrayList<>(I18n.lore("gui_sbmenu.main.calendar.lore_header", l, Component.text(date)));
        lore.add("");

        if (multipleEvents) {
            lore.add(I18n.string("gui_sbmenu.main.calendar.current_events", l));
            for (CalendarEvent event : currentEvents) {
                lore.add(event.getDisplayName(SkyBlockCalendar.getYear()));
            }
        } else if (currentEvents.size() == 1) {
            CalendarEvent currentEvent = currentEvents.getFirst();
            lore.add(I18n.string("gui_sbmenu.main.calendar.current_event", l, Component.text(currentEvent.getDisplayName(SkyBlockCalendar.getYear()))));
            long ticksRemaining = getTicksRemaining(currentEvent);
            lore.add(I18n.string("gui_sbmenu.main.calendar.event_ends_in", l, Component.text(StringUtility.formatTimeLeft(ticksRemaining * 50L))));
        } else {
            lore.add(I18n.string("gui_sbmenu.main.calendar.no_current_events", l));
        }

        lore.add(" ");

        Map<SkyBlockCalendar.EventInfo, CalendarEvent> upcomingEvents;
        if (ctx.player().getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_VISITED_DARK_AUCTION)) {
            upcomingEvents = SkyBlockCalendar.getEventsWithDurationUntil(1);
        } else {
            upcomingEvents = SkyBlockCalendar.getEventsWithDurationUntilSkipSpecific(1, Collections.singletonList(CalendarEvent.DARK_AUCTION));
        }

        if (!upcomingEvents.isEmpty()) {
            Map.Entry<SkyBlockCalendar.EventInfo, CalendarEvent> entry = upcomingEvents.entrySet().iterator().next();
            SkyBlockCalendar.EventInfo info = entry.getKey();
            CalendarEvent event = entry.getValue();

            lore.add(I18n.string("gui_sbmenu.main.calendar.next_event", l, Component.text(event.getDisplayName(info.year()))));
            lore.add(I18n.string("gui_sbmenu.main.calendar.next_event_starting", l, Component.text(StringUtility.formatTimeLeft(info.timeUntilBegin() * 50L))));
        } else {
            lore.add(I18n.string("gui_sbmenu.main.calendar.no_upcoming_events", l));
        }

        lore.addAll(I18n.lore("gui_sbmenu.main.calendar.lore_footer", l));
        return lore;
    }

    private static long getTicksRemaining(CalendarEvent currentEvent) {
        long currentElapsedInYear = SkyBlockCalendar.getElapsed() % SkyBlockCalendar.YEAR;
        long eventEndTime = 0;
        for (Long eventStartTime : currentEvent.times()) {
            if (currentElapsedInYear >= eventStartTime && currentElapsedInYear < eventStartTime + currentEvent.duration().toMillis() / 50) {
                eventEndTime = eventStartTime + currentEvent.duration().toMillis() / 50;
                break;
            }
        }
        return eventEndTime - currentElapsedInYear;
    }

}

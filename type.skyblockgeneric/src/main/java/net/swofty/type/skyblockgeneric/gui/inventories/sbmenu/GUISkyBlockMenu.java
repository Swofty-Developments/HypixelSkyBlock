package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.Material;
import net.swofty.commons.ServerType;
import net.swofty.commons.StringUtility;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
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
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIRecipe;
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
import java.util.List;
import java.util.Map;

public class GUISkyBlockMenu extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("SkyBlock Menu", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);

        layout.slot(13, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            PlayerStatistics statistics = player.getStatistics();
            List<String> lore = new ArrayList<>(List.of("§7View your equipment, stats, and more!", "§e "));
            List<String> stats = new ArrayList<>(List.of("Health", "Defense", "Speed", "Strength", "Intelligence",
                    "Crit Chance", "Crit Damage", "Swing Range"
            ));
            statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                if (!value.equals(statistic.getBaseAdditiveValue()) || stats.contains(statistic.getDisplayName())) {
                    lore.add(" " + statistic.getFullDisplayName() + " §f" +
                            StringUtility.decimalify(value, 2) + statistic.getSuffix());
                }
            });

            lore.add("§e ");
            lore.add("§eClick to view!");

            return ItemStackCreator.getStackHead("§aYour SkyBlock Profile",
                    player.getPlayerSkin(), 1,
                    lore
            );
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            player.openView(new GUISkyBlockProfile());
        });

        layout.slot(22, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            SkyBlockLevelRequirement levelRequirement = player.getSkyBlockExperience().getLevel();
            SkyBlockLevelRequirement nextLevel = levelRequirement.getNextLevel();

            return ItemStackCreator.getStackHead("§aSkyBlock Leveling", "3255327dd8e90afad681a19231665bea2bd06065a09d77ac1408837f9e0b242", 1,
                    "§7Your SkyBlock Level: §8[" + levelRequirement.getColor() + levelRequirement + "§8]",
                    " ",
                    "§7Determine how far you've",
                    "§7progressed in SkyBlock and earn",
                    "§7rewards from completing unique",
                    "§7tasks.",
                    " ",
                    "§7Progress to Level " + (nextLevel == null ? "§cMAX" : nextLevel),
                    player.getSkyBlockExperience().getNextLevelDisplay(),
                    " ",
                    "§eClick to view!"
            );
        }, (click, c) -> c.push(new GUISkyBlockLevels()));

        layout.slot(29, (s, c) -> ItemStackCreator.getStackHead("§aYour Bags", "961a918c0c49ba8d053e522cb91abc74689367b4d8aa06bfc1ba9154730985ff", 1,
                "§7Different bags allow you to store",
                "§7many different items inside!",
                " ",
                "§eClick to view!"
        ), (click, c) -> {
            c.push(new GUIYourBags());
        });

        layout.slot(30, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            return ItemStackCreator.getStack("§aPets", Material.BONE, 1,
                    "§7View and manage all of your",
                    "§7Pets.",
                    " ",
                    "§7Level up your pets faster by",
                    "§7gaining XP in their favourite",
                    "§7skill!",
                    " ",
                    "§7Selected pet: " + (player.getPetData().getEnabledPet() == null ? "§cNone" : player.getPetData().getEnabledPet().getDisplayName()),
                    " ",
                    "§eClick to view!"
            );
        }, (click, c) -> c.push(new GUIPets(), GUIPets.createInitialState((SkyBlockPlayer) c.player())));

        layout.slot(21, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            List<String> lore = new ArrayList<>(List.of(
                    "§7Through your adventure, you will",
                    "§7unlock recipes for all kinds of",
                    "§7special items! You can view how to",
                    "§7craft these items here.",
                    " "
            ));

            SkyBlockRecipe.getMissionDisplay(lore, player.getUuid());

            lore.add(" ");
            lore.add("§eClick to view!");
            return ItemStackCreator.getStack("§aRecipe Book", Material.BOOK, 1, lore);
        }, (click, c) -> {
            c.push(new GUIRecipeBook());
        });

        layout.slot(25, (s, c) -> ItemStackCreator.getStack("§aStorage", Material.CHEST, 1,
                "§7Store global items that you",
                "§7want to access at any time",
                "§7from anywhere here.",
                " ",
                "§eClick to view!"
        ), (click, c) -> c.push(new GUIStorage()));

        layout.slot(23, (s, c) -> ItemStackCreator.getStack("§aQuests & Chapters", Material.WRITABLE_BOOK, 1,
                "§7Each island has its own series of",
                "§bChapters §7for you to complete!",
                " ",
                "§7Complete tasks within a Chapter to",
                "§7earn small §6rewards§7, or complete",
                "§7entire Chapters to earn big ones!",
                " ",
                "§7Some islands also have §aQuests §7for",
                "§7you to complete! Some items can only",
                "§7be obtained through Quests.",
                " ",
                "§eClick to view!"
        ), (click, c) -> c.push(new GUIMissionLog()));

        layout.autoUpdating(24, (s, c) -> ItemStackCreator.getStack("§aCalendar and Events", Material.CLOCK, 1, getCalendarLore()),
                (click, c) -> c.push(new GUICalendar()), Duration.ofSeconds(1));

        layout.slot(19, (s, c) -> ItemStackCreator.getStack("§aYour Skills", Material.DIAMOND_SWORD, 1,
                "§7View your Skill progression and",
                "§7rewards.",
                " ",
                "§eClick to view!"
        ), (click, c) -> c.push(new GUISkills()));

        layout.slot(20, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            List<String> lore = new ArrayList<>(List.of(
                    "§7View all of the items available in",
                    "§7SkyBlock. Collect more of an item to",
                    "§7unlock rewards on your way to",
                    "§7becoming a master of SkyBlock!",
                    " "
            ));

            player.getCollection().getDisplay(lore);

            lore.add(" ");
            lore.add("§eClick to view!");
            return ItemStackCreator.getStack("§aCollections", Material.PAINTING, 1, lore.toArray(new String[0]));
        }, (click, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            player.openView(new GUICollections());
        });

        layout.slot(31, (s, c) -> ItemStackCreator.getStack("§aCrafting Table", Material.CRAFTING_TABLE, 1,
                "§7Opens the crafting grid.",
                " ",
                "§eClick to open!"
        ), (click, c) -> c.push(new GUICrafting()));

        layout.slot(47, (s, c) -> ItemStackCreator.getStackHead("§bFast Travel", "f151cffdaf303673531a7651b36637cad912ba485643158e548d59b2ead5011", 1,
                "§7Teleport to islands you've already",
                "§7visited.",
                " ",
                "§8Right-click to warp home!",
                "§eClick to pick location!"
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
            return ItemStackCreator.getStack("§aProfile Management", Material.NAME_TAG, 1,
                    "§7You can have multiple SkyBlock",
                    "§7profiles at the same time.",
                    " ",
                    "§7Each profile has its own island,",
                    "§7inventory, quest log...",
                    " ",
                    "§7Profiles: §e" + ((SkyBlockPlayer) player).getProfiles().getProfiles().size() + "§6/§e4",
                    " ",
                    "§eClick to manage!"
            );
        }, (click, c) -> c.push(new GUIProfileManagement()));
    }

    private static @NonNull List<String> getCalendarLore() {
        List<CalendarEvent> currentEvents = SkyBlockCalendar.getCurrentEvents();
        boolean multipleEvents = currentEvents.size() > 1;

        List<String> lore = new ArrayList<>(List.of("§7View the SkyBlock Calendar, upcoming",
                "§7events, and event rewards!",
                " ",
                "§7Date: §a" + StringUtility.ntify(SkyBlockCalendar.getDay()) + " " + SkyBlockCalendar.getMonthName() + " " + SkyBlockCalendar.getYear(),
                ""));
        if(multipleEvents) {
            lore.add("§7Current events: ");
            for (CalendarEvent event : currentEvents) {
                lore.add(event.getDisplayName(SkyBlockCalendar.getYear()));
            }
        } else if (currentEvents.size() == 1) {
            CalendarEvent currentEvent = currentEvents.getFirst();
            lore.add("§7Current event: " + currentEvent.getDisplayName(SkyBlockCalendar.getYear()));
            long ticksRemaining = getTicksRemaining(currentEvent);
            lore.add("§7Ends in: §e" + StringUtility.formatTimeLeft(ticksRemaining * 50L));
        } else {
            lore.add("§7No current events.");
        }

        lore.add(" ");

        Map<SkyBlockCalendar.EventInfo, CalendarEvent> upcomingEvents = SkyBlockCalendar.getEventsWithDurationUntil(1);
        if (!upcomingEvents.isEmpty()) {
            Map.Entry<SkyBlockCalendar.EventInfo, CalendarEvent> entry = upcomingEvents.entrySet().iterator().next();
            SkyBlockCalendar.EventInfo info = entry.getKey();
            CalendarEvent event = entry.getValue();

            lore.add("§7Next event: " + event.getDisplayName(info.year()));
            lore.add("§7Starting in: §e" + StringUtility.formatTimeLeft(info.timeUntilBegin() * 50L));
        } else {
            lore.add("§7No upcoming events.");
        }

        lore.addAll(
                List.of(
                        " ",
                        "§8Also accessible via /calendar",
                        " ",
                        "§eClick to view!"
                )
        );
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

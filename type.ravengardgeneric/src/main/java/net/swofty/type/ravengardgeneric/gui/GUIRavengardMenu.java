package net.swofty.type.ravengardgeneric.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.ravengardgeneric.classes.RavengardAbility;
import net.swofty.type.ravengardgeneric.classes.RavengardClass;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

import java.util.List;

public class GUIRavengardMenu extends RavengardView {
    // origins taken from the captured menu
    private static final int SLOT_FIGHT = 3;
    private static final int SLOT_STATUE = 18;
    private static final int SLOT_LOCKBOX = 20;
    private static final int SLOT_BAG = 22;
    private static final int SLOT_TROPHY = 24;
    private static final int SLOT_ABILITY_ONE = 26;
    private static final int SLOT_QUILL = 38;
    private static final int SLOT_BOOK = 39;
    private static final int SLOT_CANDLE = 42;
    private static final int SLOT_ABILITY_TWO = 44;

    @Override
    protected String title() {
        return "Main Menu";
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        RavengardClass playerClass = ctx.player() instanceof RavengardPlayer player
                ? player.getRavengardClass()
                : null;

        place(layout, SLOT_FIGHT, RavengardItems.button(RavengardButton.TEXT_FIGHT)
                .label("Join the Fight!")
                .lore("§7Jump into the action and fight",
                        "§7against other players and monsters!")
                .blankLine()
                .lore("§eClick to join!"));

        RavengardButton statue = playerClass == null ? null : RavengardButton.statueFor(playerClass);
        if (statue != null) {
            RavengardItems.Builder profile = RavengardItems.button(statue)
                    .label("Profiles - " + playerClass.getDisplayName())
                    .blankLine();

            String[] profileLore = playerClass.profileLore();
            if (profileLore != null) {
                profile.lore(profileLore).blankLine();
            }

            place(layout, SLOT_STATUE, profile.lore("§eClick to change profile!"));
        }

        place(layout, SLOT_LOCKBOX, RavengardItems.button(RavengardButton.CHEST)
                .label("Lockbox")
                .lore("§7Safely store your items here!")
                .blankLine()
                .lore("§eClick to open!"));

        // Unreleased features. Hypixel renders their whole tooltip in the Illager rune font so the
        // placeholder text (alphabet runs, lorem ipsum) is unreadable in game; kept verbatim.
        place(layout, SLOT_BAG, RavengardItems.button(RavengardButton.BAG)
                .font(RavengardFont.ILLAGERALT)
                .label("Abcdefgh")
                .lore("§7Lorem ipsum dolor sit amet!")
                .blankLine()
                .lore("§cComing Soon!"));

        place(layout, SLOT_TROPHY, RavengardItems.button(RavengardButton.TROPHY)
                .font(RavengardFont.ILLAGERALT)
                .label("Ijklmno")
                .lore("§7Consectetur adipiscing elit.")
                .blankLine()
                .lore("§cComing Soon!"));

        place(layout, SLOT_QUILL, RavengardItems.button(RavengardButton.QUILL)
                .font(RavengardFont.ILLAGERALT)
                .label("Pqrstuv")
                .lore("§7Sed do eiusmod tempor incididunt!")
                .blankLine()
                .lore("§cComing Soon!"));

        place(layout, SLOT_BOOK, RavengardItems.button(RavengardButton.BOOK)
                .font(RavengardFont.ILLAGERALT)
                .label("Pqrstu")
                .lore("§7Sed do eiusmod tempor incididunt!")
                .blankLine()
                .lore("§cComing Soon!"));

        place(layout, SLOT_CANDLE, RavengardItems.button(RavengardButton.CANDLE)
                .font(RavengardFont.ILLAGERALT)
                .label("Vwxyz")
                .lore("§7Ut labore et dolore magna aliqua!")
                .blankLine()
                .lore("§cComing Soon!"));

        if (playerClass == null) {
            return;
        }

        List<RavengardAbility> abilities = playerClass.getAbilities();
        int[] slots = {SLOT_ABILITY_ONE, SLOT_ABILITY_TWO};
        for (int index = 0; index < slots.length && index < abilities.size(); index++) {
            RavengardAbility ability = abilities.get(index);
            place(layout, slots[index], RavengardItems.button(ability)
                    .label("Ability " + (index + 1) + " - " + ability.getDisplayName())
                    .lore(ability.getHighlightedDescription())
                    .blankLine()
                    .lore("§eClick to change!"));
        }
    }

    @Override
    public void onOpen(DefaultState state, ViewContext ctx) {
        if (((RavengardPlayer) ctx.player()).isTutorial()) {
            ctx.player().sendMessage(Component.text("You must select a class to open this menu!", NamedTextColor.RED));
            ctx.backOrClose();
            return;
        }
        super.onOpen(state, ctx);
    }
}

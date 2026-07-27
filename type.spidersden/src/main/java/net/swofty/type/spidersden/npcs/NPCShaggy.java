package net.swofty.type.spidersden.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ChatColor;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.generic.event.custom.NPCInteractEvent;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.stream.Stream;

public class NPCShaggy extends HypixelNPC {
    public NPCShaggy() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{
                        ChatColor.AQUA + "Shaggy",
                        ChatColor.YELLOW + "" + ChatColor.BOLD + "CLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "HtvR7Hudc9cMplF4TVFyGq8/lAarZEtOLRylnuRu3Gc54dLPvb6x7uOXlhYbSpD3he3kpMR8PEdsvyxYjwrIXdRsgHTjMVPFAPK+zcA3api5UlLSS0VNzMDZDRsbZiFx99OSvmwKPQHSGwSD4gLl8afy/JLTaeu+CCQ6SgnFYcDbegroTuDbEhKJYzw9p3MDksJ8e6iXTlbdEWEzMXvQVVvh3m9fZr1K9AZKHKcKP+I1JQjiQBYGjdUvhXQErCmMoBIxqfRgW7xw3RGAeE6c6w/9kC04PgVG3Vwhcyu4FRWTndRuIp5XkxYdupTwA3m7wgiW638hc9m13kscfXpe8cVjf2j2lrxZGgw5Qo8hVd4nDWLGKimD7Ul1jMX0AzXkJccaVxs6Jg2gdnXyQr2xNviCU45V0DA7+L88B/haTvfWEKipRzgLaXokLk5stUqFtW7PWQa87n6Zlvju9fDYvbtHL24kvB0VMc7gagD+7L46i4heDxDs9hbDmzA9gdE/nZgN45rcnU6Ab6yNs7GVnBICwuHmZpIc7Laki1XbQd+h0GXQYCbfAOh0bDmlY3fHdqxs7SEovwC8l0+vDJHeLHZN8N2+CwxQC72EhVG5j8yzBBqpUfRKBMCJjaG8ud/S1qd3KWBa7Ba0mMEBCvVu/NhyBblo45FodGONMwTtvlo=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYxMzA5NTQ2OTc1NSwKICAicHJvZmlsZUlkIiA6ICJhYTZhNDA5NjU4YTk0MDIwYmU3OGQwN2JkMzVlNTg5MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiejE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI1OGY4OGRjYmQyYWYxMTBjNzdjZmZhYTAwZWFiN2E0OTljMDAxMzNlNjE1NzU1OTlkOWUwNmU2MWI4YTI0YTYiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-278.5, 121, -182.5, 156, -1);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {

    }

    @Override
    public DialogueSet[] dialogues(HypixelPlayer player) {
        return Stream.of(
                DialogueSet.builder()
                        .key("first-interaction").lines(new String[]{
                                "I used to think this world was peaceful, but there are some beings in this world that are stronger than I could ever hope to handle...",
                                "This spider, " + ChatColor.DARK_RED + "Arachne" + ChatColor.WHITE + " - it's way out of my league...",
                                "I can barely defeat her " + ChatColor.DARK_PURPLE + "Keepers" + ChatColor.WHITE + "..."
                        }).build(),
                DialogueSet.builder()
                        .key("holding-arachne-fragment").lines(new String[]{
                                "Wow, you actually challenged that old witch Arachne, you have some potential kid...",
                                "Have you heard about the rare relics on this island?",
                                "Legend says they are infused with an evil deities strength, the truth is it is her power.",
                                "If you help me find those Relics I will tell you everything I know about the creature."
                        }).build(),
                DialogueSet.builder()
                        .key("bringing-required-materials").lines(new String[]{
                                "Great, this is everything I need! With this I can make the crystal.",
                                "* Hammer and drilling sounds *",
                                "* Glass breaking *",
                                "Here ya go, if you put this on the alter, it will drag that retched creature to this world.",
                                "But be warned, she is strong, and I have it set up to drag her back if she is not defeated in 10 minutes, for the safety of this whole world.",
                                "I wish you good luck!"
                        }).build()
        ).toArray(DialogueSet[]::new);
    }
}

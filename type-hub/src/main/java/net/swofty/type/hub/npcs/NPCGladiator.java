package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.entity.npc.NPCDialogue;
import net.swofty.types.generic.entity.npc.NPCParameters;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.stream.Stream;

public class NPCGladiator extends NPCDialogue {

    public NPCGladiator() {
        super(new NPCParameters() {
            @Override
            public String[] holograms(SkyBlockPlayer player) {
                return new String[]{"§bGladiator", "§e§lCLICK"};
            }

            @Override
            public String signature(SkyBlockPlayer player) {
                return "DiRWUdP+lEw22Xt2U2EeW7Pqw9+3SHDm501yNMHaBvvyCGr+iDIu7wXhDRlGRtHtlRyEE+kddgm+L7QrseN4OXywN/M9NUayMFlVsjgym7vwEyNKhWGxehC+HCM0mcA4BR+DXwV8/34MwCWnpYprABrGXl1cQ4n/zfHdkvO/auM3r95Mzjp5hYcupr5kJUUoTJkGKSx5Gqx3UzcOvR3GG185v8jTLsUaRMt4b5FegWbN17+BjApdi9PxcJwQ+j9EGXRYflA061C629h4yFAVVSyASYYP2zSe6otrNaeKCSFAg0OCwVWDzUDLRS8xCFYRhqQh7igrRYu9UYUulcXDxvbIrhNtdL8N5dc98LJ+nPO2WEXFT7iZc+MCyIfARSq18vTy1C2nobVVe1BgXqIZLa05ARiQV4kNgUbW4kHWaUJ9Q5WIGBtw8Xv/7vYRMxvYF0dYkzivbbtBGubnWtMMIKvRaWJxL9kLzpzP7USGVNgaKd7e273yALRvthAlxvI3+HZzYAUAzzHzzE+50Gd69g0ReioaRYIGeRwwLtJ1eolIGUTK/3d0g5i/Pf4cWaKZ0eZMt297LAx2AGKWOBNCYrfru2BmkdWMKkXQYOAc0FjwHBn1N4P6JC/X607jDiO2AtCb/AEx2VKOBniX5ifPJOkQ8xklrI4NXFYdUNMIVjA=";
            }

            @Override
            public String texture(SkyBlockPlayer player) {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYyMzg0OTcxNjk4MywKICAicHJvZmlsZUlkIiA6ICI0OTQwYzRiZmMyZGE0MjZkYWI2MTA3OWQ1ZTMwZDZjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJzdXNwaWNpb3VzIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2Y3MWNhYWM2YmVjNjQwNjYwNDJjZjViZmJkOWRmZjJkZGRlZWFiMWM2OTU2ZjM1MWUxOGQ4MjcxNGI1NmIxMmIiCiAgICB9CiAgfQp9";
            }

            @Override
            public Pos position(SkyBlockPlayer player) {
                return new Pos(123.5, 79, 165.5, 135, 25);
            }

            @Override
            public boolean looking() {
                return true;
            }
        });
    }

    @Override
    public void onClick(PlayerClickNPCEvent e) {
        if (isInDialogue(e.player())) return;
        setDialogue(e.player(), "hello");
    }


    @Override
    public NPCDialogue.DialogueSet[] getDialogueSets(SkyBlockPlayer player) {
        return Stream.of(
                NPCDialogue.DialogueSet.builder()
                        .key("hello").lines(new String[]{
                                "Welcome to the §bColosseum§f!",
                                "Oh...wait. Nevermind.",
                                "Here you can join me in drinking away your sorrows."
                        }).build()
        ).toArray(NPCDialogue.DialogueSet[]::new);
    }
}

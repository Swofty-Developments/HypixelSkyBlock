package net.swofty.type.skyblockgeneric.museum;

import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public abstract class MuseumDisplay {
    public abstract MuseumDisplayEntityInformation display(SkyBlockPlayer player,
                                                           MuseumDisplays display,
                                                           boolean empty,
                                                           int position
    );
}
package net.swofty.types.generic.museum;

import net.swofty.types.generic.user.SkyBlockPlayer;

public abstract class MuseumDisplay {
    public abstract MuseumDisplayEntityInformation display(SkyBlockPlayer player,
                                                           MuseumDisplays display,
                                                           boolean empty,
                                                           int position);
}
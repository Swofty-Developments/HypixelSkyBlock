package net.swofty.type.skyblockgeneric.museum;

import net.swofty.type.generic.user.HypixelPlayer;

public abstract class MuseumDisplay {
    public abstract MuseumDisplayEntityInformation display(HypixelPlayer player,
                                                           MuseumDisplays display,
                                                           boolean empty,
                                                           int position);
}
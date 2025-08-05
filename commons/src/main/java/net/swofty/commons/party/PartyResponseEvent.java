package net.swofty.commons.party;

import net.swofty.commons.party.PartyEvent;

public abstract class PartyResponseEvent extends PartyEvent {
    public PartyResponseEvent(Party party) {
        super(party);
    }
}
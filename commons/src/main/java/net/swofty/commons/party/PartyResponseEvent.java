package net.swofty.commons.party;

public abstract class PartyResponseEvent extends PartyEvent {
    public PartyResponseEvent(Party party) {
        super(party);
    }
}
package net.swofty.type.generic.gui.v2.event;

public sealed interface GuiEvent
    permits GUIClickEvent, GUICloseEvent, GUIOpenEvent, GUIRefreshEvent {}

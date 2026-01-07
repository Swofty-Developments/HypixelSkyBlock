package net.swofty.type.generic.gui.v2.event;

public sealed interface GUIEvent
    permits GUIClickEvent, GUICloseEvent, GUIOpenEvent, GUIRefreshEvent {}

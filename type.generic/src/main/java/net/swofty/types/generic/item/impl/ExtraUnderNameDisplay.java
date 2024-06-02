package net.swofty.types.generic.item.impl;

import java.util.List;

public interface ExtraUnderNameDisplay extends ExtraUnderNameDisplays{
    String getExtraUnderNameDisplay();

    @Override
    default List<String> getExtraUnderNameDisplays() {
        return List.of(getExtraUnderNameDisplay());
    }
}

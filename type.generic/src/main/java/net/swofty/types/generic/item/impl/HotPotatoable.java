package net.swofty.types.generic.item.impl;

import lombok.Getter;
import net.swofty.commons.item.PotatoType;
import net.swofty.commons.statistics.ItemStatistic;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface HotPotatoable {

    @Nullable
    PotatoType getHotPotatoType();
}

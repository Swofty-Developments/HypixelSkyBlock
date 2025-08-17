package net.swofty.type.skyblockgeneric.museum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.entity.LivingEntity;
import net.swofty.type.generic.entity.hologram.PlayerHolograms;

import java.util.List;

@Getter
@AllArgsConstructor
public class MuseumDisplayEntityInformation {
    private final List<LivingEntity> entities;
    private final PlayerHolograms.ExternalPlayerHologram hologram;
}
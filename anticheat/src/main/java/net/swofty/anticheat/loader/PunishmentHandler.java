package net.swofty.anticheat.loader;

import net.swofty.anticheat.flag.FlagType;

import java.util.UUID;

public abstract class PunishmentHandler {
    public abstract void onFlag(UUID uuid, FlagType flagType);
}

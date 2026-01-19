package net.swofty.commons.punishment;

import lombok.Getter;
import lombok.NonNull;
import net.swofty.commons.punishment.template.BanType;
import net.swofty.commons.punishment.template.MuteType;
import net.swofty.commons.punishment.template.UnpunishReason;
import org.jetbrains.annotations.Nullable;

@Getter
public class PunishmentReason {
    @Nullable
    private BanType banType;
    @Nullable
    private MuteType muteType;

    public PunishmentReason(@NonNull BanType banType) {
        this.banType = banType;
    }

    public PunishmentReason(@NonNull MuteType muteType) {
        this.muteType = muteType;
    }

    public String getReasonString() {
        if (banType != null) {
            return banType.getReason();
        } else if (muteType != null) {
            return muteType.getReason();
        } else {
            return "Could not resolve reason.";
        }
    }
}

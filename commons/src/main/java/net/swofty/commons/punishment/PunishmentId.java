package net.swofty.commons.punishment;

import java.security.SecureRandom;

public record PunishmentId(String id) {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static PunishmentId generateId() {
        StringBuilder idBuilder = new StringBuilder("#");
        String hexChars = "0123456789ABCDEF";
        for (int i = 0; i < 8; i++) {
            idBuilder.append(hexChars.charAt(RANDOM.nextInt(hexChars.length())));
        }
        return new PunishmentId(idBuilder.toString());
    }
}

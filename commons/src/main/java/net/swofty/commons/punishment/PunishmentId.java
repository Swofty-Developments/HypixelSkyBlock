package net.swofty.commons.punishment;

public record PunishmentId(String id) {

    public static PunishmentId generateId() {
        StringBuilder idBuilder = new StringBuilder("#");
        String hexChars = "0123456789ABCDEF";
        for (int i = 0; i < 8; i++) {
            int randomIndex = (int) (Math.random() * hexChars.length());
            idBuilder.append(hexChars.charAt(randomIndex));
        }
        return new PunishmentId(idBuilder.toString());
    }
}

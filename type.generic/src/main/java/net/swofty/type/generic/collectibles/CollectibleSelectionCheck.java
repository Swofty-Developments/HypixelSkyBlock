package net.swofty.type.generic.collectibles;

public record CollectibleSelectionCheck(boolean selectable, String reason) {

    public static CollectibleSelectionCheck allowed() {
        return new CollectibleSelectionCheck(true, null);
    }

    public static CollectibleSelectionCheck blocked(String reason) {
        return new CollectibleSelectionCheck(false, reason);
    }
}

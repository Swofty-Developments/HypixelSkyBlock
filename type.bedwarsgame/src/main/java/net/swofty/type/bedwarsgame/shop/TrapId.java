package net.swofty.type.bedwarsgame.shop;

public enum TrapId {
    BLINDNESS("blindness_trap"),
    COUNTER_OFFENSIVE("counter_offensive_trap"),
    REVEAL("reveal_trap"),
    MINER_FATIGUE("miner_fatigue_trap");

    private final String key;

    TrapId(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}

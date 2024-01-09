package net.swofty.types.generic.utility;

public class Tuple<A, B> {
    private A key;
    private B value;

    public Tuple(A key, B value) {
        this.key = key;
        this.value = value;
    }

    public void setKey(A key) {
        this.key = key;
    }

    public void setValue(B value) {
        this.value = value;
    }

    public A getKey() {
        return key;
    }

    public B getValue() {
        return value;
    }
}

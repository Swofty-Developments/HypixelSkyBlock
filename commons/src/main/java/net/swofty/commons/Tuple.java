package net.swofty.commons;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Tuple<A, B> {
    private A key;
    private B value;

    public Tuple(A key, B value) {
        this.key = key;
        this.value = value;
    }

}

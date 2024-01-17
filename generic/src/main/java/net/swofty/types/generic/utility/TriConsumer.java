package net.swofty.types.generic.utility;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

    /**
     * Performs the operation given the specified arguments.
     *
     * @param k the first input argument
     * @param v the second input argument
     * @param s the third input argument
     */
    void accept(T k, U v, V s);

    /**
     * Returns a composed {@link TriConsumer} that performs, in sequence, this operation followed by the {@code after}
     * operation. If performing either operation throws an exception, it is relayed to the caller of the composed
     * operation. If performing this operation throws an exception, the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation.
     * @return a composed {@link TriConsumer} that performs in sequence this operation followed by the {@code after}
     *         operation.
     * @throws NullPointerException if {@code after} is null.
     */
    default TriConsumer<T, U, V> andThen(final TriConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);

        return (t, u, v) -> {
            accept(t, u, v);
            after.accept(t, u, v);
        };
    }
}
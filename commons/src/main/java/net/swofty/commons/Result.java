package net.swofty.commons;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Either a success value of type {@code T} or an error of type {@code E},
 * never both. Use as a return type when a method has a meaningful failure
 * case that the caller is expected to handle — keeps the happy path on the
 * type signature instead of forcing a try/catch.
 *
 * <p>Modelled as a sealed two-variant interface so {@code switch} over a
 * Result is exhaustive at the compiler level:
 * <pre>{@code
 * String message = switch (result) {
 *     case Result.Ok<Integer, String> ok    -> "got " + ok.value();
 *     case Result.Err<Integer, String> err  -> "failed: " + err.error();
 * };
 * }</pre>
 *
 * <p>Variants are records, so destructuring works:
 * <pre>{@code
 * if (result instanceof Result.Ok<Integer, String>(Integer value)) {
 *     useValue(value);
 * }
 * }</pre>
 */
public sealed interface Result<T, E> {

    static <T, E> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    static <T, E> Result<T, E> err(E error) {
        return new Err<>(error);
    }

    /**
     * Runs {@code action} and wraps a thrown exception as the {@code Err}
     * variant. Useful at the boundary of a try-block to keep failure as a
     * value instead of a control-flow jump.
     */
    static <T> Result<T, Exception> attempt(Supplier<T> action) {
        try {
            return ok(action.get());
        } catch (Exception e) {
            return err(e);
        }
    }

    boolean isOk();

    default boolean isErr() {
        return !isOk();
    }

    /** Returns the success value or throws if this is an error. Prefer {@link #orElse}. */
    T unwrap();

    /** Returns the error or throws if this is a success. */
    E unwrapErr();

    /** Returns the success value or {@code other} if this is an error. */
    T orElse(T other);

    /** Returns the success value or applies {@code mapper} to the error to produce one. */
    T orElseGet(Function<? super E, ? extends T> mapper);

    Optional<T> ok();

    Optional<E> err();

    /** Maps the success value, leaving an error untouched. */
    <U> Result<U, E> map(Function<? super T, ? extends U> mapper);

    /** Maps the error, leaving the success untouched. */
    <F> Result<T, F> mapErr(Function<? super E, ? extends F> mapper);

    /** Monadic bind: chains operations that may themselves fail. */
    <U> Result<U, E> flatMap(Function<? super T, ? extends Result<U, E>> mapper);

    /** Executes the consumer if this is a success. */
    Result<T, E> ifOk(Consumer<? super T> action);

    /** Executes the consumer if this is an error. */
    Result<T, E> ifErr(Consumer<? super E> action);

    record Ok<T, E>(T value) implements Result<T, E> {
        public Ok {
            Objects.requireNonNull(value, "Ok value must not be null; use Optional for absence");
        }

        @Override public boolean isOk() { return true; }
        @Override public T unwrap() { return value; }
        @Override public E unwrapErr() { throw new NoSuchElementException("Result is Ok, not Err"); }
        @Override public T orElse(T other) { return value; }
        @Override public T orElseGet(Function<? super E, ? extends T> mapper) { return value; }
        @Override public Optional<T> ok() { return Optional.of(value); }
        @Override public Optional<E> err() { return Optional.empty(); }

        @Override
        public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
            return new Ok<>(mapper.apply(value));
        }

        @Override
        public <F> Result<T, F> mapErr(Function<? super E, ? extends F> mapper) {
            return new Ok<>(value);
        }

        @Override
        public <U> Result<U, E> flatMap(Function<? super T, ? extends Result<U, E>> mapper) {
            return mapper.apply(value);
        }

        @Override
        public Result<T, E> ifOk(Consumer<? super T> action) {
            action.accept(value);
            return this;
        }

        @Override
        public Result<T, E> ifErr(Consumer<? super E> action) {
            return this;
        }
    }

    record Err<T, E>(E error) implements Result<T, E> {
        public Err {
            Objects.requireNonNull(error, "Err value must not be null");
        }

        @Override public boolean isOk() { return false; }
        @Override public T unwrap() {
            throw error instanceof Throwable t
                    ? new RuntimeException("Result is Err: " + error, t)
                    : new NoSuchElementException("Result is Err: " + error);
        }
        @Override public E unwrapErr() { return error; }
        @Override public T orElse(T other) { return other; }
        @Override public T orElseGet(Function<? super E, ? extends T> mapper) { return mapper.apply(error); }
        @Override public Optional<T> ok() { return Optional.empty(); }
        @Override public Optional<E> err() { return Optional.of(error); }

        @Override
        public <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
            return new Err<>(error);
        }

        @Override
        public <F> Result<T, F> mapErr(Function<? super E, ? extends F> mapper) {
            return new Err<>(mapper.apply(error));
        }

        @Override
        public <U> Result<U, E> flatMap(Function<? super T, ? extends Result<U, E>> mapper) {
            return new Err<>(error);
        }

        @Override
        public Result<T, E> ifOk(Consumer<? super T> action) {
            return this;
        }

        @Override
        public Result<T, E> ifErr(Consumer<? super E> action) {
            action.accept(error);
            return this;
        }
    }
}

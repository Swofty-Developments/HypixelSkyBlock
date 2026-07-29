package net.swofty.commons;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void okHoldsValue() {
        Result<Integer, String> r = Result.ok(42);
        assertTrue(r.isOk());
        assertFalse(r.isErr());
        assertEquals(42, r.unwrap());
        assertEquals(42, r.orElse(0));
        assertEquals(42, r.ok().orElseThrow());
        assertTrue(r.err().isEmpty());
    }

    @Test
    void errHoldsError() {
        Result<Integer, String> r = Result.err("boom");
        assertTrue(r.isErr());
        assertFalse(r.isOk());
        assertEquals("boom", r.unwrapErr());
        assertEquals(99, r.orElse(99));
        assertEquals("boom", r.err().orElseThrow());
        assertTrue(r.ok().isEmpty());
    }

    @Test
    void unwrapOnErrThrows() {
        Result<Integer, String> r = Result.err("nope");
        assertThrows(NoSuchElementException.class, r::unwrap);
    }

    @Test
    void unwrapErrOnOkThrows() {
        Result<Integer, String> r = Result.ok(1);
        assertThrows(NoSuchElementException.class, r::unwrapErr);
    }

    @Test
    void mapTransformsSuccess() {
        Result<Integer, String> r = Result.<Integer, String>ok(3).map(x -> x * x);
        assertEquals(9, r.unwrap());
    }

    @Test
    void mapPassesThroughError() {
        Result<Integer, String> r = Result.<Integer, String>err("e").map(x -> x * x);
        assertEquals("e", r.unwrapErr());
    }

    @Test
    void mapErrTransformsError() {
        Result<Integer, Integer> r = Result.<Integer, String>err("bad").mapErr(String::length);
        assertEquals(3, r.unwrapErr());
    }

    @Test
    void flatMapChainsResults() {
        Result<Integer, String> r = Result.<Integer, String>ok(2)
                .flatMap(x -> Result.ok(x + 10));
        assertEquals(12, r.unwrap());

        Result<Integer, String> failed = Result.<Integer, String>ok(2)
                .flatMap(x -> Result.err("downstream"));
        assertEquals("downstream", failed.unwrapErr());
    }

    @Test
    void ifOkAndIfErrRunOnlyOnMatchingVariant() {
        AtomicReference<Integer> seen = new AtomicReference<>();
        AtomicInteger errCounter = new AtomicInteger(0);

        Result.<Integer, String>ok(7)
                .ifOk(seen::set)
                .ifErr(e -> errCounter.incrementAndGet());

        assertEquals(7, seen.get());
        assertEquals(0, errCounter.get());

        seen.set(null);
        Result.<Integer, String>err("x")
                .ifOk(seen::set)
                .ifErr(e -> errCounter.incrementAndGet());

        assertNull(seen.get());
        assertEquals(1, errCounter.get());
    }

    @Test
    void attemptCapturesException() {
        Result<Integer, Exception> ok = Result.attempt(() -> 5);
        assertEquals(5, ok.unwrap());

        Result<Integer, Exception> err = Result.attempt(() -> { throw new IllegalStateException("oh no"); });
        assertTrue(err.isErr());
        assertInstanceOf(IllegalStateException.class, err.unwrapErr());
    }

    @Test
    void switchPatternMatchingIsExhaustive() {
        Result<Integer, String> r = Result.ok(1);
        String msg = switch (r) {
            case Result.Ok<Integer, String> ok    -> "ok: " + ok.value();
            case Result.Err<Integer, String> err  -> "err: " + err.error();
        };
        assertEquals("ok: 1", msg);
    }

    @Test
    void nullsRejected() {
        assertThrows(NullPointerException.class, () -> Result.ok(null));
        assertThrows(NullPointerException.class, () -> Result.err(null));
    }
}

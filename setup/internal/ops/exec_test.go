package ops

import (
	"errors"
	"fmt"
	"testing"
	"time"
)

func TestRunWithRetryEventuallySucceeds(t *testing.T) {
	attempts := 0
	err := RunWithRetry(RetryPolicy{Attempts: 4, InitialDelay: time.Millisecond, MaxDelay: 2 * time.Millisecond, Factor: 2}, func() error {
		attempts++
		if attempts < 3 {
			return fmt.Errorf("attempt %d failed", attempts)
		}
		return nil
	})
	if err != nil {
		t.Fatalf("RunWithRetry returned error: %v", err)
	}
	if attempts != 3 {
		t.Fatalf("expected 3 attempts, got %d", attempts)
	}
}

func TestRunWithRetryReturnsLastError(t *testing.T) {
	expected := errors.New("still failing")
	err := RunWithRetry(RetryPolicy{Attempts: 3, InitialDelay: time.Millisecond, MaxDelay: 2 * time.Millisecond, Factor: 2}, func() error {
		return expected
	})
	if !errors.Is(err, expected) {
		t.Fatalf("expected %v, got %v", expected, err)
	}
}

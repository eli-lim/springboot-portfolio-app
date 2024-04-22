package com.example.portfolio.market.pricing.engine;

import java.time.Instant;

/**
 * Represents a time step between two points in time.
 */
public class TimeStep {

    private final Instant lastEpoch;

    private final Instant currEpoch;

    public TimeStep(Instant lastEpoch) {
        this.lastEpoch = lastEpoch;
        this.currEpoch = lastEpoch;
    }

    public TimeStep(Instant lastEpoch, Instant currEpoch) {
        this.lastEpoch = lastEpoch;
        this.currEpoch = currEpoch;
    }

    public static TimeStep fromNow() {
        Instant now = Instant.now();
        return new TimeStep(now, now);
    }

    public static TimeStep from(Instant instant) {
        return new TimeStep(instant, instant);
    }

    public TimeStep advance(long ms) {
        return new TimeStep(currEpoch, currEpoch.plusMillis(ms));
    }

    public Instant getLast() {
        return lastEpoch;
    }

    public Instant getCurrent() {
        return currEpoch;
    }

    public long getDeltaMs() {
        return currEpoch.toEpochMilli() - lastEpoch.toEpochMilli();
    }
}

package no.spp.sdk.utils;

public class Timer {
    private final long startMillis;

    public Timer() {
        startMillis = System.currentTimeMillis();
    }

    public long getElapsedTime() {
        return (System.currentTimeMillis() - startMillis);
    }
}

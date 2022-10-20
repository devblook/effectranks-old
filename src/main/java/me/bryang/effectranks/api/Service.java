package me.bryang.effectranks.api;

public interface Service {

    void start();

    default void stop() {
        // Do nothing
    }
}

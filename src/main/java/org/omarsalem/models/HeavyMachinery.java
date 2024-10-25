package org.omarsalem.models;

public interface HeavyMachinery {
    Direction getDirection();

    void advance();

    boolean isOutsideBoundary(int length, int width);

    void right();

    void left();

    Position getPosition();
}

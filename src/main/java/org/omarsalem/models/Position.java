package org.omarsalem.models;

public record Position(int x, int y) {
    public Position translate(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }
}

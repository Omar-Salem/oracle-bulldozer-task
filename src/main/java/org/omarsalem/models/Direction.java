package org.omarsalem.models;

public enum Direction {
    EAST(0, 1, 0),
    SOUTH(90, 0, 1),
    WEST(180, -1, 0),
    NORTH(270, 0, -1);

    private final int heading;
    private final int xTranslate;
    private final int yTranslate;

    Direction(int heading, int xTranslate, int yTranslate) {
        this.heading = heading;
        this.xTranslate = xTranslate;
        this.yTranslate = yTranslate;
    }

    public static Direction byHeading(int heading) {
        for (Direction e : values()) {
            if (e.heading == heading) {
                return e;
            }
        }
        throw new IllegalArgumentException("Could not find Direction for value " + heading);
    }

    public int getHeading() {
        return heading;
    }

    public int getxTranslate() {
        return xTranslate;
    }

    public int getyTranslate() {
        return yTranslate;
    }
}

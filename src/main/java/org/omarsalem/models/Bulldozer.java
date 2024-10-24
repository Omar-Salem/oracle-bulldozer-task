package org.omarsalem.models;

public class Bulldozer {
    private static final int TURN_ANGLE = 90;
    public static final int FULL_CIRCLE = 360;

    private int x;
    private int y;
    private Direction direction;

    public Bulldozer(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public void advance() {
        x += direction.getxTranslate();
        y += direction.getyTranslate();
    }

    public boolean isOutsideBoundary(int length, int width) {
        final int newX = x + direction.getxTranslate();
        final int newY = y + direction.getyTranslate();
        return newX > width ||
                newY > length ||
                newX < 0 ||
                newY < 0;
    }

    public void right() {
        updateHeading(TURN_ANGLE);
    }

    public void left() {
        updateHeading(-TURN_ANGLE);
    }

    private void updateHeading(int angle) {
        int heading = direction.getHeading();
        heading += angle;
        if (heading < 0) {
            heading += FULL_CIRCLE;
        }
        heading %= FULL_CIRCLE;
        direction = Direction.byHeading(heading);
    }

    @Override
    public String toString() {
        return "Bulldozer{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                '}';
    }
}

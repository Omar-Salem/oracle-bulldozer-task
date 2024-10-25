package org.omarsalem.models;

public class Bulldozer {
    private static final int TURN_ANGLE = 90;
    public static final int FULL_CIRCLE = 360;

    private Position position;
    private Direction direction;

    public Bulldozer(Position position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void advance() {
        position = position.translate(direction.getxTranslate(), direction.getyTranslate());
    }

    public boolean isOutsideBoundary(int length, int width) {
        final int newX = position.x() + direction.getxTranslate();
        final int newY = position.y() + direction.getyTranslate();
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

    public Position getPosition() {
        return position;
    }
}

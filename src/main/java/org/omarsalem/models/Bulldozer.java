package org.omarsalem.models;

public class Bulldozer implements HeavyMachinery {
    private static final int TURN_ANGLE = 90;
    public static final int FULL_CIRCLE = 360;

    private Position position;
    private Direction direction;

    public Bulldozer(Position position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void advance() {
        position = position.translate(direction.getxTranslate(), direction.getyTranslate());
    }

    @Override
    public boolean isOutsideBoundary(int length, int width) {
        final int newX = position.x() + direction.getxTranslate();
        final int newY = position.y() + direction.getyTranslate();
        return newX > width ||
                newY > length ||
                newX < 0 ||
                newY < 0;
    }

    @Override
    public void right() {
        updateHeading(TURN_ANGLE);
    }

    @Override
    public void left() {
        updateHeading(-TURN_ANGLE);
    }

    @Override
    public Position getPosition() {
        return position;
    }

    private void updateHeading(int angle) {
        int heading = direction.getHeading();
        heading += angle;
        if (heading < 0) {
            heading += Bulldozer.FULL_CIRCLE;
        }
        heading %= Bulldozer.FULL_CIRCLE;
        direction = Direction.byHeading(heading);
    }
}

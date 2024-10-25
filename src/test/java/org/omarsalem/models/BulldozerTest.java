package org.omarsalem.models;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BulldozerTest {

    @ParameterizedTest
    @MethodSource("provideRightTurnArgs")
    void right_turn_updates_heading(int numberOfTurns, Direction expectedDirection) {
        //Arrange
        final HeavyMachinery target = new Bulldozer(new Position( 0, 0), Direction.EAST);

        //Act
        for (int i = 0; i < numberOfTurns; i++) {
            target.right();
        }

        //Assert
        assertEquals(expectedDirection, target.getDirection());
    }

    @ParameterizedTest
    @MethodSource("provideLeftTurnArgs")
    void left_turn_updates_heading(int numberOfTurns, Direction expectedDirection) {
        //Arrange
        final HeavyMachinery target = new Bulldozer(new Position( 0, 0), Direction.EAST);

        //Act
        for (int i = 0; i < numberOfTurns; i++) {
            target.left();
        }

        //Assert
        assertEquals(expectedDirection, target.getDirection());
    }

    @ParameterizedTest
    @MethodSource("provideAdvanceArgs")
    void advance_updates_position(Direction startingDirection, int expectedX, int expectedY) {
        //Arrange
        final HeavyMachinery target = new Bulldozer(new Position( 0, 0), startingDirection);

        //Act
        target.advance();

        //Assert
        assertEquals(new Position(expectedX, expectedY), target.getPosition());
    }

    @ParameterizedTest
    @MethodSource("provideBoundaryCheckingArgs")
    void boundary_checking(int x, int y, Direction direction, boolean isOutOfBounds) {
        //Arrange
        final HeavyMachinery target = new Bulldozer(new Position( x, y), direction);

        //Act
        final boolean actual = target.isOutsideBoundary(5, 7);

        //Assert
        assertEquals(isOutOfBounds, actual);
    }

    private static Stream<Arguments> provideRightTurnArgs() {
        return Stream.of(
                Arguments.of(1, Direction.SOUTH),
                Arguments.of(2, Direction.WEST),
                Arguments.of(3, Direction.NORTH),
                Arguments.of(4, Direction.EAST)
        );
    }

    private static Stream<Arguments> provideLeftTurnArgs() {
        return Stream.of(
                Arguments.of(1, Direction.NORTH),
                Arguments.of(2, Direction.WEST),
                Arguments.of(3, Direction.SOUTH),
                Arguments.of(4, Direction.EAST)
        );
    }

    public static Stream<Arguments> provideAdvanceArgs() {

        return Stream.of(
                Arguments.of(Direction.NORTH, 0, 1),
                Arguments.of(Direction.WEST, -1, 0),
                Arguments.of(Direction.SOUTH, 0, -1),
                Arguments.of(Direction.EAST, 1, 0)
        );
    }

    public static Stream<Arguments> provideBoundaryCheckingArgs() {

        return Stream.of(
                Arguments.of(7, 5, Direction.EAST, true),
                Arguments.of(7, 5, Direction.WEST, false),
                Arguments.of(7, 5, Direction.NORTH, true),
                Arguments.of(7, 5, Direction.SOUTH, false),

                Arguments.of(0, 0, Direction.EAST, false),
                Arguments.of(0, 0, Direction.WEST, true),
                Arguments.of(0, 0, Direction.NORTH, false),
                Arguments.of(0, 0, Direction.SOUTH, true)
        );
    }
}
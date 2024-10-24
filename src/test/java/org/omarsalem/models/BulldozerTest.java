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
        final Bulldozer target = new Bulldozer(0, 0, Direction.EAST);

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
        final Bulldozer target = new Bulldozer(0, 0, Direction.EAST);

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
        final Bulldozer target = new Bulldozer(0, 0, startingDirection);

        //Act
        target.advance();

        //Assert
        assertPosition(expectedX, expectedY, target);
    }

    private static void assertPosition(int expectedX, int expectedY, Bulldozer target) {
        assertEquals(expectedX, target.getX());
        assertEquals(expectedY, target.getY());
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
}
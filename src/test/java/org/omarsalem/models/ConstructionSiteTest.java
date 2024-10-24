package org.omarsalem.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.omarsalem.models.CommandType.*;

class ConstructionSiteTest {

    @Test
    void operation_adds_to_command_list() {
        //Arrange
        final ConstructionSite target = new ConstructionSite(new char[][]{
                {'o', 'o'},
                {'o', 'o'}
        });
        final List<Command> expected = List.of(new Command(LEFT),
                new Command(RIGHT),
                new Command(RIGHT),
                new PayloadCommand<>(ADVANCE, 5),
                new Command(QUIT));

        //Act
        expected.forEach(target::handleCommand);

        //Assert
        assertEquals(expected, target.getCommands());
    }

    @ParameterizedTest
    @MethodSource("provideCommandArgs")
    void command_parsed_and_calls_bulldozer_actions(Command cmd, Direction direction, int x, int y) {
        //Arrange
        final ConstructionSite target = new ConstructionSite(new char[][]{
                {'o', 'o'},
                {'o', 'o'}
        });

        //Act
        target.handleCommand(cmd);

        //Assert
        assertEquals(direction, target.getBulldozer().getDirection());
        assertEquals(x, target.getBulldozer().getX());
        assertEquals(y, target.getBulldozer().getY());
    }

    @Test
    void bulldozer_out_of_bounds_ends_simulation() {
        //Arrange
        final ConstructionSite target = new ConstructionSite(new char[][]{
                {'o', 'o'},
                {'o', 'o'}
        });
        final Command command = new PayloadCommand<>(ADVANCE, 3);

        //Act
        boolean simulationEnded = target.handleCommand(command);

        //Assert
        assertTrue(simulationEnded);
    }

    @Test
    void quit_command_ends_simulation() {
        //Arrange
        final ConstructionSite target = new ConstructionSite(new char[][]{
                {'o', 'o'},
                {'o', 'o'}
        });
        final Command command = new Command(QUIT);

        //Act
        boolean simulationEnded = target.handleCommand(command);

        //Assert
        assertTrue(simulationEnded);
    }

    @Test
    void bulldozer_within_bounds_simulation_keeps_running() {
        //Arrange
        final ConstructionSite target = new ConstructionSite(new char[][]{
                {'o', 'o'},
                {'o', 'o'}
        });
        final Command command = new PayloadCommand<>(ADVANCE, 1);

        //Act
        boolean simulationEnded = target.handleCommand(command);

        //Assert
        assertFalse(simulationEnded);
    }

    public static Stream<Arguments> provideCommandArgs() {
        return Stream.of(
                Arguments.of(new Command(LEFT), Direction.NORTH, -1, 0),
                Arguments.of(new Command(RIGHT), Direction.SOUTH, -1, 0),
                Arguments.of(new PayloadCommand<>(ADVANCE, 2), Direction.EAST, 1, 0)
        );
    }
}
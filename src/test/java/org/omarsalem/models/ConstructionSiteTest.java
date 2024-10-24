package org.omarsalem.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

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

    @ParameterizedTest
    @MethodSource("provideFuelCalculationArgs")
    void fuel_calculation_for_a_single_square(char c, int expected) {
        //Arrange
        final ConstructionSite target = new ConstructionSite(new char[][]{
                {c},
                {c}
        });
        final Command command = new PayloadCommand<>(ADVANCE, 1);

        //Act
        target.handleCommand(command);

        //Assert
        assertEquals(expected, target.getFuelCost());
    }

    @Test
    void fuel_calculation_for_a_single_row() {
        //Arrange
        final char[][] map = {
                {'o', 'c', 'r', 't', 'T'},
                {'o', 'c', 'r', 't', 'T'},
        };
        final ConstructionSite target = new ConstructionSite(map);
        final Command command = new PayloadCommand<>(ADVANCE, 1);

        //Act
        for (int i = 0; i < map[0].length; i++) {
            target.handleCommand(command);
        }

        //Assert
        assertEquals(8, target.getFuelCost());
    }

    @Test
    void bulldozer_clears_squares_it_passes() {
        //Arrange
        final char[][] map = {
                {'o', 'c', 'r', 't', 'T'},
                {'o', 'c', 'r', 't', 'T'},
        };
        final ConstructionSite target = new ConstructionSite(map);
        final Command command = new PayloadCommand<>(ADVANCE, 1);

        //Act
        for (int i = 0; i < map[0].length; i++) {
            target.handleCommand(command);
        }

        //Assert
        final List<Character> expected = List.of('c', 'c', 'c', 'c', 'c');
        final List<Character> actual = Arrays
                .stream(target.getSite()[0])
                .map(s -> s.getSymbol())
                .collect(Collectors.toList());
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("providePenaltyCalculationArgs")
    void penalty_calculation(char c, int expected) {
        //Arrange
        final ConstructionSite target = new ConstructionSite(new char[][]{
                {c},
                {c}
        });
        final Command command = new PayloadCommand<>(ADVANCE, 1);

        //Act
        target.handleCommand(command);

        //Assert
        assertEquals(expected, target.getFuelCost());
    }

    public static Stream<Arguments> provideCommandArgs() {
        return Stream.of(
                Arguments.of(new Command(LEFT), Direction.NORTH, -1, 0),
                Arguments.of(new Command(RIGHT), Direction.SOUTH, -1, 0),
                Arguments.of(new PayloadCommand<>(ADVANCE, 2), Direction.EAST, 1, 0)
        );
    }

    public static Stream<Arguments> provideFuelCalculationArgs() {
        return Stream.of(
                Arguments.of('o', 1),
                Arguments.of('c', 1),
                Arguments.of('r', 2),
                Arguments.of('t', 2),
                Arguments.of('T', 2)
        );
    }

    public static Stream<Arguments> providePenaltyCalculationArgs() {
        return Stream.of(
                Arguments.of('o', 1),
                Arguments.of('c', 1),
                Arguments.of('r', 2),
                Arguments.of('t', 2),
                Arguments.of('T', 2)
        );
    }
}
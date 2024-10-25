package org.omarsalem.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.omarsalem.models.ClearingOperationType.*;
import static org.omarsalem.models.CommandType.*;
import static org.omarsalem.models.SimulationResult.*;

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
        assertEquals(direction, target.getHeavyMachineryDirection());
        final Position position = target.getHeavyMachineryPosition();
        assertEquals(x, position.x());
        assertEquals(y, position.y());
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
        final SimulationResult simulationResult = target.handleCommand(command);

        //Assert
        assertEquals(SimulationResult.OUT_OF_BOUNDS, simulationResult);
    }

    @Test
    void bulldozer_hitting_a_protected_tree_ends_simulation() {
        //Arrange
        final ConstructionSite target = new ConstructionSite(new char[][]{
                {'T', 'o'},
                {'o', 'o'}
        });
        final Command command = new PayloadCommand<>(ADVANCE, 3);

        //Act
        final SimulationResult simulationResult = target.handleCommand(command);

        //Assert
        assertEquals(SimulationResult.PROTECTED_TREE_DESTRUCTION, simulationResult);
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
        final SimulationResult simulationResult = target.handleCommand(command);

        //Assert
        assertEquals(USER_REQUEST, simulationResult);
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
        final SimulationResult simulationResult = target.handleCommand(command);

        //Assert
        assertEquals(RUNNING, simulationResult);
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
        assertEquals(expected, target
                .getSimulationCost()
                .getCostByOperationType(FUEL));
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
        assertEquals(8, target
                .getSimulationCost()
                .getCostByOperationType(FUEL));
    }

    @Test
    void penalty_calculation_for_machinery_drive_communication() {
        //Arrange
        final char[][] map = {
                {'o', 'c', 'r'},
                {'c', 'c', 'c'},
        };
        final ConstructionSite target = new ConstructionSite(map);
        //Act

        target.handleCommand(new PayloadCommand<>(ADVANCE, 1));
        target.handleCommand(new Command(LEFT));
        target.handleCommand(new Command(RIGHT));

        //Assert
        assertEquals(3, target
                .getSimulationCost()
                .getCostByOperationType(COMMUNICATION));
    }

    @Test
    void quit_command_not_added_in_communication_penalty() {
        //Arrange
        final char[][] map = {
                {'o', 'c', 'r'},
                {'c', 'c', 'c'},
        };
        final ConstructionSite target = new ConstructionSite(map);
        //Act

        target.handleCommand(new Command(QUIT));

        //Assert
        assertEquals(0, target
                .getSimulationCost()
                .getCostByOperationType(COMMUNICATION));
    }

    @Test
    void not_clearing_all_squares_incurs_penalty() {
        //Arrange
        final char[][] map = {
                {'o', 'o', 'r'},
                {'o', 'o', 'o'},
        };
        final ConstructionSite target = new ConstructionSite(map);
        final Command command = new PayloadCommand<>(ADVANCE, 3);

        //Act
        target.handleCommand(command);

        //Assert
        assertEquals(9, target
                .getSimulationCost()
                .getCostByOperationType(UNCLEARED_SQUARE));
    }

    @Test
    void not_stopping_at_a_tree_incurs_paint_damage_penalty() {
        //Arrange
        final char[][] map = {
                {'o', 't', 'r'},
                {'c', 'c', 'c'},
        };
        final ConstructionSite target = new ConstructionSite(map);
        final Command command = new PayloadCommand<>(ADVANCE, 3);

        //Act
        target.handleCommand(command);

        //Assert
        assertEquals(2, target
                .getSimulationCost()
                .getCostByOperationType(PAINT_DAMAGE));
    }

    @Test
    void stopping_at_a_tree_is_not_penalized() {
        //Arrange
        final char[][] map = {
                {'o', 'r', 't'},
                {'c', 'c', 'c'},
        };
        final ConstructionSite target = new ConstructionSite(map);
        final Command command = new PayloadCommand<>(ADVANCE, 3);

        //Act
        target.handleCommand(command);

        //Assert
        assertEquals(0, target
                .getSimulationCost()
                .getCostByOperationType(PAINT_DAMAGE));
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
}
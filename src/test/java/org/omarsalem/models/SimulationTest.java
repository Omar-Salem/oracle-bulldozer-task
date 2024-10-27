package org.omarsalem.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.omarsalem.models.ClearingOperationType.*;
import static org.omarsalem.models.CommandType.*;
import static org.omarsalem.models.SimulationStatus.*;

class SimulationTest {

    @ParameterizedTest
    @MethodSource("provideDifferentLandTypes")
    void fuel_calculation_for_a_single_square_depends_on_land_type(char squareType, int expected) {
        //Arrange
        final Simulation target = new Simulation(new char[][]{
                {squareType},
                {squareType}
        });
        final Command command = new AdvanceCommand(ADVANCE, 1);

        //Act
        target.handleCommand(command);

        //Assert
        assertEquals(expected, target
                .getSimulationCost()
                .getCostByOperationType(FUEL));
    }

    @Test
    void fuel_calculation_for_a_single_row_is_sum_of_squares() {
        //Arrange
        final char[][] map = {
                {'o', 'c', 'r', 't', 'T'},
                {'o', 'c', 'r', 't', 'T'},
        };
        final Simulation target = new Simulation(map);
        final Command command = new AdvanceCommand(ADVANCE, 1);

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
    void passing_a_tree_incurs_paint_damage_penalty() {
        //Arrange
        final char[][] map = {
                {'o', 't', 'r'},
                {'c', 'c', 'c'},
        };
        final Simulation target = new Simulation(map);
        final Command command = new AdvanceCommand(ADVANCE, 3);

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
        final Simulation target = new Simulation(map);
        final Command command = new AdvanceCommand(ADVANCE, 3);

        //Act
        target.handleCommand(command);

        //Assert
        assertEquals(0, target
                .getSimulationCost()
                .getCostByOperationType(PAINT_DAMAGE));
    }

    @Test
    void hitting_a_protected_tree_ends_simulation() {
        //Arrange
        final Simulation target = new Simulation(new char[][]{
                {'T', 'o'},
                {'o', 'o'}
        });
        final Command command = new AdvanceCommand(ADVANCE, 3);

        //Act
        final SimulationStatus simulationStatus = target.handleCommand(command);

        //Assert
        assertEquals(SimulationStatus.PROTECTED_TREE_DESTRUCTION, simulationStatus);
    }

    @Test
    void hitting_a_protected_tree_incurs_penalty() {
        //Arrange
        final Simulation target = new Simulation(new char[][]{
                {'T', 'o'},
                {'o', 'o'}
        });
        final Command command = new AdvanceCommand(ADVANCE, 3);

        //Act
        target.handleCommand(command);

        //Assert
        assertEquals(10, target.getSimulationCost().getCostByOperationType(ClearingOperationType.PROTECTED_TREE_DESTRUCTION));
    }

    @Test
    void drive_commands_adds_to_communication_penalty() {
        //Arrange
        final char[][] map = {
                {'o', 'c', 'r'},
                {'c', 'c', 'c'},
        };
        final Simulation target = new Simulation(map);
        //Act

        target.handleCommand(new AdvanceCommand(ADVANCE, 1));
        target.handleCommand(new Command(LEFT));
        target.handleCommand(new Command(RIGHT));

        //Assert
        assertEquals(3, target
                .getSimulationCost()
                .getCostByOperationType(COMMUNICATION));
    }

    @Test
    void quit_command_not_included_in_communication_penalty() {
        //Arrange
        final char[][] map = {
                {'o', 'c', 'r'},
                {'c', 'c', 'c'},
        };
        final Simulation target = new Simulation(map);
        //Act

        target.handleCommand(new Command(QUIT));

        //Assert
        assertEquals(0, target
                .getSimulationCost()
                .getCostByOperationType(COMMUNICATION));
    }

    @Test
    void quit_command_ends_simulation() {
        //Arrange
        final Simulation target = new Simulation(new char[][]{
                {'o', 'o'},
                {'o', 'o'}
        });
        final Command command = new Command(QUIT);

        //Act
        final SimulationStatus simulationStatus = target.handleCommand(command);

        //Assert
        assertEquals(USER_REQUEST, simulationStatus);
    }

    @Test
    void commands_no_longer_accepted_after_simulation_ends() {
        //Arrange
        final Simulation target = new Simulation(new char[][]{
                {'o', 'o'},
                {'o', 'o'}
        });

        //Act
        target.handleCommand(new Command(QUIT));

        //Assert
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            target.handleCommand(new Command(RIGHT));
        });

        final String expectedMessage = "Simulation ended";
        final String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void bulldozer_out_of_bounds_ends_simulation() {
        //Arrange
        final Simulation target = new Simulation(new char[][]{
                {'o', 'o'},
                {'o', 'o'}
        });
        final Command command = new AdvanceCommand(ADVANCE, 3);

        //Act
        final SimulationStatus simulationStatus = target.handleCommand(command);

        //Assert
        assertEquals(SimulationStatus.OUT_OF_BOUNDS, simulationStatus);
    }

    @Test
    void bulldozer_within_bounds_simulation_keeps_running() {
        //Arrange
        final Simulation target = new Simulation(new char[][]{
                {'o', 'o'},
                {'o', 'o'}
        });
        final Command command = new AdvanceCommand(ADVANCE, 1);

        //Act
        final SimulationStatus simulationStatus = target.handleCommand(command);

        //Assert
        assertEquals(RUNNING, simulationStatus);
    }

    @ParameterizedTest
    @MethodSource("provideBulldozerDriveCommands")
    void command_parsed_and_calls_bulldozer_actions(Command cmd, Direction direction, int x, int y) {
        //Arrange
        final Simulation target = new Simulation(new char[][]{
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
    void operation_adds_to_command_list() {
        //Arrange
        final Simulation target = new Simulation(new char[][]{
                {'o', 'o'},
                {'o', 'o'}
        });
        final List<Command> expected = List.of(
                new AdvanceCommand(ADVANCE, 2),
                new Command(LEFT),
                new Command(RIGHT),
                new Command(RIGHT),
                new Command(QUIT));

        //Act
        expected.forEach(target::handleCommand);

        //Assert
        assertEquals(expected, target.getCommands());
    }

    @Test
    void commands_represented_as_comma_separated_string() {
        //Arrange
        final Simulation target = new Simulation(new char[][]{
                {'o', 'o'},
                {'o', 'o'}
        });
        final List<Command> expected = List.of(
                new AdvanceCommand(ADVANCE, 1),
                new Command(RIGHT),
                new Command(LEFT),
                new Command(QUIT));

        //Act
        expected.forEach(target::handleCommand);

        //Assert
        assertEquals("advance 1, turn right, turn left, quit", target.getCommandsAsString());
    }

    @Test
    void not_clearing_all_squares_incurs_penalty() {
        //Arrange
        final char[][] map = {
                {'o', 'o', 'r'},
                {'o', 'o', 'o'},
        };
        final Simulation target = new Simulation(map);
        final Command command = new AdvanceCommand(ADVANCE, 3);

        //Act
        target.handleCommand(command);

        //Assert
        assertEquals(9, target
                .getSimulationCost()
                .getCostByOperationType(UNCLEARED_SQUARE));
    }

    @Test
    void bulldozer_clears_squares_it_passes() {
        //Arrange
        final char[][] map = {
                {'o', 'o', 'r', 't', 'T'},
                {'c', 'c', 'c', 'c', 'c'},
        };
        final Simulation target = new Simulation(map);
        final Command command = new AdvanceCommand(ADVANCE, 1);

        //Act
        for (int i = 0; i < map[0].length; i++) {
            target.handleCommand(command);
        }

        //Assert
        assertEquals(0, target.getSite().getUnclearedSquaresCount());
    }

    @Test
    void sample_scenario() {
        //Arrange
        final char[][] map = {
                {'o', 'o', 't', 'o', 'o', 'o', 'o', 'o', 'o', 'o'},
                {'o', 'o', 'o', 'o', 'o', 'o', 'o', 'T', 'o', 'o'},
                {'r', 'r', 'r', 'o', 'o', 'o', 'o', 'T', 'o', 'o'},
                {'r', 'r', 'r', 'r', 'o', 'o', 'o', 'o', 'o', 'o'},
                {'r', 'r', 'r', 'r', 'r', 't', 'o', 'o', 'o', 'o'},
        };
        final Simulation target = new Simulation(map);
        final List<Command> commands = List.of(
                new AdvanceCommand(ADVANCE, 4),
                new Command(RIGHT),
                new AdvanceCommand(ADVANCE, 4),
                new Command(LEFT),
                new AdvanceCommand(ADVANCE, 2),
                new AdvanceCommand(ADVANCE, 4),
                new Command(LEFT),
                new Command(QUIT)
        );

        //Act
        commands.forEach(target::handleCommand);

        //Assert
        final SimulationCost expectedCost = new SimulationCost(List.of(
                new ClearingOperation(COMMUNICATION, 7),
                new ClearingOperation(FUEL, 19),
                new ClearingOperation(UNCLEARED_SQUARE, 34),
                new ClearingOperation(ClearingOperationType.PROTECTED_TREE_DESTRUCTION, 0),
                new ClearingOperation(PAINT_DAMAGE, 1)));
        assertEquals(expectedCost, target.getSimulationCost());
        assertEquals(commands, target.getCommands());
    }

    public static Stream<Arguments> provideBulldozerDriveCommands() {
        return Stream.of(
                Arguments.of(new Command(LEFT), Direction.NORTH, -1, 0),
                Arguments.of(new Command(RIGHT), Direction.SOUTH, -1, 0),
                Arguments.of(new AdvanceCommand(ADVANCE, 2), Direction.EAST, 1, 0)
        );
    }

    public static Stream<Arguments> provideDifferentLandTypes() {
        return Stream.of(
                Arguments.of('o', 1),
                Arguments.of('c', 1),
                Arguments.of('r', 2),
                Arguments.of('t', 2),
                Arguments.of('T', 2)
        );
    }
}
package org.omarsalem;

import org.omarsalem.models.*;
import org.omarsalem.services.*;

import java.util.Scanner;

import static org.omarsalem.models.SimulationResult.RUNNING;

/**
 * Hello world!
 */
public class App {
    private static MapReaderService fileMapReaderService;
    private static Simulation simulation;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Please enter map file path");
        }
        final String mapPathArg = args[0];
        fileMapReaderService = new FileMapReaderService(mapPathArg);
        final char[][] map = fileMapReaderService.getMap();
        simulation = new Simulation(map);
        System.out.println("Welcome to the Aconex site clearing simulator. This is a map of the site:\n");
        displayMap(map);

        System.out.println("The bulldozer is currently located at the Northern edge of the site, immediately to the West of the site, and facing East.\n");

        final SimulationResult simulationResult = runSimulation();
        System.out.println();

        displaySimulationResult(simulationResult);

        final String commandsAsString = simulation.getCommandsAsString();
        displayCommands(commandsAsString);

        final SimulationCost simulationCost = simulation.getSimulationCost();
        displaySimulationCost(simulationCost);

        System.out.println("Thank you for using the Aconex site clearing simulator.");
    }

    private static void displayCommands(String commandsAsString) {
        System.out.println(commandsAsString);
        System.out.println();
    }

    private static SimulationResult runSimulation() {
        SimulationResult simulationResult = RUNNING;
        while (RUNNING.equals(simulationResult)) {
            System.out.print("(l)eft, (r)ight, (a)dvance <n>, (q)uit:");
            final String[] commandArgs = scanner.nextLine().split(" ");
            if (commandArgs.length == 0) {
                throw new IllegalArgumentException("Empty arguments");
            }
            final char commandSymbol = commandArgs[0].charAt(0);
            final CommandType commandType = CommandType.bySymbol(commandSymbol);
            final Command command = switch (commandType) {
                case ADVANCE -> {
                    final int steps = Integer.parseInt(commandArgs[1]);
                    yield new PayloadCommand<>(commandType, steps);
                }
                case LEFT, RIGHT, QUIT -> new Command(commandType);
            };
            simulationResult = simulation.handleCommand(command);
        }
        return simulationResult;
    }

    private static void displaySimulationResult(SimulationResult simulationResult) {
        switch (simulationResult) {
            case OUT_OF_BOUNDS -> System.out.print("The simulation has ended because machinery drove out of bounds.");
            case PROTECTED_TREE_DESTRUCTION ->
                    System.out.print("The simulation has ended because a protected tree was destroyed.");
            case USER_REQUEST -> System.out.print("The simulation has ended at your request.");
            case RUNNING -> System.out.print("The simulation is still running.");
        }
        System.out.println(" These are the commands you issued:");
    }

    private static void displaySimulationCost(SimulationCost simulationCost) {
        System.out.println("The costs for this land clearing operation were:\n");
        System.out.println(simulationCost);
        System.out.println();
    }

    private static void displayMap(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }
}

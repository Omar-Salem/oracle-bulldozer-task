package org.omarsalem.models;

import java.util.*;
import java.util.stream.Collectors;

import static org.omarsalem.models.ClearingOperationType.*;
import static org.omarsalem.models.CommandType.QUIT;
import static org.omarsalem.models.Direction.EAST;
import static org.omarsalem.models.SimulationResult.*;

public class Simulation {
    private final ConstructionSite site;
    private final HeavyMachinery heavyMachinery;

    private final List<Command> commands = new ArrayList<>();
    private double fuelCost = 0;
    private double protectedTreePenalty = 0;
    private double paintDamage = 0;

    public Simulation(char[][] map) {
        this(map, new Bulldozer(new Position(-1, 0), EAST));
    }

    public Simulation(char[][] map, HeavyMachinery heavyMachinery) {
        this.site = new ConstructionSite(map);
        this.heavyMachinery = heavyMachinery;
    }

    public SimulationResult handleCommand(Command command) {
        SimulationResult simulationResult = RUNNING;
        switch (command.getCommandType()) {
            case ADVANCE -> {
                final PayloadCommand<Integer> payloadCommand = (PayloadCommand<Integer>) command;
                simulationResult = advance(payloadCommand.getPayload());
            }
            case LEFT -> heavyMachinery.left();
            case RIGHT -> heavyMachinery.right();
            case QUIT -> simulationResult = USER_REQUEST;
            default -> throw new IllegalStateException("Unhandled command type: " + command.getCommandType());
        }
        commands.add(command);
        return simulationResult;
    }

    private SimulationResult advance(Integer steps) {
        for (int i = 0; i < steps; i++) {
            if (heavyMachinery.isOutsideBoundary(site.getLength() - 1, site.getWidth() - 1)) {
                return OUT_OF_BOUNDS;
            }
            heavyMachinery.advance();
            final Position position = heavyMachinery.getPosition();
            final boolean isStillPassing = i < steps - 1;
            final Square.VisitResult visitResult = site.visit(position, isStillPassing);
            fuelCost += visitResult.fuelCost();
            paintDamage += visitResult.paintDamage();
            protectedTreePenalty = visitResult.protectedTreePenalty();
            if (protectedTreePenalty != 0) {
                return SimulationResult.PROTECTED_TREE_DESTRUCTION;
            }
        }
        return RUNNING;
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public String getCommandsAsString() {
        return getCommands()
                .stream()
                .map(c -> {
                    if (c instanceof PayloadCommand<?>) {
                        final String payload = ((PayloadCommand<?>) c).getPayload().toString();
                        return c.getCommandType().getDescription() + " " + payload;
                    }
                    return c.getCommandType().getDescription();
                })
                .collect(Collectors.joining(", "));
    }

    public Position getHeavyMachineryPosition() {
        return heavyMachinery.getPosition();
    }

    public Direction getHeavyMachineryDirection() {
        return heavyMachinery.getDirection();
    }

    public SimulationCost getSimulationCost() {
        return new SimulationCost(List.of(
                new ClearingOperation(COMMUNICATION, getMachineryDrivingCommandsCount()),
                new ClearingOperation(FUEL, fuelCost),
                new ClearingOperation(UNCLEARED_SQUARE, site.getUnclearedSquaresCount()),
                new ClearingOperation(ClearingOperationType.PROTECTED_TREE_DESTRUCTION, protectedTreePenalty),
                new ClearingOperation(PAINT_DAMAGE, paintDamage)
        ));
    }

    private double getMachineryDrivingCommandsCount() {
        return commands
                .stream()
                .filter(c -> !QUIT.equals(c.getCommandType()))
                .count();
    }

    public ConstructionSite getSite() {
        return site;
    }
}

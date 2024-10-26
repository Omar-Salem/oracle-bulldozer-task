package org.omarsalem.models;

import java.util.*;
import java.util.stream.*;

import static org.omarsalem.models.ClearingOperationType.*;
import static org.omarsalem.models.CommandType.QUIT;
import static org.omarsalem.models.Direction.EAST;
import static org.omarsalem.models.SimulationResult.*;

public class ConstructionSite {
    private final Square[][] site;
    private final int length;
    private final int width;
    private final HeavyMachinery heavyMachinery;
    private final List<Command> commands = new ArrayList<>();
    private double fuelCost = 0;
    private double protectedTreePenalty = 0;
    private double paintDamage = 0;

    public ConstructionSite(char[][] map) {
        this(map, new Bulldozer(new Position(-1, 0), EAST));
    }

    public ConstructionSite(char[][] map, HeavyMachinery heavyMachinery) {
        this.heavyMachinery = heavyMachinery;
        if (Stream.of(map)
                .anyMatch(row -> row.length != map[0].length)) {
            throw new IllegalArgumentException("Site rows length mismatch");
        }
        this.length = map.length;
        this.width = map[0].length;
        this.site = new Square[length][width];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                site[i][j] = new Square(SquareType.bySymbol(map[i][j]));
            }
        }
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
            if (heavyMachinery.isOutsideBoundary(length - 1, width - 1)) {
                return OUT_OF_BOUNDS;
            }
            heavyMachinery.advance();
            final Position position = heavyMachinery.getPosition();
            final boolean isStillPassing = i < steps - 1;
            final Square.VisitResult visitResult = site[position.y()][position.x()].visit(isStillPassing);
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

    public Position getHeavyMachineryPosition() {
        return heavyMachinery.getPosition();
    }

    public Direction getHeavyMachineryDirection() {
        return heavyMachinery.getDirection();
    }

    public Square[][] getSite() {
        return site.clone();
    }

    public SimulationCost getSimulationCost() {
        return new SimulationCost(List.of(
                new ClearingOperation(COMMUNICATION, getMachineryDrivingCommandsCount()),
                new ClearingOperation(FUEL, fuelCost),
                new ClearingOperation(UNCLEARED_SQUARE, getUnclearedSquaresCount()),
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

    private long getUnclearedSquaresCount() {
        return Stream.of(site)
                .flatMap(Stream::of)
                .filter(square -> !(square.isCleared() || square.isProtectedTree()))
                .count();
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
}

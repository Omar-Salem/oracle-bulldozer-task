package org.omarsalem.models;

import java.util.*;
import java.util.stream.Stream;

import static org.omarsalem.models.ClearingOperationType.*;
import static org.omarsalem.models.Direction.EAST;
import static org.omarsalem.models.Square.CLEARED;

public class ConstructionSite {
    private final Square[][] site;
    private final int length;
    private final int width;
    private final Bulldozer bulldozer;
    private final List<Command> commands = new ArrayList<>();
    private double fuelCost = 0;
    private double protectedTreePenalty = 0;
    private double paintDamage = 0;

    public ConstructionSite(char[][] map) {
        this(map, new Bulldozer(new Position(-1, 0), EAST));
    }

    public ConstructionSite(char[][] map, Bulldozer bulldozer) {
        this.bulldozer = bulldozer;
        if (Stream.of(map)
                .anyMatch(row -> row.length != map[0].length)) {
            throw new IllegalArgumentException("Site rows length mismatch");
        }
        this.length = map.length;
        this.width = map[0].length;
        this.site = new Square[length][width];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                site[i][j] = Square.bySymbol(map[i][j]);
            }
        }
    }

    public boolean handleCommand(Command command) {
        boolean simulationEnded = false;
        switch (command.getCommandType()) {
            case ADVANCE -> {
                final PayloadCommand<Integer> payloadCommand = (PayloadCommand<Integer>) command;
                simulationEnded = advance(payloadCommand.getPayload());
            }
            case LEFT -> bulldozer.left();
            case RIGHT -> bulldozer.right();
            case QUIT -> simulationEnded = true;
            default -> throw new IllegalStateException("Unhandled command type: " + command.getCommandType());
        }
        commands.add(command);
        return simulationEnded;
    }

    private boolean advance(Integer steps) {
        for (int i = 0; i < steps; i++) {
            if (bulldozer.isOutsideBoundary(length - 1, width - 1)) {
                return true;
            }
            bulldozer.advance();
            final int x = bulldozer.getPosition().x();
            final int y = bulldozer.getPosition().y();
            final Square currentSquare = site[y][x];
            site[y][x] = CLEARED;
            fuelCost += currentSquare.getFuelCost();
            if (currentSquare.isProtectedTree()) {
                protectedTreePenalty++;
                return true;
            }
            final boolean isStillPassing = i < steps - 1;
            if (currentSquare.isTree() && isStillPassing) {
                paintDamage++;
            }
        }
        return false;
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public Bulldozer getBulldozer() {
        return bulldozer;
    }

    public Square[][] getSite() {
        return site.clone();
    }

    public SimulationCost getSimulationCost() {
        return new SimulationCost(List.of(
                new ClearingOperation(COMMUNICATION, commands.size()),
                new ClearingOperation(FUEL, fuelCost),
                new ClearingOperation(UNCLEARED_SQUARE, getUnclearedSquaresCount()),
                new ClearingOperation(PROTECTED_TREE_DESTRUCTION, protectedTreePenalty),
                new ClearingOperation(PAINT_DAMAGE, paintDamage)
        ));
    }

    private long getUnclearedSquaresCount() {
        return Stream.of(site)
                .flatMap(Stream::of)
                .filter(square -> !(square.isCleared() || square.isProtectedTree()))
                .count();
    }
}

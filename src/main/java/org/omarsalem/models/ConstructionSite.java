package org.omarsalem.models;

import java.util.*;
import java.util.stream.Stream;

import static org.omarsalem.models.Direction.EAST;
import static org.omarsalem.models.Square.CLEARED;

public class ConstructionSite {
    private final Square[][] site;
    private final int length;
    private final int width;
    private final Bulldozer bulldozer = new Bulldozer(-1, 0, EAST);
    private final List<Command> commands = new ArrayList<>();
    private int fuelCost = 0;
    private int damagePenalty = 0;

    public ConstructionSite(char[][] map) {
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
            final int x = bulldozer.getX();
            final int y = bulldozer.getY();
            final Square currentSquare = site[y][x];
            site[y][x] = CLEARED;
            fuelCost += currentSquare.getFuelCost();
            if (currentSquare.isProtectedTree()) {
                damagePenalty += 10;
                return true;
            }
            final boolean stillPassing = i < steps - 1;
            if (currentSquare.isTree() && stillPassing) {
                damagePenalty += 2;
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

    public int getFuelCost() {
        return fuelCost;
    }

    public Square[][] getSite() {
        return site.clone();
    }

    public long getPenalty() {
        return commands.size() +
                damagePenalty +
                fuelCost +
                getUnclearedSquaresCount();
    }

    private long getUnclearedSquaresCount() {
        return Stream.of(site)
                .flatMap(Stream::of)
                .filter(square -> !(square.isCleared() || square.isProtectedTree()))
                .count();
    }
}

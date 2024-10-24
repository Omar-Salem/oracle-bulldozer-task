package org.omarsalem.models;

import java.util.*;

import static org.omarsalem.models.Direction.EAST;
import static org.omarsalem.models.Square.CLEARED;

public class ConstructionSite {
    private final Square[][] site;
    private final int length;
    private final int width;
    private final Bulldozer bulldozer = new Bulldozer(-1, 0, EAST);
    private final List<Command> commands = new ArrayList<>();
    private int fuelCost = 0;
    private int penalty = 0;

    public ConstructionSite(char[][] map) {
        if (Arrays.stream(map)
                .anyMatch(row -> row.length != map[0].length)) {
            throw new IllegalArgumentException("Site rows length mismatch");
        }
        this.length = map.length - 1;
        this.width = map[0].length - 1;
        this.site = new Square[map.length][map[0].length];

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
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
            if (bulldozer.isOutsideBoundary(length, width)) {
                return true;
            }
            bulldozer.advance();
            final int x = bulldozer.getX();
            final int y = bulldozer.getY();
            final Square currentSquare = site[x][y];
            fuelCost += currentSquare.getFuelCost();
            if (currentSquare.isProtectedTree()) {
                penalty += 10;
                return true;
            }
            final boolean stillPassing = i < steps - 1;
            if (currentSquare.isTree() && stillPassing) {
                penalty += 2;
            }
            site[x][y] = CLEARED;
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
}

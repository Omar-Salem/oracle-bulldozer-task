package org.omarsalem.models;

import java.util.*;

import static org.omarsalem.models.Direction.EAST;

public class ConstructionSite {
    private final Square[][] site;
    private final int length;
    private final int width;
    private final Bulldozer bulldozer = new Bulldozer(-1, 0, EAST);
    private final List<Command> commands = new ArrayList<>();

    public ConstructionSite(char[][] map) {
        if (Arrays.stream(map)
                .anyMatch(row -> row.length != map.length)) {
            throw new IllegalArgumentException("Site is not a square");
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
            case ADVANCE:
                final PayloadCommand<Integer> payloadCommand = (PayloadCommand<Integer>) command;
                for (int i = 0; i < payloadCommand.getPayload(); i++) {
                    if (bulldozer.isOutsideBoundary(length, width)) {
                        simulationEnded = true;
                        break;
                    } else {
                        bulldozer.advance();
                    }
                }
                break;
            case LEFT:
                bulldozer.left();
                break;
            case RIGHT:
                bulldozer.right();
                break;
            case QUIT:
                simulationEnded = true;
                break;
            default:
                throw new IllegalStateException("Unhandled command type: " + command.getCommandType());
        }
        commands.add(command);
        return simulationEnded;
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public Bulldozer getBulldozer() {
        return bulldozer;
    }
}

package org.omarsalem.models;

import java.util.*;

import static org.omarsalem.models.Direction.EAST;

public class ConstructionSite {
    private final char[][] map;
    private final int length;
    private final int width;
    private final Bulldozer bulldozer = new Bulldozer(-1, 0, EAST);
    private final List<Command> commands = new ArrayList<>();

    public ConstructionSite(char[][] map) {
        this.map = map.clone();
        this.length = map.length - 1;
        this.width = map[0].length - 1;
    }

    public void handleCommand(Command command) {
        switch (command.getCommandType()) {
            case ADVANCE:
                final PayloadCommand<Integer> payloadCommand = (PayloadCommand<Integer>) command;
                for (int i = 0; i < payloadCommand.getPayload(); i++) {
                    bulldozer.advance();
                }
                break;
            case LEFT:
                bulldozer.left();
                break;
            case RIGHT:
                bulldozer.right();
                break;
            case QUIT:
                break;
            default:
                throw new IllegalStateException("Unhandled command type: " + command.getCommandType());
        }
        commands.add(command);
    }

    public List<Command> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public Bulldozer getBulldozer() {
        return bulldozer;
    }
}

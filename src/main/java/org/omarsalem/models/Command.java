package org.omarsalem.models;

public class Command {
    private final CommandType commandType;

    public Command(CommandType commandType) {
        this.commandType = commandType;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public String toString() {
        return getCommandType().getDescription();
    }
}

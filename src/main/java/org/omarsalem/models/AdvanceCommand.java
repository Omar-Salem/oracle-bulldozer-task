package org.omarsalem.models;

public class AdvanceCommand extends Command {
    private final int steps;

    public AdvanceCommand(CommandType commandType, int steps) {
        super(commandType);
        if (steps < 1) {
            throw new IllegalArgumentException("steps should be positive");
        }
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }

    @Override
    public String toString() {
        return "%s %s".formatted(super.toString(), steps);
    }
}

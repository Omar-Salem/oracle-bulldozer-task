package org.omarsalem.models;

public class PayloadCommand<T> extends Command {
    private final T payload;

    public PayloadCommand(CommandType commandType, T payload) {
        super(commandType);
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "%s %s".formatted(super.toString(), payload);
    }
}

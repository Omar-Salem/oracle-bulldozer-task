package org.omarsalem.models;

public enum CommandType {
    ADVANCE('a', "advance"),
    LEFT('l', "turn left"),
    RIGHT('r', "turn right"),
    QUIT('q', "quit");


    private final char symbol;
    private final String description;

    CommandType(char symbol,String description) {
        this.symbol = symbol;
        this.description = description;
    }

    public static CommandType bySymbol(char symbol) {
        for (CommandType e : values()) {
            if (e.symbol == symbol) {
                return e;
            }
        }
        throw new IllegalArgumentException("Could not find CommandType for symbol " + symbol);
    }

    public String getDescription() {
        return description;
    }
}

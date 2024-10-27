package org.omarsalem.models;

public enum SquareType {
    PLAIN('o', 1),
    CLEARED('c', PLAIN.fuelCost),
    ROCKY('r', PLAIN.fuelCost * 2),
    TREE('t', PLAIN.fuelCost * 2),
    PROTECTED_TREE('T', PLAIN.fuelCost * 2);

    private final int fuelCost;
    private final char symbol;

    SquareType(char symbol, int fuelCost) {
        this.fuelCost = fuelCost;
        this.symbol = symbol;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public char getSymbol() {
        return symbol;
    }

    public static SquareType bySymbol(char symbol) {
        for (SquareType e : values()) {
            if (e.symbol == symbol) {
                return e;
            }
        }
        throw new IllegalArgumentException("Could not find Square for symbol " + symbol);
    }
}

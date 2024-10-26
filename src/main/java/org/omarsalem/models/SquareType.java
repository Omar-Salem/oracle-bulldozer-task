package org.omarsalem.models;

public enum SquareType {
    PLAIN(1, 'o'),
    CLEARED(1, 'c'),
    ROCKY(2, 'r'),
    TREE(2, 't'),
    PROTECTED_TREE(2, 'T');

    private final int fuelCost;
    private final char symbol;

    SquareType(int fuelCost, char symbol) {
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

package org.omarsalem.models;

public enum Square {
    PLAIN(1, 'o'),
    CLEARED(1, 'c'),
    ROCKY(2, 'r'),
    TREE(2, 't'),
    PROTECTED_TREE(0, 'T');

    private final int fuelCost;
    private final char symbol;

    Square(int fuelCost, char symbol) {
        this.fuelCost = fuelCost;
        this.symbol = symbol;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public boolean endsSimulation() {
        return this == PROTECTED_TREE;
    }

    public static Square bySymbol(char symbol) {
        for (Square e : values()) {
            if (e.symbol == symbol) {
                return e;
            }
        }
        throw new IllegalArgumentException("Could not find Square for symbol " + symbol);
    }
}

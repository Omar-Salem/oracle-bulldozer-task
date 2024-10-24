package org.omarsalem.models;

public enum Square {
    PLAIN(1, 'o', 0),
    CLEARED(1, 'c', 0),
    ROCKY(2, 'r', 0),
    TREE(2, 't', 2),
    PROTECTED_TREE(2, 'T', 10);

    private final int fuelCost;
    private final char symbol;
    private final int penalty;

    Square(int fuelCost, char symbol, int penalty) {
        this.fuelCost = fuelCost;
        this.symbol = symbol;
        this.penalty = penalty;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isProtectedTree() {
        return this == PROTECTED_TREE;
    }

    public boolean isCleared() {
        return this == CLEARED;
    }

    public boolean isTree() {
        return this == TREE;
    }

    public static Square bySymbol(char symbol) {
        for (Square e : values()) {
            if (e.symbol == symbol) {
                return e;
            }
        }
        throw new IllegalArgumentException("Could not find Square for symbol " + symbol);
    }

    public int getPenalty(boolean isStillPassing) {
        return isTree() && isStillPassing ? penalty : 0;
    }
}

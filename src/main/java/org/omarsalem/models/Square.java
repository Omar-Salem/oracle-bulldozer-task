package org.omarsalem.models;

import static org.omarsalem.models.SquareType.*;

public final class Square {
    private SquareType squareType;

    public Square(SquareType squareType) {
        this.squareType = squareType;
    }

    public VisitResult visit(boolean isStillPassing) {
        final VisitResult visitResult = new VisitResult(squareType.getFuelCost(),
                PROTECTED_TREE.equals(squareType) ? 1 : 0,
                TREE.equals(squareType) && isStillPassing ? 1 : 0);
        this.squareType = SquareType.CLEARED;
        return visitResult;
    }

    public boolean isProtectedTree() {
        return squareType == PROTECTED_TREE;
    }

    public boolean isCleared() {
        return squareType == CLEARED;
    }

    public char getSymbol() {
        return squareType.getSymbol();
    }


    public record VisitResult(int fuelCost,
                              double protectedTreePenalty,
                              double paintDamage) {

    }
}

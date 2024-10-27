package org.omarsalem.models;

import java.util.*;

import static org.omarsalem.models.SquareType.*;

public final class Square {
    private static final Set<SquareType> CLEARED_TYPES = Collections.unmodifiableSet(EnumSet.of(CLEARED, PROTECTED_TREE));
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

    public boolean isCleared() {
        return CLEARED_TYPES.contains(squareType);
    }


    public record VisitResult(int fuelCost,
                              double protectedTreePenalty,
                              double paintDamage) {

    }
}

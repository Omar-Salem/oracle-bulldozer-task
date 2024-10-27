package org.omarsalem.models;

import java.util.stream.Stream;

public class ConstructionSite {
    private final Square[][] squares;
    private final int length;
    private final int width;

    public ConstructionSite(char[][] map) {
        if (Stream.of(map)
                .anyMatch(row -> row.length != map[0].length)) {
            throw new IllegalArgumentException("Site rows length mismatch");
        }
        this.length = map.length;
        this.width = map[0].length;
        this.squares = new Square[length][width];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                final char symbol = map[i][j];
                final SquareType squareType = SquareType.bySymbol(symbol);
                squares[i][j] = new Square(squareType);
            }
        }
    }

    public long getUnclearedSquaresCount() {
        return Stream.of(squares)
                .flatMap(Stream::of)
                .filter(square -> !square.isCleared())
                .count();
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public Square.VisitResult visit(Position position, boolean isStillPassing) {
        final Square square = squares[position.y()][position.x()];
        return square.visit(isStillPassing);
    }
}

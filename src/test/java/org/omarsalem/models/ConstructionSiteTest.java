package org.omarsalem.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstructionSiteTest {

    @Test
    void calculating_uncleared_squares_count() {
        //Arrange
        final char[][] map = {
                {'o', 'c', 'c', 't', 'T'},
                {'o', 'c', 'r', 't', 'T'},
        };
        final ConstructionSite constructionSite = new ConstructionSite(map);

        //Act
        final long actual = constructionSite.getUnclearedSquaresCount();

        //Assert
        assertEquals(5, actual);
    }
}
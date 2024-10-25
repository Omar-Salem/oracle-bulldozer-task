package org.omarsalem.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearingOperationTest {

    @Test
    void single_clearing_operation_total_sum() {
        //Arrange
        final ClearingOperation target = new ClearingOperation(ClearingOperationType.PAINT_DAMAGE, 3);

        //Act
        final double totalCost = target.getTotalCost();

        //Assert
        assertEquals(6, totalCost);
    }
}
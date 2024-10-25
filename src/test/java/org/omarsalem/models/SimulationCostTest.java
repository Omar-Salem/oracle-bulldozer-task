package org.omarsalem.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationCostTest {

    @Test
    void sum_of_clearing_operations() {
        //Arrange
        final SimulationCost target = new SimulationCost(List.of(
                new ClearingOperation(ClearingOperationType.FUEL, 1),
                new ClearingOperation(ClearingOperationType.PAINT_DAMAGE, 2),
                new ClearingOperation(ClearingOperationType.PROTECTED_TREE_DESTRUCTION, 1)
        ));

        //Act
        final double totalCost = target.getTotalCost();

        //Assert
        assertEquals(15, totalCost);
    }
}
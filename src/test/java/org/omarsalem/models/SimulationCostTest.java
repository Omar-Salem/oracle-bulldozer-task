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

    @Test
    void string_representation_as_table() {
        //Arrange
        final SimulationCost target = new SimulationCost(List.of(
                new ClearingOperation(ClearingOperationType.COMMUNICATION, 7),
                new ClearingOperation(ClearingOperationType.FUEL, 19),
                new ClearingOperation(ClearingOperationType.UNCLEARED_SQUARE, 34),
                new ClearingOperation(ClearingOperationType.PROTECTED_TREE_DESTRUCTION, 0),
                new ClearingOperation(ClearingOperationType.PAINT_DAMAGE, 2)
        ));

        //Act
        final String actual = target.toString();

        //Assert
        assertEquals("""
                Item                                            Quantity       Cost\s
                communication overhead                                 7          7\s
                fuel usage                                            19         19\s
                uncleared squares                                     34        102\s
                destruction of protected tree                          0          0\s
                paint damage to bulldozer                              2          4\s
                ---
                Total                                                132\s
                """, actual);
    }
}
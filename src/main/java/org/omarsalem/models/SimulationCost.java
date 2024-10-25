package org.omarsalem.models;

import java.util.List;

public record SimulationCost(List<ClearingOperation> operations) {
    double getTotalCost() {
        return operations
                .stream()
                .mapToDouble(o -> o.getTotalCost())
                .sum();
    }

    double getCostByOperationType(ClearingOperationType operationType) {
        return operations()
                .stream()
                .filter(o -> operationType.equals(o.clearingOperationType()))
                .findAny()
                .orElseThrow()
                .getTotalCost();
    }
}

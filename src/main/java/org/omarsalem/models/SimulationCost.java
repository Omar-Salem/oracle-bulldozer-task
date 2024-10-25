package org.omarsalem.models;

import java.util.List;

public record SimulationCost(List<ClearingOperation> operations) {
    public double getTotalCost() {
        return operations
                .stream()
                .mapToDouble(ClearingOperation::getTotalCost)
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

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

    @Override
    public String toString() {
        final String headerFormat = "%-40s %15s %10s %n";
        final String rowsFormat = "%-40s %15.0f %10.0f %n";
        final String totalFormat = "%-40s %15.0f %n";
        final StringBuilder table = new StringBuilder(String.format(headerFormat, "Item", "Quantity", "Cost"));
        for (ClearingOperation c : operations()) {
            final ClearingOperationType operationType = c.clearingOperationType();
            table.append(String.format(rowsFormat, operationType.getDescription(), c.quantity(), c.getTotalCost()));
        }
        table.append("---\n");
        table.append(String.format(totalFormat, "Total", getTotalCost()));
        return table.toString();
    }
}

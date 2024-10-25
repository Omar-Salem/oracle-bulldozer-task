package org.omarsalem.models;

public record ClearingOperation(ClearingOperationType clearingOperationType, double quantity) {

    public double getTotalCost() {
        return quantity * clearingOperationType.getCredit();
    }
}

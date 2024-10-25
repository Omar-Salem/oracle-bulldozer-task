package org.omarsalem.models;

public record ClearingOperation(ClearingOperationType clearingOperationType, double quantity) {

    double getTotalCost() {
        return quantity * clearingOperationType.getCredit();
    }
}

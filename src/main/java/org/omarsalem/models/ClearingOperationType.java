package org.omarsalem.models;

public enum ClearingOperationType {
    COMMUNICATION("communication overhead", 1),
    FUEL("fuel usage", 1),
    UNCLEARED_SQUARE("uncleared squares", 3),
    PROTECTED_TREE_DESTRUCTION("destruction of protected tree", 10),
    PAINT_DAMAGE("paint damage to bulldozer", 2);

    private final String description;
    private final int credit;

    ClearingOperationType(String description, int credit) {
        this.description = description;
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public int getCredit() {
        return credit;
    }
}

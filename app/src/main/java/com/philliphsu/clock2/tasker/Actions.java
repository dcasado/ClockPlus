package com.philliphsu.clock2.tasker;

public enum Actions {
    CREATE(0),
    DELETE(1),
    ENABLE(2);

    private final int value;

    Actions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

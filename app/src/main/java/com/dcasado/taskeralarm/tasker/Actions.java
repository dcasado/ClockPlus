package com.dcasado.taskeralarm.tasker;

public enum Actions {
    CREATE(0),
    DELETE(1),
    ENABLE(2),
    DISABLE(3);

    private final int value;

    Actions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

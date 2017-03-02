package org.ikmich.sqlitefoo.ui;

public enum ActionType {
    ADD("add"), EDIT("edit");

    private String value;

    ActionType(String val) {
        this.value = val;
    }

    public String getValue() {
        return this.value;
    }
}

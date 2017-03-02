package org.ikmich.sqlitefoo.ui;

public enum ActionType {
    ADD("add"), EDIT("edit"), DELETE("delete");

    private String value;

    ActionType(String val) {
        this.value = val;
    }

    public String getValue() {
        return this.value;
    }
}

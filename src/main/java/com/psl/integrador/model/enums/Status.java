package com.psl.integrador.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum Status implements Serializable {
    toOpen,
    opened,
    closed;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}

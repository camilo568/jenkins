package com.psl.integrador.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Expertise {
    beginner,
    intermediate,
    expert;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}

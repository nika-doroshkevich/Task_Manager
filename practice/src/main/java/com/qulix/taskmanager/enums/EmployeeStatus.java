package com.qulix.taskmanager.enums;

import com.qulix.taskmanager.enumeration.BaseConverter;
import com.qulix.taskmanager.enumeration.BaseEnum;

public enum EmployeeStatus implements BaseEnum<EmployeeStatus> {

    ACTIVE(1),
    DELETED(2);

    private final int code;

    EmployeeStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static class Converter extends BaseConverter<EmployeeStatus> {

        public Converter() {
            super(EmployeeStatus.class);
        }
    }
}

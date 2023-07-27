package com.qulix.taskmanager.enums;

import com.qulix.taskmanager.enumeration.BaseConverter;
import com.qulix.taskmanager.enumeration.BaseEnum;

public enum TaskStatus implements BaseEnum<TaskStatus> {

    NOT_STARTED(1),
    IN_PROGRESS(2),
    COMPLETED(3),
    POSTPONED(4),
    DELETED(5);

    private final int code;

    TaskStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static class Converter extends BaseConverter<TaskStatus> {

        public Converter() {
            super(TaskStatus.class);
        }
    }
}

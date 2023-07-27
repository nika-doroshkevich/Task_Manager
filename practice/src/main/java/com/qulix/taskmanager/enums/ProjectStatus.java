package com.qulix.taskmanager.enums;

import com.qulix.taskmanager.enumeration.BaseConverter;
import com.qulix.taskmanager.enumeration.BaseEnum;

public enum ProjectStatus implements BaseEnum<ProjectStatus> {

    ACTIVE(1),
    DELETED(2);

    private final int code;

    ProjectStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static class Converter extends BaseConverter<ProjectStatus> {

        public Converter() {
            super(ProjectStatus.class);
        }
    }
}

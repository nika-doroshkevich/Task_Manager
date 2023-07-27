package com.qulix.taskmanager.enumeration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.hibernate.query.sqm.sql.ConversionException;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Converter
public abstract class BaseConverter<E extends BaseEnum<E>> implements AttributeConverter<E, Integer> {

    private final Map<Integer, E> mappings;
    private final String enumName;

    protected BaseConverter(Class<E> clazz) {
        enumName = clazz.getSimpleName();
        mappings = buildMappings(clazz);
    }

    @Override
    public final Integer convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public final E convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Optional.ofNullable(mappings.get(dbData))
                .orElseThrow(() -> new ConversionException(enumName + " not found for code " + dbData, null));
    }

    private Map<Integer, E> buildMappings(Class<E> clazz) {
        return Arrays.stream(clazz.getEnumConstants())
                .collect(toMap(BaseEnum::getCode, Function.identity()));
    }
}

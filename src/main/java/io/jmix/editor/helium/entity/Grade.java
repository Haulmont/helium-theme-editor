package io.jmix.editor.helium.entity;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum Grade implements EnumClass<Integer> {

    MINIMAL(10),
    STANDARD(20),
    EXTENDED(30);

    private Integer id;

    Grade(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static Grade fromId(Integer id) {
        for (Grade at : Grade.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
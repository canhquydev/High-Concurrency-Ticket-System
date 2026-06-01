package com.quy.highconcurrency_ticket_system.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateResourceException extends RuntimeException {
    private String fieldName;
    private Object fieldValue;
    public DuplicateResourceException(String fieldName, Object fieldValue) {
        super(fieldName + " already exists") ;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}

package com.training.demo.controllers.exception;

import java.util.function.Supplier;

public class CanNotFoundException extends Exception {

    public CanNotFoundException(String message) {
        super(message);
    }
}
